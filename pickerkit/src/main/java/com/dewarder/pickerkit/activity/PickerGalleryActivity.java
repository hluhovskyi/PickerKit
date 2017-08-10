package com.dewarder.pickerkit.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.annimon.stream.Stream;
import com.dewarder.pickerkit.GridSpacingItemDecoration;
import com.dewarder.pickerkit.PickedMediaPreviewFetcher;
import com.dewarder.pickerkit.PickerItemAdapter;
import com.dewarder.pickerkit.PickerPanelView;
import com.dewarder.pickerkit.PreviewFetcher;
import com.dewarder.pickerkit.R;
import com.dewarder.pickerkit.RequestCodeGenerator;
import com.dewarder.pickerkit.model.PickerMedia;
import com.dewarder.pickerkit.result.PickerGalleryResult;
import com.dewarder.pickerkit.utils.Activities;
import com.dewarder.pickerkit.utils.Objects;
import com.dewarder.pickerkit.utils.Recyclers;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public final class PickerGalleryActivity extends AppCompatActivity implements
        PickerPanelView.OnSubmitClickListener,
        PickerPanelView.OnCancelClickListener,
        PickerItemAdapter.DataController<PickerMedia>,
        PickerItemAdapter.AccessibilityController<PickerMedia> {

    private static final String EXTRA_TITLE = "EXTRA_TITLE";
    private static final String EXTRA_ACCENT_COLOR = "EXTRA_ACCENT_COLOR";
    private static final String EXTRA_DATA = "EXTRA_DATA";
    private static final String EXTRA_PICKED = "EXTRA_PICKED";
    private static final String EXTRA_TOTAL_PICKED = "EXTRA_TOTAL_PICKED";
    private static final String EXTRA_RESULT = "EXTRA_RESULT";
    private static final String EXTRA_LIMIT = "EXTRA_LIMIT";

    private int mTotalPicked;
    private int mLimit;
    private final ArrayList<PickerMedia> mData = new ArrayList<>();
    private final Set<PickerMedia> mInitialPicked = new HashSet<>();
    private final Set<PickerMedia> mPickedImages = new HashSet<>();

    private RecyclerView mPickerRecycler;
    private GridLayoutManager mPickerLayoutManager;
    private PickerItemAdapter<PickerMedia> mPickerAdapter;

    private Toolbar mToolbar;
    private PickerPanelView mPickerPanel;

    private int mAccentColor;
    private int mItemMinSize;
    private int mItemSpacing;

    public static PickerGalleryResult getGalleryResult(Intent data) {
        return data.getParcelableExtra(EXTRA_RESULT);
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
        String title = getIntent().getExtras().getString(EXTRA_TITLE);
        mTotalPicked = getIntent().getExtras().getInt(EXTRA_TOTAL_PICKED, 0);
        mLimit = getIntent().getExtras().getInt(EXTRA_LIMIT, -1);

        mData.addAll(Activities.getSerializableArgument(this, EXTRA_DATA));
        mInitialPicked.addAll(Activities.getSerializableArgument(this, EXTRA_PICKED));
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
            int spanCount = Recyclers.calculateSpanCount(mPickerRecycler, mItemMinSize);
            int itemSize = Recyclers.calculateItemSize(mPickerRecycler, spanCount, mItemMinSize, mItemSpacing);
            mPickerLayoutManager = new GridLayoutManager(this, spanCount);
            mPickerRecycler.addItemDecoration(new GridSpacingItemDecoration(spanCount, mItemSpacing, true));
            mPickerAdapter = new PickerItemAdapter.Builder<PickerMedia>()
                    .setPreviewFetcher(new PickedMediaPreviewFetcher(this))
                    .setDataController(this)
                    .setAccessibilityController(this)
                    .setPreviewParams(PreviewFetcher.Params.of(itemSize, itemSize))
                    .setData(mData)
                    .setPickEnabled(mLimit - mTotalPicked > 0)
                    .build();

            mPickerRecycler.setAdapter(mPickerAdapter);
            mPickerRecycler.setLayoutManager(mPickerLayoutManager);
        });
    }

    @Override
    public List<PickerMedia> getPicked() {
        return new ArrayList<>(mPickedImages);
    }

    @Override
    public boolean isPicked(PickerMedia item) {
        return mPickedImages.contains(item);
    }

    @Override
    public void onPick(PickerMedia item) {
        mTotalPicked++;
        mPickerPanel.setPickedCount(mTotalPicked);
        mPickedImages.add(item);
    }

    @Override
    public void onUnpick(PickerMedia item) {
        mTotalPicked--;
        mPickerPanel.setPickedCount(mTotalPicked);
        mPickedImages.remove(item);
    }

    @Override
    public void clearPicked() {
        mPickedImages.clear();
    }

    @Override
    public boolean canPickMore(Collection<PickerMedia> items) {
        return mLimit <= 0 || mLimit - mTotalPicked > 0;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                finishWithResult(ResultGallery.of(getChecked(), getUnchecked()));
                break;
            }
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        finishWithResult(ResultGallery.of(getChecked(), getUnchecked()));
    }

    @Override
    public void onCancelClicked() {
        finishWithResult(ResultGallery.cancel());
    }

    @Override
    public void onSubmitClicked() {
        finishWithResult(ResultGallery.submit(getChecked(), getUnchecked()));
    }

    private List<PickerMedia> getChecked() {
        return Stream.of(mPickedImages)
                .filterNot(mInitialPicked::contains)
                .toList();
    }

    private List<PickerMedia> getUnchecked() {
        return Stream.of(mData)
                .filterNot(mPickedImages::contains)
                .toList();
    }

    private void finishWithResult(PickerGalleryResult result) {
        Intent intent = new Intent();
        intent.putExtra(EXTRA_RESULT, result);
        setResult(RESULT_OK, intent);
        finish();
    }

    public static class Starter {

        private final Activity activity;
        private int requestCode = -1;
        private String title;
        private ArrayList<PickerMedia> data;
        private ArrayList<PickerMedia> selected;
        private int totalSelected;
        private int limit = -1;

        public Starter(@NonNull Activity activity) {
            Objects.requireNonNull(activity);
            this.activity = activity;
        }

        @NonNull
        public Starter setRequestCode(int requestCode) {
            this.requestCode = requestCode;
            return this;
        }

        @NonNull
        public Starter setTitle(@NonNull String title) {
            Objects.requireNonNull(title);
            this.title = title;
            return this;
        }

        @NonNull
        public Starter setData(@NonNull List<PickerMedia> data) {
            Objects.requireNonNull(data);
            this.data = new ArrayList<>(data);
            return this;
        }

        @NonNull
        public Starter setSelected(@NonNull List<PickerMedia> selected) {
            Objects.requireNonNull(selected);
            this.selected = new ArrayList<>(selected);
            return this;
        }

        @NonNull
        public Starter setTotalSelected(@IntRange(from = 0, to = Integer.MAX_VALUE) int totalSelected) {
            this.totalSelected = totalSelected;
            return this;
        }

        @NonNull
        public Starter setLimit(@IntRange(from = 0, to = Integer.MAX_VALUE) int limit) {
            this.limit = limit;
            return this;
        }

        public void start() {
            Intent intent = new Intent(activity, PickerGalleryActivity.class);
            intent.putExtra(EXTRA_TITLE, title);
            intent.putExtra(EXTRA_DATA, data != null ? data : new ArrayList<File>());
            intent.putExtra(EXTRA_PICKED, selected != null ? selected : new ArrayList<File>());
            intent.putExtra(EXTRA_TOTAL_PICKED, totalSelected);
            intent.putExtra(EXTRA_LIMIT, limit);

            int code = requestCode == -1 ? RequestCodeGenerator.generate() : requestCode;
            activity.startActivityForResult(intent, code);
        }
    }
}
