package com.dewarder.pickerkit;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

public class PickerAdapter<T extends PickerItem> extends RecyclerView.Adapter<ItemPickerViewHolder> {

    public interface Controller<T> {

        List<T> getPicked();

        boolean isPicked(T item);

        void onPick(T item);

        void onUnpick(T item);

        void clearPicked();
    }

    private int mItemSize = WRAP_CONTENT;
    private PreviewFetcher.Params mPreviewParams = PreviewFetcher.Params.empty();

    private Controller<T> mController;
    private boolean mPickEnabled = true;
    private final PreviewFetcher<T> mPreviewFetcher;
    private final ArrayList<T> mItems = new ArrayList<>();

    private OnPickerItemClickListener<T> mOnPickerItemClickListener;
    private OnPickerItemCheckListener<T> mOnPickerItemCheckListener;

    public PickerAdapter(PreviewFetcher<T> previewFetcher) {
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
        //Needed to remove previous listeners that will unpick previous items
        holder.getCheckBox().setOnCheckedChangeListener(null);

        boolean isPicked = mController.isPicked(item);
        holder.getCheckBox().setChecked(isPicked);
        if (!isPicked && !mPickEnabled) {
            holder.getCheckBox().setVisibility(View.GONE);
        } else {
            holder.getCheckBox().setVisibility(View.VISIBLE);
        }

        if (holder.hasPreview()) {
            mPreviewFetcher.fetchPreview(item, mPreviewParams, holder.getPreviewTarget());
        }
        holder.getPreviewTarget().setOnClickListener(v -> {
            if (mOnPickerItemClickListener != null) {
                mOnPickerItemClickListener.onPickerItemCLicked(item);
            }
        });
        holder.getCheckBox().setOnCheckedChangeListener((v, isChecked) -> {
            if (!isChecked) {
                mController.onUnpick(item);
            } else {
                mController.onPick(item);
            }
            if (mOnPickerItemCheckListener != null) {
                mOnPickerItemCheckListener.onPickerItemChecked(item, isChecked);
            }
        });
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

    public void setOnPickerItemClickListener(OnPickerItemClickListener<T> listener) {
        mOnPickerItemClickListener = listener;
    }

    public void setOnPickerItemCheckListener(OnPickerItemCheckListener<T> listener) {
        mOnPickerItemCheckListener = listener;
    }

    public void setPickerController(Controller<T> controller) {
        mController = controller;
    }

    public void setPickEnabled(boolean pickEnabled) {
        if (mPickEnabled != pickEnabled) {
            mPickEnabled = pickEnabled;
            notifyDataSetChanged();
        }
    }
}
