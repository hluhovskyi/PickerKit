package com.dewarder.pickerkit;

import android.support.annotation.NonNull;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public final class FileCategoryData implements CategoryData<File, File> {

    private final File mSource;
    private final List<File> mData;
    private final int mItemCount;

    private FileCategoryData(File file, Collection<File> data, int itemCount) {
        mSource = file;
        mData = new ArrayList<>(data);
        mItemCount = itemCount;
    }

    public static FileCategoryData fromFiles(@NonNull List<File> files) {
        if (files.isEmpty()) {
            throw new IllegalStateException("Files can't be empty");
        }

        List<File> copy = new ArrayList<>(files);
        return new FileCategoryData(copy.get(0).getParentFile(), copy, copy.size());
    }

    @Override
    public File getSource() {
        return mSource;
    }

    @Override
    public String getName() {
        return mSource.getName();
    }

    @Override
    public List<File> getData() {
        return mData;
    }

    @Override
    public int getItemCount() {
        return mItemCount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FileCategoryData that = (FileCategoryData) o;
        return mItemCount == that.mItemCount && mSource.equals(that.mSource) && mData.equals(that.mData);

    }

    @Override
    public int hashCode() {
        int result = mSource.hashCode();
        result = 31 * result + mData.hashCode();
        result = 31 * result + mItemCount;
        return result;
    }
}
