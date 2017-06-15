package com.dewarder.pickerkit;

import android.content.Context;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;

import java.io.File;

public class CategoryPreviewFetcher implements PreviewFetcher<CategoryData> {

    private final Picasso mPicasso;

    public CategoryPreviewFetcher(Context context) {
        mPicasso = new Picasso.Builder(context)
                .addRequestHandler(new VideoRequestHandler())
                .build();
    }

    @Override
    public void fetchPreview(CategoryData from, Params params, ImageView target) {
        File primaryFile = from.getData().get(0);
        RequestCreator request = mPicasso.load(primaryFile);
        request.placeholder(R.drawable.placeholder_default);
        if (!params.isEmpty()) {
            request.resize(params.getWidth(), params.getHeight());
            request.centerCrop();
        }
        request.into(target);
    }

}
