package com.dewarder.pickerok;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Checkable;
import android.widget.ImageView;

public abstract class ItemPickerViewHolder extends RecyclerView.ViewHolder {

    public ItemPickerViewHolder(View itemView) {
        super(itemView);
    }

    public abstract boolean hasPreview();

    public abstract ImageView getPreviewTarget();

    @NonNull
    public abstract Checkable getCheckBox();
}
