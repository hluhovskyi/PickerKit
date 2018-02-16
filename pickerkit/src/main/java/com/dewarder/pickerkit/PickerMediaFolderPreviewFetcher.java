package com.dewarder.pickerkit;

import android.content.Context;
import android.net.Uri;
import android.widget.ImageView;

import com.dewarder.pickerkit.gallery.model.PickerMediaFolder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;

public class PickerMediaFolderPreviewFetcher implements PreviewFetcher<PickerMediaFolder> {

    private final Picasso mPicasso;

    public PickerMediaFolderPreviewFetcher(Context context) {
        mPicasso = new Picasso.Builder(context)
                .addRequestHandler(new VideoRequestHandler())
                .build();
    }

    @Override
    public void fetchPreview(PickerMediaFolder from, Params params, ImageView target) {
        Uri primaryUri = from.getChildren().get(0).getSource();
        RequestCreator request = mPicasso.load(primaryUri);
        request.placeholder(R.drawable.placeholder_default);
        if (!params.isEmpty()) {
            request.resize(params.getWidth(), params.getHeight());
            request.centerCrop();
        }
        request.into(target);
    }

}
