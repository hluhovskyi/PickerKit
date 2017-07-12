package com.dewarder.pickerkit;

import android.content.Context;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;

public final class FilePickerDataPreviewFetcher implements PreviewFetcher<FilePickerData> {

    private final Picasso mPicasso;

    public FilePickerDataPreviewFetcher(Context context) {
        mPicasso = new Picasso.Builder(context)
                .addRequestHandler(new VideoRequestHandler())
                .build();
    }

    @Override
    public void fetchPreview(FilePickerData from, Params params, ImageView target) {
        RequestCreator request = mPicasso.load(from.getSource());
        request.placeholder(R.drawable.placeholder_default);
        if (!params.isEmpty()) {
            request.resize(params.getWidth(), params.getHeight());
            request.centerCrop();
        }
        request.into(target);
    }
}
