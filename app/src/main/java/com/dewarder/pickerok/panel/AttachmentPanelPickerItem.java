package com.dewarder.pickerok.panel;

import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.IdRes;

public final class AttachmentPanelPickerItem {

    private final int mId;
    private final String mName;
    private final int mColor;
    private final int mIcon;

    private AttachmentPanelPickerItem(@IdRes int id, String name, @ColorInt int color, @DrawableRes int icon) {
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

    public static AttachmentPanelPickerItem of(@IdRes int id, String name, @ColorInt int color, @DrawableRes int icon) {
        return new AttachmentPanelPickerItem(id, name, color, icon);
    }
}
