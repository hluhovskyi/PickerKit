package com.dewarder.pickerkit

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.TextView

class PickerFolderViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    val previewTarget: ImageView = view.findViewById(R.id.item_category_preview)
    private val name: TextView = view.findViewById(R.id.item_category_name)
    private val itemCount: TextView = view.findViewById(R.id.item_category_count)

    fun setName(name: String) {
        this.name.text = name
    }

    fun setItemCount(count: Int) {
        itemCount.text = count.toString()
    }
}
