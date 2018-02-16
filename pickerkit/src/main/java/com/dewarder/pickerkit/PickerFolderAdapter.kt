package com.dewarder.pickerkit

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import com.dewarder.pickerkit.gallery.model.PickerMediaFolder
import java.util.*

class PickerFolderAdapter(
        private val previewFetcher: PreviewFetcher<PickerMediaFolder>
) : RecyclerView.Adapter<PickerFolderViewHolder>() {

    private val categories = ArrayList<PickerMediaFolder>()

    private var categoryItemSize = WRAP_CONTENT
    private var previewParams: PreviewFetcher.Params = PreviewFetcher.Params.empty()

    private var onCategoryClickListener: OnCategoryClickListener<PickerMediaFolder>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PickerFolderViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        val view = inflater.inflate(R.layout.item_category, parent, false)
        view.layoutParams.apply {
            height = categoryItemSize
            width = categoryItemSize
        }

        return PickerFolderViewHolder(view)
    }

    override fun onBindViewHolder(holder: PickerFolderViewHolder, position: Int) {
        val folder = categories[position]

        holder.apply {
            setName(folder.name)
            setItemCount(folder.itemCount)
            itemView.setOnClickListener { notifyCategoryClicked(folder) }
        }

        previewFetcher.fetchPreview(folder, previewParams, holder.previewTarget)
    }

    override fun getItemCount(): Int = categories.size

    fun setCategoryItemSize(size: Int) {
        categoryItemSize = size
        previewParams = PreviewFetcher.Params.of(size, size)
        notifyDataSetChanged()
    }

    fun clearCategories() {
        categories.clear()
        notifyDataSetChanged()
    }

    fun appendCategories(categories: Collection<PickerMediaFolder>) {
        val previousSize = this.categories.size

        this.categories += categories

        notifyItemRangeInserted(previousSize, categories.size)
    }

    fun setOnCategoryClickListener(listener: OnCategoryClickListener<PickerMediaFolder>) {
        onCategoryClickListener = listener
    }

    private fun notifyCategoryClicked(category: PickerMediaFolder) {
        onCategoryClickListener?.onCategoryClicked(category)
    }
}
