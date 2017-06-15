package com.dewarder.pickerkit;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class CategoryData {

    private final File mCategory;
    private final List<File> mData;
    private final int mItemCount;

    public CategoryData(File file, Collection<File> data, int itemCount) {
        mCategory = file;
        mData = new ArrayList<>(data);
        mItemCount = itemCount;
    }

    public File getCategory() {
        return mCategory;
    }

    public String getName() {
        return mCategory.getName();
    }

    public List<File> getData() {
        return mData;
    }

    public int getItemCount() {
        return mItemCount;
    }
}
