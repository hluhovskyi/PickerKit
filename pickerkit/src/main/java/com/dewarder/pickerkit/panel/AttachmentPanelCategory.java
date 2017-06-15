package com.dewarder.pickerkit.panel;

import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.IdRes;

public final class AttachmentPanelCategory {

    private final int mId;
    private final String mName;
    private final int mColor;
    private final int mIcon;

    private AttachmentPanelCategory(@IdRes int id, String name, @ColorInt int color, @DrawableRes int icon) {
        mId = id;
        mName = name;
        mColor = color;
        mIcon = icon;
    }

    @IdRes
    public int getId() {
        return mId;
    }

    public String getName() {
        return mName;
    }

    @ColorInt
    public int getColor() {
        return mColor;
    }

    @DrawableRes
    public int getIcon() {
        return mIcon;
    }

    public static AttachmentPanelCategory of(@IdRes int id, String name, @ColorInt int color, @DrawableRes int icon) {
        return new AttachmentPanelCategory(id, name, color, icon);
    }
}
