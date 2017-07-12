package com.dewarder.pickerkit;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

public final class PickerAdapter<T extends PickerItem> extends RecyclerView.Adapter<ItemPickerViewHolder> {

    public interface DataController<T> {

        List<T> getPicked();

        boolean isPicked(T item);

        void onPick(T item);

        void onUnpick(T item);

        void clearPicked();
    }

    public interface PickerController<T> {

        boolean isPickEnabled(Collection<T> items);
    }

    private int mItemSize = WRAP_CONTENT;
    private PreviewFetcher.Params mPreviewParams = PreviewFetcher.Params.empty();
    private boolean mPickEnabled = true;

    private final DataController<T> mDataController;
    private final PickerController<T> mPickerController;
    private final PreviewFetcher<T> mPreviewFetcher;
    private final ArrayList<T> mItems = new ArrayList<>();

    private OnPickerItemClickListener<T> mOnPickerItemClickListener;
    private OnPickerItemCheckListener<T> mOnPickerItemCheckListener;

    private PickerAdapter(@NonNull DataController<T> dataController,
                          @Nullable PickerController<T> pickerController,
                          @NonNull PreviewFetcher<T> previewFetcher) {

        mDataController = dataController;
        mPickerController = pickerController;
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

        boolean isPicked = mDataController.isPicked(item);
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
                mDataController.onUnpick(item);
            } else {
                mDataController.onPick(item);
            }
            invalidatePickEnabled();
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

    public void setOnPickerItemClickListener(@Nullable OnPickerItemClickListener<T> listener) {
        mOnPickerItemClickListener = listener;
    }

    public void setOnPickerItemCheckListener(@Nullable OnPickerItemCheckListener<T> listener) {
        mOnPickerItemCheckListener = listener;
    }

    private void invalidatePickEnabled() {
        if (mPickerController != null) {
            boolean enabled = mPickerController.isPickEnabled(mDataController.getPicked());
            setPickEnabled(enabled);
        }
    }

    public void setPickEnabled(boolean pickEnabled) {
        if (mPickEnabled != pickEnabled) {
            mPickEnabled = pickEnabled;
            notifyDataSetChanged();
        }
    }

    public final static class Builder<T extends PickerItem> {

        private DataController<T> mDataController;
        private PickerController<T> mPickerController;
        private PreviewFetcher<T> mPreviewFetcher;
        private OnPickerItemClickListener<T> mOnPickerItemClickListener;
        private OnPickerItemCheckListener<T> mOnPickerItemCheckListener;
        private PreviewFetcher.Params mPreviewParams = PreviewFetcher.Params.empty();
        private boolean mPickEnabled = true;
        private Collection<T> mData = Collections.emptyList();

        public Builder<T> setDataController(@Nullable DataController<T> controller) {
            mDataController = controller;
            return this;
        }

        public Builder<T> setPickerController(@Nullable PickerController<T> controller) {
            mPickerController = controller;
            return this;
        }

        public Builder<T> setPreviewFetcher(@NonNull PreviewFetcher<T> previewFetcher) {
            mPreviewFetcher = previewFetcher;
            return this;
        }

        public Builder<T> setOnPickerItemClickListener(@Nullable OnPickerItemClickListener<T> listener) {
            mOnPickerItemClickListener = listener;
            return this;
        }

        public Builder<T> setOnPickerItemCheckListener(@Nullable OnPickerItemCheckListener<T> listener) {
            mOnPickerItemCheckListener = listener;
            return this;
        }

        public Builder<T> setPreviewParams(@Nullable PreviewFetcher.Params params) {
            mPreviewParams = params;
            return this;
        }

        public Builder<T> setPickEnabled(boolean enabled) {
            mPickEnabled = enabled;
            return this;
        }

        public Builder<T> setData(@NonNull Collection<T> data) {
            mData = data;
            return this;
        }

        public PickerAdapter<T> build() {
            if (mPreviewFetcher == null) {
                throw new IllegalStateException("PreviewFetcher can't be null. Set it via `setPreviewFetcher` call.");
            }
            DataController<T> dataController = mDataController != null ? mDataController : new HashSetDataController();

            PickerAdapter<T> adapter = new PickerAdapter<T>(dataController, mPickerController, mPreviewFetcher);
            adapter.mItems.addAll(mData);
            adapter.mPickEnabled = mPickEnabled;
            adapter.mPreviewParams = mPreviewParams != null ? mPreviewParams : PreviewFetcher.Params.empty();
            adapter.mOnPickerItemClickListener = mOnPickerItemClickListener;
            adapter.mOnPickerItemCheckListener = mOnPickerItemCheckListener;

            return adapter;
        }
    }
}
