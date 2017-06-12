package com.dewarder.pickerok;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.annimon.stream.Stream;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PickerActivity extends AppCompatActivity implements
        PickerPanelView.OnSubmitClickListener,
        PickerPanelView.OnCancelClickListener,
        PickerAdapter.Controller<PickerData> {

    private static final String EXTRA_NAME = "EXTRA_NAME";
    private static final String EXTRA_DATA = "EXTRA_DATA";
    private static final String EXTRA_PICKED = "EXTRA_PICKED";
    private static final String EXTRA_TOTAL_PICKED = "EXTRA_TOTAL_PICKED";
    private static final String EXTRA_RESULT = "EXTRA_RESULT_FILES";

    private int mTotalPicked;
    private final ArrayList<PickerData> mData = new ArrayList<>();
    private final Set<PickerData> mInitialPicked = new HashSet<>();
    private final Set<PickerData> mPickedImages = new HashSet<>();

    private RecyclerView mPickerRecycler;
    private GridLayoutManager mPickerLayoutManager;
    private PickerAdapter<PickerData> mPickerAdapter;

    private Toolbar mToolbar;
    private PickerPanelView mPickerPanel;

    private int mItemMinSize;
    private int mItemSpacing;

    public static void startForResult(Activity activity,
                                      int requestCode,
                                      String name,
                                      List<File> data,
                                      List<File> picked,
                                      int totalPicked) {

        Intent intent = new Intent(activity, PickerActivity.class);
        intent.putExtra(EXTRA_NAME, name);
        intent.putExtra(EXTRA_DATA, new ArrayList<>(data));
        intent.putExtra(EXTRA_PICKED, new ArrayList<>(picked));
        intent.putExtra(EXTRA_TOTAL_PICKED, totalPicked);
        activity.startActivityForResult(intent, requestCode);
    }

    public static Result getResult(Intent intent) {
        return (Result) intent.getSerializableExtra(EXTRA_RESULT);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acitivty_picker);

        mItemMinSize = getResources().getDimensionPixelSize(R.dimen.item_picker_image_min_size);
        mItemSpacing = getResources().getDimensionPixelSize(R.dimen.spacing_default);

        List<File> files = ActivityUtils.getSerializableArgument(this, EXTRA_DATA);
        List<File> picked = ActivityUtils.getSerializableArgument(this, EXTRA_PICKED);
        String title = getIntent().getExtras().getString(EXTRA_NAME);
        mTotalPicked = getIntent().getExtras().getInt(EXTRA_TOTAL_PICKED, 0);

        mData.addAll(Stream.of(files).map(PickerData::new).toList());
        mInitialPicked.addAll(Stream.of(picked).map(PickerData::new).toList());
        mPickedImages.addAll(mInitialPicked);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(title);
        mPickerPanel = (PickerPanelView) findViewById(R.id.picker_panel);
        mPickerPanel.setOnSubmitClickListener(this);
        mPickerPanel.setOnCancelClickListener(this);
        mPickerPanel.setPickedCount(mTotalPicked);
        mPickerRecycler = (RecyclerView) findViewById(R.id.picker_recycler);
        mPickerRecycler.post(() -> {
            int spanCount = RecyclerUtils.calculateSpanCount(mPickerRecycler, mItemMinSize);
            int itemSize = RecyclerUtils.calculateItemSize(mPickerRecycler, spanCount, mItemMinSize, mItemSpacing);
            mPickerLayoutManager = new GridLayoutManager(this, spanCount);
            mPickerRecycler.addItemDecoration(new GridSpacingItemDecoration(spanCount, mItemSpacing, true));
            mPickerAdapter = new PickerAdapter<>(this, new PickerDataPreviewFetcher(this));
            mPickerAdapter.setCategoryItemSize(itemSize);
            mPickerAdapter.setData(mData);
            mPickerRecycler.setAdapter(mPickerAdapter);
            mPickerRecycler.setLayoutManager(mPickerLayoutManager);
        });
    }

    @Override
    public boolean isPicked(PickerData item) {
        return mPickedImages.contains(item);
    }

    @Override
    public void onPick(PickerData item) {
        mTotalPicked++;
        mPickerPanel.setPickedCount(mTotalPicked);
        mPickedImages.add(item);
    }

    @Override
    public void onUnpick(PickerData item) {
        mTotalPicked--;
        mPickerPanel.setPickedCount(mTotalPicked);
        mPickedImages.remove(item);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                finishWithResult(Result.of(getChecked(), getUnchecked()));
                break;
            }
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        finishWithResult(Result.of(getChecked(), getUnchecked()));
    }

    @Override
    public void onCancelClicked() {
        finishWithResult(Result.cancel());
    }

    @Override
    public void onSubmitClicked() {
        finishWithResult(Result.submit(getChecked(), getUnchecked()));
    }

    private List<File> getChecked() {
        return Stream.of(mPickedImages)
                .filterNot(mInitialPicked::contains)
                .map(PickerData::getFile)
                .toList();
    }

    private List<File> getUnchecked() {
        return Stream.of(mData)
                .filterNot(mPickedImages::contains)
                .map(PickerData::getFile)
                .toList();
    }

    private void finishWithResult(Result result) {
        Intent intent = new Intent();
        intent.putExtra(EXTRA_RESULT, result);
        setResult(RESULT_OK, intent);
        finish();
    }

    public static class Result implements Serializable {

        private final List<File> mChecked;
        private final List<File> mUnchecked;
        private final boolean mIsSubmitted;
        private final boolean mIsCanceled;

        private Result(List<File> checked, List<File> unchecked, boolean isSubmitted, boolean isCanceled) {
            mChecked = checked;
            mUnchecked = unchecked;
            mIsSubmitted = isSubmitted;
            mIsCanceled = isCanceled;
        }

        public List<File> getChecked() {
            return mChecked;
        }

        public List<File> getUnchecked() {
            return mUnchecked;
        }

        public boolean isSubmitted() {
            return mIsSubmitted;
        }

        public boolean isCanceled() {
            return mIsCanceled;
        }

        public static Result of(List<File> checked, List<File> unchecked) {
            return new Result(checked, unchecked, false, false);
        }

        public static Result submit(List<File> checked, List<File> unchecked) {
            return new Result(checked, unchecked, true, false);
        }

        public static Result cancel() {
            return new Result(Collections.emptyList(), Collections.emptyList(), false, true);
        }
    }
}
