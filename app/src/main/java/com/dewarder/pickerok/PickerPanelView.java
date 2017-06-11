package com.dewarder.pickerok;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.AttrRes;
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

    public interface OnSubmitClickListener {

        void onSubmitClicked();
    }

    private TextView mCancel;
    private LinearLayout mSubmitContainer;
    private TextView mCounter;
    private TextView mSubmitLabel;

    private OnCancelClickListener mOnCancelClickListener;
    private OnSubmitClickListener mOnSubmitClickListener;

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
        mSubmitContainer.setOnClickListener(v -> notifyOnSubmitClicked());
        mCounter = (TextView) findViewById(R.id.picker_panel_counter);
        mSubmitLabel = (TextView) findViewById(R.id.picker_panel_submit_label);
        setPickedCount(0);
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

    public void setOnSubmitClickListener(OnSubmitClickListener onSubmitClickListener) {
        mOnSubmitClickListener = onSubmitClickListener;
    }

    public void setOnCancelClickListener(OnCancelClickListener onCancelClickListener) {
        mOnCancelClickListener = onCancelClickListener;
    }

    private void notifyOnCancelClicked() {
        if (mOnCancelClickListener != null) {
            mOnCancelClickListener.onCancelClicked();
        }
    }

    private void notifyOnSubmitClicked() {
        if (mOnSubmitClickListener != null) {
            mOnSubmitClickListener.onSubmitClicked();
        }
    }
}
