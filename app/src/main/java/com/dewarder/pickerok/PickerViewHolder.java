package com.dewarder.pickerok;

import android.support.annotation.NonNull;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Checkable;
import android.widget.ImageView;

public class PickerViewHolder extends ItemPickerViewHolder {

    private final ImageView mPreview;
    private final CheckBox mCheckBox;

    public PickerViewHolder(View view) {
        super(view);
        mPreview = (ImageView) view.findViewById(R.id.item_picker_image_preview);
        mCheckBox = (CheckBox) view.findViewById(R.id.item_picker_check_box);
    }

    @Override
    public boolean hasPreview() {
        return true;
    }

    @Override
    public ImageView getPreviewTarget() {
        return mPreview;
    }

    @Override
    @NonNull
    public CheckBox getCheckBox() {
        return mCheckBox;
    }
}
