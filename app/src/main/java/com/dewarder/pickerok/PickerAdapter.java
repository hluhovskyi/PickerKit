package com.dewarder.pickerok;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

public class PickerAdapter<T extends PickerItem> extends RecyclerView.Adapter<ItemPickerViewHolder> {

    interface Controller<T> {

        boolean isPicked(T item);

        void onPick(T item);

        void onUnpick(T item);
    }

    private int mItemSize = WRAP_CONTENT;
    private PreviewFetcher.Params mPreviewParams = PreviewFetcher.Params.empty();

    private final Controller<T> mController;
    private final PreviewFetcher<T> mPreviewFetcher;
    private final ArrayList<T> mItems = new ArrayList<>();

    public PickerAdapter(Controller<T> controller, PreviewFetcher<T> previewFetcher) {
        mController = controller;
        mPreviewFetcher = previewFetcher;
    }

    @Override
    public ItemPickerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_picker_image, parent, false);
        ViewGroup.LayoutParams params = view.getLayoutParams();
        params.width = mItemSize;
        params.height = mItemSize;
        return new PickerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ItemPickerViewHolder holder, int position) {
        T item = mItems.get(position);
        holder.getCheckBox().setChecked(mController.isPicked(item));
        holder.getPreviewTarget().setOnClickListener(v -> {
            if (mController.isPicked(item)) {
                mController.onUnpick(item);
                holder.getCheckBox().setChecked(false);
            } else {
                mController.onPick(item);
                holder.getCheckBox().setChecked(true);
            }
        });
        if (holder.hasPreview()) {
            mPreviewFetcher.fetchPreview(item, mPreviewParams, holder.getPreviewTarget());
        }
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public void setCategoryItemSize(int size) {
        mItemSize = size;
        mPreviewParams = PreviewFetcher.Params.of(size, size);
        notifyDataSetChanged();
    }

    public void setData(List<T> data) {
        mItems.clear();
        mItems.addAll(data);
        notifyDataSetChanged();
    }
}
