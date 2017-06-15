package com.dewarder.pickerkit;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.support.annotation.AttrRes;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;


public class PickerPanelView extends FrameLayout {

    public interface OnCancelClickListener {

        void onCancelClicked();
    }

    public interface OnCounterClickListener {

        void onCounterClicked();
    }

    public interface OnSubmitClickListener {

        void onSubmitClicked();
    }

    private TextView mCancel;
    private LinearLayout mSubmitContainer;
    private TextView mCounter;
    private TextView mSubmitLabel;

    private OnCancelClickListener mOnCancelClickListener;
    private OnSubmitClickListener mOnSubmitClickListener;
    private OnCounterClickListener mOnCounterClickListener;

    public PickerPanelView(@NonNull Context context) {
        super(context);
        init();
    }

    public PickerPanelView(@NonNull Context context,
                           @Nullable AttributeSet attrs) {

        super(context, attrs);
        init();
    }

    public PickerPanelView(@NonNull Context context,
                           @Nullable AttributeSet attrs,
                           @AttrRes int defStyleAttr) {

        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public PickerPanelView(@NonNull Context context,
                           @Nullable AttributeSet attrs,
                           @AttrRes int defStyleAttr,
                           @StyleRes int defStyleRes) {

        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        inflate(getContext(), R.layout.content_picker_panel, this);

        setBackgroundColor(Color.parseColor("#1a1a1a"));
        setPadding(48, 0, 48, 0);

        mCancel = (TextView) findViewById(R.id.picker_panel_cancel);
        mCancel.setOnClickListener(v -> notifyOnCancelClicked());
        mSubmitContainer = (LinearLayout) findViewById(R.id.picker_panel_submit_container);
        mCounter = (TextView) findViewById(R.id.picker_panel_counter);
        mCounter.setOnClickListener(v -> notifyOnCounterClicked());
        mSubmitLabel = (TextView) findViewById(R.id.picker_panel_submit_label);
        mSubmitLabel.setOnClickListener(v -> notifyOnSubmitClicked());
        setPickedCount(0);
    }

    public void setAccentColor(@ColorInt int color) {
        GradientDrawable drawable = (GradientDrawable) mCounter.getBackground();
        drawable.setColor(color);
    }

    public void setPickedCount(int count) {
        if (count > 0) {
            mCounter.setText(String.valueOf(count));
            mCounter.setVisibility(VISIBLE);
            mSubmitLabel.setTextColor(Color.WHITE);
        } else {
            mCounter.setVisibility(GONE);
            mSubmitLabel.setTextColor(Color.parseColor("#838383"));
        }
    }

    public void setOnSubmitClickListener(OnSubmitClickListener listener) {
        mOnSubmitClickListener = listener;
    }

    public void setOnCancelClickListener(OnCancelClickListener listener) {
        mOnCancelClickListener = listener;
    }

    public void setOnCounterClickListener(OnCounterClickListener listener) {
        mOnCounterClickListener = listener;
    }

    private void notifyOnCancelClicked() {
        if (mOnCancelClickListener != null) {
            mOnCancelClickListener.onCancelClicked();
        }
    }

    private void notifyOnCounterClicked() {
        if (mOnCounterClickListener != null) {
            mOnCounterClickListener.onCounterClicked();
        }
    }

    private void notifyOnSubmitClicked() {
        if (mOnSubmitClickListener != null) {
            mOnSubmitClickListener.onSubmitClicked();
        }
    }
}
