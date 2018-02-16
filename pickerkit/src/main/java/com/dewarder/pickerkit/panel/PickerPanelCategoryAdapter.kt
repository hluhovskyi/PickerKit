package com.dewarder.pickerkit.panel

import android.support.annotation.IdRes
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.dewarder.pickerkit.R

import java.util.ArrayList

class PickerPanelCategoryAdapter : RecyclerView.Adapter<PickerPanelCategoryViewHolder>() {

    private val mCategories = ArrayList<PickerCategory>()
    private var mOnPickerPanelCategoryClickListener: OnPickerPanelCategoryClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PickerPanelCategoryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_attachment_category, parent, false)
        return PickerPanelCategoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: PickerPanelCategoryViewHolder, position: Int) {
        val item = mCategories[position]
        holder.itemView.id = item.id
        holder.setName(item.name)
        holder.setCircleIcon(item.icon)
        holder.setCircleBackground(CircleDrawable(item.color))
        holder.itemView.setOnClickListener { v -> notifyCategoryClicked(item) }
    }

    override fun getItemCount(): Int {
        return mCategories.size
    }

    fun setCategories(categories: List<PickerCategory>) {
        mCategories.clear()
        mCategories.addAll(categories)
        notifyDataSetChanged()
    }

    fun add(category: PickerCategory) {
        mCategories.add(category)
        notifyDataSetChanged()
    }

    fun addAll(categories: Collection<PickerCategory>) {
        mCategories.addAll(categories)
        notifyDataSetChanged()
    }

    fun replace(@IdRes oldCategoryId: Int, newCategory: PickerCategory) {
        val index = findCategoryById(oldCategoryId)
        mCategories[index] = newCategory
        notifyItemChanged(index)
    }

    fun remove(@IdRes categoryId: Int) {
        val index = findCategoryById(categoryId)
        mCategories.removeAt(index)
        notifyItemRemoved(index)
    }

    private fun findCategoryById(@IdRes id: Int): Int {
        var index = -1
        for (i in mCategories.indices) {
            if (mCategories[i].id == id) {
                index = i
                break
            }
        }

        return if (index == -1) {
            throw IllegalStateException("Category with id $id isn't found")
        } else {
            index
        }
    }

    fun setOnAttachmentPanelCategoryClickListener(listener: OnPickerPanelCategoryClickListener) {
        mOnPickerPanelCategoryClickListener = listener
    }

    private fun notifyCategoryClicked(item: PickerCategory) {
        if (mOnPickerPanelCategoryClickListener != null) {
            mOnPickerPanelCategoryClickListener!!.onPickerCategoryClicked(item.id)
        }
    }
}
