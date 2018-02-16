package com.dewarder.pickerkit

import android.annotation.TargetApi
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.support.annotation.AttrRes
import android.support.annotation.ColorInt
import android.support.annotation.StyleRes
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView


class PickerPanelView : FrameLayout {

    private var mCancel: TextView? = null
    private var mSubmitContainer: LinearLayout? = null
    private var mCounter: TextView? = null
    private var mSubmitLabel: TextView? = null

    private var mOnCancelClickListener: OnCancelClickListener? = null
    private var mOnSubmitClickListener: OnSubmitClickListener? = null
    private var mOnCounterClickListener: OnCounterClickListener? = null

    interface OnCancelClickListener {

        fun onCancelClicked()
    }

    interface OnCounterClickListener {

        fun onCounterClicked()
    }

    interface OnSubmitClickListener {

        fun onSubmitClicked()
    }

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context,
                attrs: AttributeSet?) : super(context, attrs) {
        init()
    }

    constructor(context: Context,
                attrs: AttributeSet?,
                @AttrRes defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context,
                attrs: AttributeSet?,
                @AttrRes defStyleAttr: Int,
                @StyleRes defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes) {
        init()
    }

    private fun init() {
        View.inflate(context, R.layout.content_picker_panel, this)

        setBackgroundColor(Color.parseColor("#1a1a1a"))
        setPadding(48, 0, 48, 0)

        mCancel = findViewById(R.id.picker_panel_cancel)
        mCancel!!.setOnClickListener { notifyOnCancelClicked() }
        mSubmitContainer = findViewById(R.id.picker_panel_submit_container)
        mCounter = findViewById(R.id.picker_panel_counter)
        mCounter!!.setOnClickListener { v -> notifyOnCounterClicked() }
        mSubmitLabel = findViewById(R.id.picker_panel_submit_label)
        mSubmitLabel!!.setOnClickListener { v -> notifyOnSubmitClicked() }
        setPickedCount(0)
    }

    fun setAccentColor(@ColorInt color: Int) {
        val drawable = mCounter!!.background as GradientDrawable
        drawable.setColor(color)
    }

    fun setPickedCount(count: Int) {
        if (count > 0) {
            mCounter!!.text = count.toString()
            mCounter!!.visibility = View.VISIBLE
            mSubmitLabel!!.setTextColor(Color.WHITE)
        } else {
            mCounter!!.visibility = View.GONE
            mSubmitLabel!!.setTextColor(Color.parseColor("#838383"))
        }
    }

    fun setOnSubmitClickListener(listener: OnSubmitClickListener) {
        mOnSubmitClickListener = listener
    }

    fun setOnCancelClickListener(listener: OnCancelClickListener) {
        mOnCancelClickListener = listener
    }

    fun setOnCounterClickListener(listener: OnCounterClickListener) {
        mOnCounterClickListener = listener
    }

    private fun notifyOnCancelClicked() {
        if (mOnCancelClickListener != null) {
            mOnCancelClickListener!!.onCancelClicked()
        }
    }

    private fun notifyOnCounterClicked() {
        if (mOnCounterClickListener != null) {
            mOnCounterClickListener!!.onCounterClicked()
        }
    }

    private fun notifyOnSubmitClicked() {
        if (mOnSubmitClickListener != null) {
            mOnSubmitClickListener!!.onSubmitClicked()
        }
    }
}
