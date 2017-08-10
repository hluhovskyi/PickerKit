package com.dewarder.pickerkit.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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
import com.dewarder.pickerkit.PickerFolderAdapter;
import com.dewarder.pickerkit.PickerMediaFolderPreviewFetcher;
import com.dewarder.pickerkit.GridSpacingItemDecoration;
import com.dewarder.pickerkit.OnCategoryClickListener;
import com.dewarder.pickerkit.PickerPanelView;
import com.dewarder.pickerkit.R;
import com.dewarder.pickerkit.RequestCodeGenerator;
import com.dewarder.pickerkit.config.PickerConfig;
import com.dewarder.pickerkit.config.PickerDataConfig;
import com.dewarder.pickerkit.model.PickerImage;
import com.dewarder.pickerkit.model.PickerMedia;
import com.dewarder.pickerkit.model.PickerMediaFolder;
import com.dewarder.pickerkit.model.PickerVideo;
import com.dewarder.pickerkit.provider.MediaStoreImagePickerDataProvider;
import com.dewarder.pickerkit.provider.MediaStoreVideoPickerDataProvider;
import com.dewarder.pickerkit.provider.PickerDataProvider;
import com.dewarder.pickerkit.provider.PickerMediaProviderWrapper;
import com.dewarder.pickerkit.result.PickerGalleryResult;
import com.dewarder.pickerkit.utils.Activities;
import com.dewarder.pickerkit.utils.Colors;
import com.dewarder.pickerkit.utils.Recyclers;
import com.github.rahatarmanahmed.cpv.CircularProgressView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public final class PickerGalleryFolderActivity extends AppCompatActivity implements
        OnCategoryClickListener<PickerMediaFolder>,
        PickerPanelView.OnSubmitClickListener,
        PickerPanelView.OnCancelClickListener,
        PickerPanelView.OnCounterClickListener {

    private static final String EXTRA_RESULT = "EXTRA_RESULT";
    private static final String EXTRA_CONFIG = "EXTRA_CONFIG";
    private static final String EXTRA_DATA_CONFIG = "EXTRA_DATA_CONFIG";

    private static final int IMAGE_PICKER_ACTIVITY_REQUEST_CODE = 1;

    private PickerConfig config;
    private PickerDataConfig dataConfig;

    private PickerDataProvider<PickerImage> mImageDataProvider;
    private PickerDataProvider<PickerVideo> mVideoDataProvider;
    private PickerDataProvider<PickerMedia> mDataProvider;

    private Toolbar mToolbar;
    private TextView mTitle;
    private PickerPanelView mPickerPanel;
    private CircularProgressView mProgress;

    private RecyclerView mCategoryRecycler;
    private GridLayoutManager mCategoryLayoutManager;
    private PickerFolderAdapter mCategoryAdapter;

    private int mAccentColor;
    private int mCategoryItemMinSize;
    private int mCategoryItemSpacing;

    private final ArrayList<PickerMedia> selected = new ArrayList<>();

    public static PickerGalleryResult getGalleryResult(Intent intent) {
        return intent.getParcelableExtra(EXTRA_RESULT);
    }

    public static void open(@NonNull Activity activity) {
        open(activity,
                PickerConfig.defaultInstance(),
                PickerDataConfig.defaultInstance());
    }

    public static void open(@NonNull Activity activity,
                            @NonNull PickerConfig config,
                            @NonNull PickerDataConfig dataConfig) {

        Intent intent = new Intent(activity, PickerGalleryFolderActivity.class);
        intent.putExtra(EXTRA_CONFIG, config);
        intent.putExtra(EXTRA_DATA_CONFIG, dataConfig);
        activity.startActivity(intent);
    }

    public static int openForResult(@NonNull Activity activity) {
        return openForResult(activity,
                PickerConfig.defaultInstance());
    }

    public static int openForResult(@NonNull Activity activity,
                                    @NonNull PickerConfig config) {

        int requestCode = RequestCodeGenerator.generate();
        Intent intent = new Intent(activity, PickerGalleryFolderActivity.class);
        intent.putExtra(EXTRA_CONFIG, config);
        intent.putExtra(EXTRA_DATA_CONFIG, PickerDataConfig.defaultInstance());
        activity.startActivityForResult(intent, requestCode);
        return requestCode;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setTheme(R.style.PickerActivityTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        config = Activities.getParcelableArgument(this, EXTRA_CONFIG);
        dataConfig = Activities.getParcelableArgument(this, EXTRA_DATA_CONFIG);

        mAccentColor = Colors.orElseAccent(this, config.getAccentColor());

        mCategoryItemMinSize = getResources().getDimensionPixelSize(R.dimen.item_category_min_size);
        mCategoryItemSpacing = getResources().getDimensionPixelSize(R.dimen.spacing_default);

        mImageDataProvider = MediaStoreImagePickerDataProvider.of(this);
        mVideoDataProvider = new MediaStoreVideoPickerDataProvider.Builder(this)
                .setExtensions(".mp4")
                .build();

        mDataProvider = PickerMediaProviderWrapper.wrapImage(mImageDataProvider);

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
            int spanCount = Recyclers.calculateSpanCount(mCategoryRecycler, mCategoryItemMinSize);
            int itemSize = Recyclers.calculateItemSize(mCategoryRecycler, spanCount, mCategoryItemMinSize, mCategoryItemSpacing);

            mCategoryLayoutManager = new GridLayoutManager(this, spanCount);
            mCategoryRecycler.addItemDecoration(new GridSpacingItemDecoration(spanCount, mCategoryItemSpacing, true));
            mCategoryAdapter = new PickerFolderAdapter(new PickerMediaFolderPreviewFetcher(this));
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

        mDataProvider.request(new PickerDataProvider.Callback<PickerMedia>() {
            @Override
            public void onNext(Collection<PickerMedia> data) {
                List<PickerMediaFolder> categories = Stream.of(data)
                        .chunkBy(media -> media.getSource().getLastPathSegment())
                        .map(PickerMediaFolder::fromMedia)
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
    public void onCategoryClicked(PickerMediaFolder category) {
        List<PickerMedia> pickedPart = Stream.of(selected)
                .filter(f -> category.getChildren().contains(f))
                .toList();

        new PickerGalleryActivity.Starter(this)
                .setRequestCode(IMAGE_PICKER_ACTIVITY_REQUEST_CODE)
                .setTitle(category.getName())
                //  .setAccentColor(mAccentColor)
                .setData(category.getChildren())
                .setSelected(pickedPart)
                .setTotalSelected(selected.size())
                .setLimit(dataConfig.getGalleryLimit())
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
            PickerGalleryResult result = PickerGalleryActivity.getGalleryResult(data);
            if (result.isCanceled()) {
                finish();
                return;
            }

            selected.removeAll(result.getUnselected());
            selected.addAll(result.getSelected());

            if (result.isSubmitted()) {
                submit();
            } else {
                mPickerPanel.setPickedCount(selected.size());
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
                mDataProvider = PickerMediaProviderWrapper.wrapImage(mImageDataProvider);
            } else {
                mTitle.setText(R.string.label_videos);
                mDataProvider = PickerMediaProviderWrapper.wrapVideo(mVideoDataProvider);
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
        new PickerGalleryActivity.Starter(this)
                .setRequestCode(IMAGE_PICKER_ACTIVITY_REQUEST_CODE)
                .setTitle(getString(R.string.label_picked))
                //.setAccentColor(mAccentColor)
                .setData(selected)
                .setSelected(selected)
                .setTotalSelected(selected.size())
                .setLimit(dataConfig.getGalleryLimit())
                .start();
    }

    private void submit() {
        Intent intent = new Intent();
        //TODO: Unselected isn't handled
        PickerGalleryResult result = ResultGallery.submit(selected, Collections.emptyList());
        intent.putExtra(EXTRA_RESULT, result);
        setResult(RESULT_OK, intent);
        finish();
    }

    private void cancel() {
        setResult(RESULT_CANCELED);
        finish();
    }
}
