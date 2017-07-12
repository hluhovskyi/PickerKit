package com.dewarder.pickerkit;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Collection;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

public class CategoryAdapter<T extends CategoryData<?, ?>> extends RecyclerView.Adapter<CategoryViewHolder> {

    private final PreviewFetcher<T> mPreviewFetcher;
    private final ArrayList<T> mCategories = new ArrayList<>();

    private int mCategoryItemSize = WRAP_CONTENT;
    private PreviewFetcher.Params mPreviewParams = PreviewFetcher.Params.empty();
    private OnCategoryClickListener<T> mOnCategoryClickListener;

    public CategoryAdapter(PreviewFetcher<T> previewFetcher) {
        mPreviewFetcher = previewFetcher;
    }

    @Override
    public CategoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_category, parent, false);
        ViewGroup.LayoutParams params = view.getLayoutParams();
        params.height = mCategoryItemSize;
        params.width = mCategoryItemSize;
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CategoryViewHolder holder, int position) {
        T data = mCategories.get(position);
        holder.setName(data.getName());
        holder.setItemCount(data.getItemCount());
        holder.itemView.setOnClickListener(v -> notifyCategoryClicked(data));
        mPreviewFetcher.fetchPreview(data, mPreviewParams, holder.getPreviewTarget());
    }

    @Override
    public int getItemCount() {
        return mCategories.size();
    }

    public void setCategoryItemSize(int size) {
        mCategoryItemSize = size;
        mPreviewParams = PreviewFetcher.Params.of(size, size);
        notifyDataSetChanged();
    }

    public void clearCategories() {
        mCategories.clear();
        notifyDataSetChanged();
    }

    public void appendCategories(Collection<T> categories) {
        int previousSize = mCategories.size();
        mCategories.addAll(categories);
        notifyItemRangeInserted(previousSize, categories.size());
    }

    public void setOnCategoryClickListener(OnCategoryClickListener<T> listener) {
        mOnCategoryClickListener = listener;
    }

    private void notifyCategoryClicked(T category) {
        if (mOnCategoryClickListener != null) {
            mOnCategoryClickListener.onCategoryClicked(category);
        }
    }
}
