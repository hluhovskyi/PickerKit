package com.dewarder.pickerkit.panel;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.dewarder.pickerkit.OnPickerItemCheckListener;
import com.dewarder.pickerkit.OnPickerItemClickListener;
import com.dewarder.pickerkit.PickerItemAdapter;
import com.dewarder.pickerkit.PickerPreviewFetcher;
import com.dewarder.pickerkit.R;
import com.dewarder.pickerkit.SimpleDataController;
import com.dewarder.pickerkit.SpaceItemDecoration;
import com.dewarder.pickerkit.model.PickerMedia;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PickerPanelView extends LinearLayout {

    private PickerItemAdapter.DataController<PickerMedia> mDataController;

    private RecyclerView mPickerRecycler;
    private LinearLayoutManager mPickerLayoutManager;
    private PickerItemAdapter<PickerMedia> mPickerAdapter;

    private RecyclerView mCategoryRecycler;
    private GridLayoutManager mCategoryLayoutManager;
    private PickerPanelCategoryAdapter mCategoryAdapter;

    private int mPickerSpacing;
    private int mCategoryMinWidth;

    private int mCategorySpanCount = -1;
    private int mLimit;
    private boolean mInitialized;

    public PickerPanelView(Context context) {
        super(context);
        init(context);
    }

    public PickerPanelView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public PickerPanelView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public PickerPanelView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context) {
        View view = inflate(context, R.layout.content_attachment_panel, this);
        setBackgroundColor(Color.WHITE);
        setOrientation(VERTICAL);

        mCategoryMinWidth = getResources().getDimensionPixelSize(R.dimen.item_attachment_panel_category_min_width);
        mPickerSpacing = getResources().getDimensionPixelSize(R.dimen.attachment_panel_picker_spacing);

        mDataController = new SimpleDataController<>();

        mPickerRecycler = (RecyclerView) view.findViewById(R.id.attachment_panel_picker_recycler);
        mPickerLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        mPickerRecycler.setLayoutManager(mPickerLayoutManager);
        mPickerAdapter = new PickerItemAdapter.Builder<PickerMedia>()
                .setPreviewFetcher(new PickerPreviewFetcher(context))
                .setDataController(mDataController)
                .build();

        mPickerRecycler.setAdapter(mPickerAdapter);
        mPickerRecycler.addItemDecoration(SpaceItemDecoration.horizontalAll(mPickerSpacing));

        mCategoryRecycler = (RecyclerView) view.findViewById(R.id.attachment_panel_category_recycler);
        mCategoryRecycler.setItemAnimator(null);
        mCategoryAdapter = new PickerPanelCategoryAdapter();
        mCategoryRecycler.setAdapter(mCategoryAdapter);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if (!mInitialized) {
            int spanCount = mCategorySpanCount != -1 ? mCategorySpanCount : getWidth() / mCategoryMinWidth;
            mCategoryLayoutManager = new GridLayoutManager(getContext(), spanCount);
            mCategoryRecycler.setLayoutManager(mCategoryLayoutManager);
            mPickerAdapter.setCategoryItemSize(mPickerRecycler.getHeight() - mPickerSpacing * 2);
            mInitialized = true;
        }
    }

    public void setCategories(List<PickerCategory> categories) {
        mCategoryAdapter.setCategories(categories);
    }

    public void addCategory(@NonNull PickerCategory category) {
        mCategoryAdapter.add(category);
    }

    public void addCategories(@NonNull List<PickerCategory> categories) {
        mCategoryAdapter.addAll(new ArrayList<>(categories));
    }

    public void addCategories(@NonNull PickerCategory... categories) {
        mCategoryAdapter.addAll(Arrays.asList(categories));
    }

    public void replaceCategory(@IdRes int oldCategoryId, @NonNull PickerCategory newCategory) {
        mCategoryAdapter.replace(oldCategoryId, newCategory);
    }

    public void removeCategory(@IdRes int categoryId) {
        mCategoryAdapter.remove(categoryId);
    }

    public void setData(@NonNull List<PickerMedia> data) {
        mPickerAdapter.setData(data);
    }

    public int getCategorySpanCount() {
        return mCategorySpanCount;
    }

    public void setCategorySpanCount(int spanCount) {
        mCategorySpanCount = spanCount;
    }

    public void clearPicked() {
        mDataController.clearPicked();
    }

    public List<PickerMedia> getPicked() {
        return new ArrayList<>(mDataController.getPicked());
    }

    public void setOnAttachmentPanelCategoryClickListener(@Nullable OnPickerPanelCategoryClickListener listener) {
        mCategoryAdapter.setOnAttachmentPanelCategoryClickListener(listener);
    }

    public void setOnPickerItemClickListener(OnPickerItemClickListener<PickerMedia> listener) {
        mPickerAdapter.setOnPickerItemClickListener(listener);
    }

    public void setOnPickerItemCheckListener(OnPickerItemCheckListener<PickerMedia> listener) {
        mPickerAdapter.setOnPickerItemCheckListener(listener);
    }

    public void setLimit(int limit) {
        mLimit = limit;
        mPickerAdapter.setPickEnabled(mLimit > 0 && mLimit - mDataController.getPicked().size() > 0);
    }

    public int getLimit() {
        return mLimit;
    }

    public void setPickEnabled(boolean enabled) {
        mPickerAdapter.setPickEnabled(enabled);
    }
}
