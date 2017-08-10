package com.dewarder.pickerkit;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class PickerFolderViewHolder extends RecyclerView.ViewHolder {

    private final ImageView mPreview;
    private final TextView mName;
    private final TextView mItemCount;

    public PickerFolderViewHolder(View view) {
        super(view);
        mPreview = (ImageView) view.findViewById(R.id.item_category_preview);
        mName = (TextView) view.findViewById(R.id.item_category_name);
        mItemCount = (TextView) view.findViewById(R.id.item_category_count);
    }

    public void setName(String name) {
        mName.setText(name);
    }

    public void setItemCount(int count) {
        mItemCount.setText(String.valueOf(count));
    }

    public ImageView getPreviewTarget() {
        return mPreview;
    }
}
