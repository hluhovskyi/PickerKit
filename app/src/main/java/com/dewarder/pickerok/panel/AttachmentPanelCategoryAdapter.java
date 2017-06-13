package com.dewarder.pickerok.panel;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dewarder.pickerok.R;

import java.util.ArrayList;

public class AttachmentPanelCategoryAdapter extends RecyclerView.Adapter<AttachmentPanelPickerViewHolder> {

    private final ArrayList<AttachmentPanelPickerItem> mItems = new ArrayList<>();

    @Override
    public AttachmentPanelPickerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_attachment_category, parent, false);
        return new AttachmentPanelPickerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AttachmentPanelPickerViewHolder holder, int position) {
        AttachmentPanelPickerItem item = mItems.get(position);
        holder.itemView.setId(item.getId());
        holder.setName(item.getName());
        holder.setCircleIcon(item.getIcon());
        holder.setCircleBackground(new CircleDrawable(item.getColor()));
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public void addItem(AttachmentPanelPickerItem item) {
        mItems.add(item);
        notifyDataSetChanged();
    }
}
