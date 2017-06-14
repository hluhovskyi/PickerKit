package com.dewarder.pickerok;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ContextThemeWrapper;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.annimon.stream.Stream;
import com.github.rahatarmanahmed.cpv.CircularProgressView;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public final class CategoryActivity extends AppCompatActivity implements
        OnCategoryClickListener,
        PickerPanelView.OnSubmitClickListener,
        PickerPanelView.OnCancelClickListener, PickerPanelView.OnCounterClickListener {

    private static final String EXTRA_ACCENT_COLOR = "EXTRA_ACCENT_COLOR";
    private static final String EXTRA_RESULT = "EXTRA_RESULT";

    private static final int IMAGE_PICKER_ACTIVITY_REQUEST_CODE = 1;

    private PickerDataProvider<File> mImageDataProvider;
    private PickerDataProvider<File> mVideoDataProvider;
    private PickerDataProvider<File> mDataProvider;

    private Toolbar mToolbar;
    private TextView mTitle;
    private PickerPanelView mPickerPanel;
    private CircularProgressView mProgress;

    private RecyclerView mCategoryRecycler;
    private GridLayoutManager mCategoryLayoutManager;
    private CategoryAdapter mCategoryAdapter;

    private int mAccentColor;
    private int mCategoryItemMinSize;
    private int mCategoryItemSpacing;

    private final ArrayList<File> mPickedData = new ArrayList<>();

    public static List<File> getResult(Intent intent) {
        return (List<File>) intent.getSerializableExtra(EXTRA_RESULT);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        if (!ActivityUtils.hasArguments(this)) {
            throw new IllegalStateException("CategoryActivity must be instantiated via Builder");
        }

        Bundle extras = getIntent().getExtras();
        int accentColor = extras.getInt(EXTRA_ACCENT_COLOR);
        mAccentColor = accentColor == -1 ? ContextCompat.getColor(this, R.color.colorAccent) : accentColor;

        mCategoryItemMinSize = getResources().getDimensionPixelSize(R.dimen.item_category_min_size);
        mCategoryItemSpacing = getResources().getDimensionPixelSize(R.dimen.spacing_default);

        mImageDataProvider = new MediaStoreImagePickerDataProvider(this);
        mVideoDataProvider = new MediaStoreVideoPickerDataProvider.Builder(this)
                .setExtensions(".mp4")
                .build();

        mDataProvider = mImageDataProvider;

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        mTitle = (TextView) findViewById(R.id.title);
        mTitle.setOnClickListener(this::showCategoryPopup);

        mPickerPanel = (PickerPanelView) findViewById(R.id.picker_panel);
        mPickerPanel.setOnSubmitClickListener(this);
        mPickerPanel.setOnCancelClickListener(this);
        mPickerPanel.setOnCounterClickListener(this);
        mPickerPanel.setAccentColor(mAccentColor);

        mProgress = (CircularProgressView) findViewById(R.id.progress);
        mProgress.setColor(mAccentColor);

        mCategoryRecycler = (RecyclerView) findViewById(R.id.category_recycler);
        mCategoryRecycler.post(() -> {
            int spanCount = RecyclerUtils.calculateSpanCount(mCategoryRecycler, mCategoryItemMinSize);
            int itemSize = RecyclerUtils.calculateItemSize(mCategoryRecycler, spanCount, mCategoryItemMinSize, mCategoryItemSpacing);
            mCategoryLayoutManager = new GridLayoutManager(this, spanCount);
            mCategoryRecycler.addItemDecoration(new GridSpacingItemDecoration(spanCount, mCategoryItemSpacing, true));
            mCategoryAdapter = new CategoryAdapter(new CategoryPreviewFetcher(this));
            mCategoryAdapter.setCategoryItemSize(itemSize);
            mCategoryAdapter.setOnCategoryClickListener(this);
            mCategoryRecycler.setAdapter(mCategoryAdapter);
            mCategoryRecycler.setLayoutManager(mCategoryLayoutManager);

            requestData();
        });
    }

    private void requestData() {
        mCategoryAdapter.clearCategories();
        mProgress.startAnimation();
        mCategoryRecycler.setVisibility(View.INVISIBLE);
        mProgress.setVisibility(View.VISIBLE);

        mDataProvider.request(new PickerDataProvider.Callback<File>() {
            @Override
            public void onNext(Collection<File> data) {
                List<CategoryData> categories = Stream.of(data)
                        .chunkBy(File::getParent)
                        .map(files -> new CategoryData(files.get(0).getParentFile(), files, files.size()))
                        .toList();

                mCategoryRecycler.post(() -> mCategoryAdapter.appendCategories(categories));
            }

            @Override
            public void onComplete() {
                mProgress.post(() -> {
                    mProgress.stopAnimation();
                    mProgress.setVisibility(View.GONE);
                    mCategoryRecycler.setVisibility(View.VISIBLE);
                    mPickerPanel.setVisibility(View.VISIBLE);
                });
            }

            @Override
            public void onError(Throwable throwable) {

            }
        });
    }

    @Override
    public void onCategoryClicked(CategoryData category) {
        List<File> pickedPart = Stream.of(mPickedData)
                .filter(f -> category.getData().contains(f))
                .toList();

        new PickerActivity.Builder(this)
                .setRequestCode(IMAGE_PICKER_ACTIVITY_REQUEST_CODE)
                .setName(category.getName())
                .setAccentColor(mAccentColor)
                .setData(category.getData())
                .setPicked(pickedPart)
                .setTotalPicked(mPickedData.size())
                .start();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                cancel();
                break;
            }
        }
        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }

        if (requestCode == IMAGE_PICKER_ACTIVITY_REQUEST_CODE) {
            PickerActivity.Result result = PickerActivity.getResult(data);
            if (result.isCanceled()) {
                finish();
                return;
            }

            mPickedData.removeAll(result.getUnchecked());
            mPickedData.addAll(result.getChecked());

            if (result.isSubmitted()) {
                submit();
            } else {
                mPickerPanel.setPickedCount(mPickedData.size());
            }
        }
    }

    private void showCategoryPopup(View anchor) {
        ContextThemeWrapper wrapper = new ContextThemeWrapper(this, R.style.PopupMenuOverlapAnchor);
        PopupMenu popupMenu = new PopupMenu(wrapper, anchor, Gravity.TOP);
        popupMenu.getMenu().add(0, 0, 0, R.string.label_photos);
        popupMenu.getMenu().add(0, 1, 0, R.string.label_videos);
        popupMenu.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == 0) {
                mTitle.setText(R.string.label_photos);
                mDataProvider = mImageDataProvider;
            } else {
                mTitle.setText(R.string.label_videos);
                mDataProvider = mVideoDataProvider;
            }
            requestData();
            return true;
        });
        popupMenu.show();
    }

    @Override
    public void onSubmitClicked() {
        submit();
    }

    @Override
    public void onCancelClicked() {
        cancel();
    }

    @Override
    public void onCounterClicked() {
        new PickerActivity.Builder(this)
                .setRequestCode(IMAGE_PICKER_ACTIVITY_REQUEST_CODE)
                .setName(getString(R.string.label_picked))
                .setAccentColor(mAccentColor)
                .setData(mPickedData)
                .setPicked(mPickedData)
                .setTotalPicked(mPickedData.size())
                .start();
    }

    private void submit() {
        Intent intent = new Intent();
        intent.putExtra(EXTRA_RESULT, new ArrayList<>(mPickedData));
        setResult(RESULT_OK, intent);
        finish();
    }

    private void cancel() {
        setResult(RESULT_CANCELED);
        finish();
    }

    public static final class Builder {

        private final Activity mActivity;
        private int mRequestCode = -1;
        private int mAccentColor = -1;

        public Builder(Activity activity) {
            mActivity = activity;
        }

        public Builder setRequestCode(int requestCode) {
            mRequestCode = requestCode;
            return this;
        }

        public Builder setAccentColor(@ColorInt int color) {
            mAccentColor = color;
            return this;
        }

        public void start() {
            Intent intent = new Intent(mActivity, CategoryActivity.class);
            intent.putExtra(EXTRA_ACCENT_COLOR, mAccentColor);

            int requestCode = mRequestCode == -1 ? RequestCodeGenerator.generate() : mRequestCode;
            mActivity.startActivityForResult(intent, requestCode);
        }
    }
}
