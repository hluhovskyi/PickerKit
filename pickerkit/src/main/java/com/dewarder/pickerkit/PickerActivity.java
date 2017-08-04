package com.dewarder.pickerkit;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.annimon.stream.Stream;
import com.dewarder.pickerkit.utils.Activities;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public final class PickerActivity extends AppCompatActivity implements
        PickerPanelView.OnSubmitClickListener,
        PickerPanelView.OnCancelClickListener,
        PickerAdapter.DataController<FilePickerData>,
        PickerAdapter.PickerController<FilePickerData> {

    private static final String EXTRA_NAME = "EXTRA_NAME";
    private static final String EXTRA_ACCENT_COLOR = "EXTRA_ACCENT_COLOR";
    private static final String EXTRA_DATA = "EXTRA_DATA";
    private static final String EXTRA_PICKED = "EXTRA_PICKED";
    private static final String EXTRA_TOTAL_PICKED = "EXTRA_TOTAL_PICKED";
    private static final String EXTRA_RESULT = "EXTRA_RESULT";
    private static final String EXTRA_LIMIT = "EXTRA_LIMIT";

    private int mTotalPicked;
    private int mLimit;
    private final ArrayList<FilePickerData> mData = new ArrayList<>();
    private final Set<FilePickerData> mInitialPicked = new HashSet<>();
    private final Set<FilePickerData> mPickedImages = new HashSet<>();

    private RecyclerView mPickerRecycler;
    private GridLayoutManager mPickerLayoutManager;
    private PickerAdapter<FilePickerData> mPickerAdapter;

    private Toolbar mToolbar;
    private PickerPanelView mPickerPanel;

    private int mAccentColor;
    private int mItemMinSize;
    private int mItemSpacing;

    public static Result getResult(Intent intent) {
        return (Result) intent.getSerializableExtra(EXTRA_RESULT);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setTheme(R.style.PickerActivityTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acitivty_picker);

        mItemMinSize = getResources().getDimensionPixelSize(R.dimen.item_picker_image_min_size);
        mItemSpacing = getResources().getDimensionPixelSize(R.dimen.spacing_default);

        Bundle extras = getIntent().getExtras();

        int accentColor = extras.getInt(EXTRA_ACCENT_COLOR);
        mAccentColor = accentColor == -1 ? ContextCompat.getColor(this, R.color.colorAccent) : accentColor;

        List<File> files = Activities.getSerializableArgument(this, EXTRA_DATA);
        List<File> picked = Activities.getSerializableArgument(this, EXTRA_PICKED);
        String title = getIntent().getExtras().getString(EXTRA_NAME);
        mTotalPicked = getIntent().getExtras().getInt(EXTRA_TOTAL_PICKED, 0);
        mLimit = getIntent().getExtras().getInt(EXTRA_LIMIT, -1);

        mData.addAll(Stream.of(files).map(FilePickerData::from).toList());
        mInitialPicked.addAll(Stream.of(picked).map(FilePickerData::from).toList());
        mPickedImages.addAll(mInitialPicked);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(title);

        mPickerPanel = (PickerPanelView) findViewById(R.id.picker_panel);
        mPickerPanel.setAccentColor(mAccentColor);
        mPickerPanel.setOnSubmitClickListener(this);
        mPickerPanel.setOnCancelClickListener(this);
        mPickerPanel.setPickedCount(mTotalPicked);

        mPickerRecycler = (RecyclerView) findViewById(R.id.picker_recycler);
        mPickerRecycler.post(() -> {
            int spanCount = RecyclerUtils.calculateSpanCount(mPickerRecycler, mItemMinSize);
            int itemSize = RecyclerUtils.calculateItemSize(mPickerRecycler, spanCount, mItemMinSize, mItemSpacing);
            mPickerLayoutManager = new GridLayoutManager(this, spanCount);
            mPickerRecycler.addItemDecoration(new GridSpacingItemDecoration(spanCount, mItemSpacing, true));
            mPickerAdapter = new PickerAdapter.Builder<FilePickerData>()
                    .setPreviewFetcher(new FilePickerDataPreviewFetcher(this))
                    .setDataController(this)
                    .setPickerController(this)
                    .setPreviewParams(PreviewFetcher.Params.of(itemSize, itemSize))
                    .setData(mData)
                    .setPickEnabled(mLimit - mTotalPicked > 0)
                    .build();

            mPickerRecycler.setAdapter(mPickerAdapter);
            mPickerRecycler.setLayoutManager(mPickerLayoutManager);
        });
    }

    @Override
    public List<FilePickerData> getPicked() {
        return new ArrayList<>(mPickedImages);
    }

    @Override
    public boolean isPicked(FilePickerData item) {
        return mPickedImages.contains(item);
    }

    @Override
    public void onPick(FilePickerData item) {
        mTotalPicked++;
        mPickerPanel.setPickedCount(mTotalPicked);
        mPickedImages.add(item);
    }

    @Override
    public void onUnpick(FilePickerData item) {
        mTotalPicked--;
        mPickerPanel.setPickedCount(mTotalPicked);
        mPickedImages.remove(item);
    }

    @Override
    public void clearPicked() {
        mPickedImages.clear();
    }

    @Override
    public boolean isPickEnabled(Collection<FilePickerData> items) {
        return mLimit <= 0 || mLimit - mTotalPicked > 0;
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
                .map(FilePickerData::getSource)
                .toList();
    }

    private List<File> getUnchecked() {
        return Stream.of(mData)
                .filterNot(mPickedImages::contains)
                .map(FilePickerData::getSource)
                .toList();
    }

    private void finishWithResult(Result result) {
        Intent intent = new Intent();
        intent.putExtra(EXTRA_RESULT, result);
        setResult(RESULT_OK, intent);
        finish();
    }

    static class Result implements Serializable {

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

    public static class Builder {

        private final Activity mActivity;
        private int mRequestCode = -1;
        private String mName;
        private int mAccentColor = -1;
        private ArrayList<File> mData;
        private ArrayList<File> mPicked;
        private int mTotalPicked;
        private int mLimit = -1;

        public Builder(Activity activity) {
            mActivity = activity;
        }

        public Builder setRequestCode(int requestCode) {
            mRequestCode = requestCode;
            return this;
        }

        public Builder setName(String name) {
            mName = name;
            return this;
        }

        public Builder setAccentColor(@ColorInt int color) {
            mAccentColor = color;
            return this;
        }

        public Builder setData(List<File> data) {
            mData = new ArrayList<>(data);
            return this;
        }

        public Builder setPicked(List<File> picked) {
            mPicked = new ArrayList<>(picked);
            return this;
        }

        public Builder setTotalPicked(int totalPicked) {
            mTotalPicked = totalPicked;
            return this;
        }

        public Builder setLimit(int limit) {
            mLimit = limit;
            return this;
        }

        public void start() {
            Intent intent = new Intent(mActivity, PickerActivity.class);
            intent.putExtra(EXTRA_NAME, mName);
            intent.putExtra(EXTRA_ACCENT_COLOR, mAccentColor);
            intent.putExtra(EXTRA_DATA, mData != null ? mData : new ArrayList<File>());
            intent.putExtra(EXTRA_PICKED, mPicked != null ? mPicked : new ArrayList<File>());
            intent.putExtra(EXTRA_TOTAL_PICKED, mTotalPicked);
            intent.putExtra(EXTRA_LIMIT, mLimit);

            int requestCode = mRequestCode == -1 ? RequestCodeGenerator.generate() : mRequestCode;
            mActivity.startActivityForResult(intent, requestCode);
        }
    }
}
