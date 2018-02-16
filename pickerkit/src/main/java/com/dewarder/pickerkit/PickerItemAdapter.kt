package com.dewarder.pickerkit

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import java.util.*

class PickerItemAdapter<T> private constructor(
        private val dataController: DataController<T>,
        private val accessibilityController: AccessibilityController<T>?,
        private val previewFetcher: PreviewFetcher<T>
) : RecyclerView.Adapter<ItemPickerViewHolder>() {

    private var itemSize = WRAP_CONTENT
    private var previewParams: PreviewFetcher.Params = PreviewFetcher.Params.empty()
    private var pickEnabled = true

    private val items = ArrayList<T>()

    private var onPickerItemClickListener: OnPickerItemClickListener<T>? = null
    private var onPickerItemCheckListener: OnPickerItemCheckListener<T>? = null

    interface DataController<T> {

        fun getPicked(): List<T>

        fun isPicked(item: T): Boolean

        fun onPick(item: T)

        fun onUnpick(item: T)

        fun clearPicked()
    }

    interface AccessibilityController<T> {

        fun canPickMore(items: Collection<T>): Boolean
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemPickerViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_picker_image, parent, false)

        view.layoutParams.apply {
            width = itemSize
            height = itemSize
        }

        return PickerViewHolder(view)
    }

    override fun onBindViewHolder(holder: ItemPickerViewHolder, position: Int) {
        val item = items[position]
        //Needed to remove previous listeners that will unpick previous items
        holder.checkBox.setOnCheckedChangeListener(null)

        val isPicked = dataController.isPicked(item)
        holder.checkBox.isChecked = isPicked
        holder.checkBox.visibility = if (!isPicked && !pickEnabled) View.GONE else View.VISIBLE

        if (holder.hasPreview()) {
            previewFetcher.fetchPreview(item, previewParams, holder.previewTarget)
        }

        holder.previewTarget.setOnClickListener {
            onPickerItemClickListener?.onPickerItemCLicked(item)
        }

        holder.checkBox.setOnCheckedChangeListener { v, isChecked ->
            if (!isChecked) {
                dataController.onUnpick(item)
            } else {
                dataController.onPick(item)
            }

            invalidatePickEnabled()

            onPickerItemCheckListener?.onPickerItemChecked(item, isChecked)
        }
    }

    override fun getItemCount(): Int = items.size

    fun setCategoryItemSize(size: Int) {
        itemSize = size
        previewParams = PreviewFetcher.Params.of(size, size)
        notifyDataSetChanged()
    }

    fun setData(data: List<T>) {
        items.clear()
        items += data
        notifyDataSetChanged()
    }

    fun setOnPickerItemClickListener(listener: OnPickerItemClickListener<T>?) {
        onPickerItemClickListener = listener
    }

    fun setOnPickerItemCheckListener(listener: OnPickerItemCheckListener<T>?) {
        onPickerItemCheckListener = listener
    }

    private fun invalidatePickEnabled() {
        if (accessibilityController != null) {
            val enabled = accessibilityController.canPickMore(dataController.getPicked())
            setPickEnabled(enabled)
        }
    }

    fun setPickEnabled(pickEnabled: Boolean) {
        if (this.pickEnabled != pickEnabled) {
            this.pickEnabled = pickEnabled
            notifyDataSetChanged()
        }
    }

    class Builder<T> {

        private var dataController: DataController<T>? = null
        private var accessibilityController: AccessibilityController<T>? = null
        private var previewFetcher: PreviewFetcher<T>? = null
        private var onPickerItemClickListener: OnPickerItemClickListener<T>? = null
        private var onPickerItemCheckListener: OnPickerItemCheckListener<T>? = null
        private var previewParams: PreviewFetcher.Params = PreviewFetcher.Params.empty()
        private var pickEnabled = true
        private var data: Collection<T> = emptyList()

        fun setDataController(controller: DataController<T>?): Builder<T> = apply {
            dataController = controller
        }

        fun setAccessibilityController(controller: AccessibilityController<T>?): Builder<T> = apply {
            accessibilityController = controller
        }

        fun setPreviewFetcher(previewFetcher: PreviewFetcher<T>): Builder<T> = apply {
            this.previewFetcher = previewFetcher
        }

        fun setOnPickerItemClickListener(listener: OnPickerItemClickListener<T>?): Builder<T> = apply {
            onPickerItemClickListener = listener
        }

        fun setOnPickerItemCheckListener(listener: OnPickerItemCheckListener<T>?): Builder<T> = apply {
            onPickerItemCheckListener = listener
        }

        fun setPreviewParams(params: PreviewFetcher.Params): Builder<T> = apply {
            previewParams = params
        }

        fun setPickEnabled(enabled: Boolean): Builder<T> = apply {
            pickEnabled = enabled
        }

        fun setData(data: Collection<T>): Builder<T> = apply {
            this.data = data
        }

        fun build(): PickerItemAdapter<T> {
            val previewFetcher = previewFetcher ?: throw IllegalStateException(
                    "PreviewFetcher can't be null. Set it via `setPreviewFetcher` call.")

            val dataController = dataController ?: SimpleDataController()

            val adapter = PickerItemAdapter(
                    dataController = dataController,
                    accessibilityController = accessibilityController,
                    previewFetcher = previewFetcher
            )

            adapter.items += data
            adapter.pickEnabled = pickEnabled
            adapter.previewParams = previewParams
            adapter.onPickerItemClickListener = onPickerItemClickListener
            adapter.onPickerItemCheckListener = onPickerItemCheckListener

            return adapter
        }
    }
}
