package com.dewarder.pickerkit.panel;

import android.support.annotation.IdRes;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dewarder.pickerkit.R;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class PickerPanelCategoryAdapter extends RecyclerView.Adapter<PickerPanelCategoryViewHolder> {

    private final ArrayList<PickerCategory> mCategories = new ArrayList<>();
    private OnAttachmentPanelCategoryClickListener mOnAttachmentPanelCategoryClickListener;

    @Override
    public PickerPanelCategoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_attachment_category, parent, false);
        return new PickerPanelCategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PickerPanelCategoryViewHolder holder, int position) {
        PickerCategory item = mCategories.get(position);
        holder.itemView.setId(item.getId());
        holder.setName(item.getName());
        holder.setCircleIcon(item.getIcon());
        holder.setCircleBackground(new CircleDrawable(item.getColor()));
        holder.itemView.setOnClickListener(v -> notifyCategoryClicked(item));
    }

    @Override
    public int getItemCount() {
        return mCategories.size();
    }

    public void setCategories(List<PickerCategory> categories) {
        mCategories.clear();
        mCategories.addAll(categories);
        notifyDataSetChanged();
    }

    public void add(PickerCategory category) {
        mCategories.add(category);
        notifyDataSetChanged();
    }

    public void addAll(Collection<PickerCategory> categories) {
        mCategories.addAll(categories);
        notifyDataSetChanged();
    }

    public void replace(@IdRes int oldCategoryId, PickerCategory newCategory) {
        int index = findCategoryById(oldCategoryId);
        mCategories.set(index, newCategory);
        notifyItemChanged(index);
    }

    public void remove(@IdRes int categoryId) {
        int index = findCategoryById(categoryId);
        mCategories.remove(index);
        notifyItemRemoved(index);
    }

    private int findCategoryById(@IdRes int id) {
        int index = -1;
        for (int i = 0; i < mCategories.size(); i++) {
            if (mCategories.get(i).getId() == id) {
                index = i;
                break;
            }
        }

        if (index == -1) {
            throw new IllegalStateException("Category with id " + id + " isn't found");
        } else {
            return index;
        }
    }

    public void setOnAttachmentPanelCategoryClickListener(OnAttachmentPanelCategoryClickListener listener) {
        mOnAttachmentPanelCategoryClickListener = listener;
    }

    private void notifyCategoryClicked(PickerCategory item) {
        if (mOnAttachmentPanelCategoryClickListener != null) {
            mOnAttachmentPanelCategoryClickListener.onPanelPickerClicked(item.getId());
        }
    }
}
