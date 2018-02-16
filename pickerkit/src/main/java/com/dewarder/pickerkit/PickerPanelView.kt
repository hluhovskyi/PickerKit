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

    private lateinit var cancel: TextView
    private lateinit var submitContainer: LinearLayout
    private lateinit var counter: TextView
    private lateinit var submitLabel: TextView

    private var onCancelClickListener: OnCancelClickListener? = null
    private var onSubmitClickListener: OnSubmitClickListener? = null
    private var onCounterClickListener: OnCounterClickListener? = null

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

    constructor(
            context: Context,
            attrs: AttributeSet?
    ) : super(context, attrs) {
        init()
    }

    constructor(
            context: Context,
            attrs: AttributeSet?,
            @AttrRes defStyleAttr: Int
    ) : super(context, attrs, defStyleAttr) {
        init()
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(
            context: Context,
            attrs: AttributeSet?,
            @AttrRes defStyleAttr: Int,
            @StyleRes defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes) {
        init()
    }

    private fun init() {
        View.inflate(context, R.layout.content_picker_panel, this)

        setBackgroundColor(Color.parseColor("#1a1a1a"))
        setPadding(48, 0, 48, 0)

        cancel = findViewById(R.id.picker_panel_cancel)
        cancel.setOnClickListener { notifyOnCancelClicked() }

        submitContainer = findViewById(R.id.picker_panel_submit_container)

        counter = findViewById(R.id.picker_panel_counter)
        counter.setOnClickListener { notifyOnCounterClicked() }

        submitLabel = findViewById(R.id.picker_panel_submit_label)
        submitLabel.setOnClickListener { notifyOnSubmitClicked() }

        setPickedCount(0)
    }

    fun setAccentColor(@ColorInt color: Int) {
        val drawable = counter.background as GradientDrawable
        drawable.setColor(color)
    }

    fun setPickedCount(count: Int) {
        if (count > 0) {
            counter.text = count.toString()
            counter.visibility = View.VISIBLE
            submitLabel.setTextColor(Color.WHITE)
        } else {
            counter.visibility = View.GONE
            submitLabel.setTextColor(Color.parseColor("#838383"))
        }
    }

    fun setOnSubmitClickListener(listener: OnSubmitClickListener) {
        onSubmitClickListener = listener
    }

    fun setOnCancelClickListener(listener: OnCancelClickListener) {
        onCancelClickListener = listener
    }

    fun setOnCounterClickListener(listener: OnCounterClickListener) {
        onCounterClickListener = listener
    }

    private fun notifyOnCancelClicked() {
        onCancelClickListener?.onCancelClicked()
    }

    private fun notifyOnCounterClicked() {
        onCounterClickListener?.onCounterClicked()
    }

    private fun notifyOnSubmitClicked() {
        onSubmitClickListener?.onSubmitClicked()
    }
}
