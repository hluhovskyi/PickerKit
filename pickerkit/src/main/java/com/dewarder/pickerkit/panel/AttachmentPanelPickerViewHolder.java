package com.dewarder.pickerkit.panel;

import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.dewarder.pickerkit.R;

public class AttachmentPanelPickerViewHolder extends RecyclerView.ViewHolder {

    private final ImageView mCircle;
    private final TextView mName;

    public AttachmentPanelPickerViewHolder(View view) {
        super(view);
        mCircle = (ImageView) view.findViewById(R.id.item_attachment_category_circle);
        mName = (TextView) view.findViewById(R.id.item_attachment_category_name);
    }

    public void setCircleIcon(@DrawableRes int iconRes) {
        mCircle.setImageResource(iconRes);
    }

    public void setCircleBackground(Drawable drawable) {
        ViewCompat.setBackground(mCircle, drawable);
    }

    public void setName(String name) {
        mName.setText(name);
    }
}
