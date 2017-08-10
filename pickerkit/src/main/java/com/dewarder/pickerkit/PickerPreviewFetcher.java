package com.dewarder.pickerkit;

import android.content.Context;
import android.widget.ImageView;

import com.dewarder.pickerkit.model.PickerMedia;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;

public final class PickerPreviewFetcher implements PreviewFetcher<PickerMedia> {

    private final Picasso mPicasso;

    public PickerPreviewFetcher(Context context) {
        mPicasso = new Picasso.Builder(context)
                .addRequestHandler(new VideoRequestHandler())
                .build();
    }

    @Override
    public void fetchPreview(PickerMedia from, Params params, ImageView target) {
        RequestCreator request = mPicasso.load(from.getSource());
        request.placeholder(R.drawable.placeholder_default);
        if (!params.isEmpty()) {
            request.resize(params.getWidth(), params.getHeight());
            request.centerCrop();
        }
        request.into(target);
    }
}
