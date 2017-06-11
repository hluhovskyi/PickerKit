package com.dewarder.pickerok;

import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.provider.MediaStore;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Request;
import com.squareup.picasso.RequestHandler;

import java.io.IOException;

public class VideoRequestHandler extends RequestHandler {

    private String[] mExtensions = {"mp4"};

    @Override
    public boolean canHandleRequest(Request data) {
        String path = data.uri.getPath();
        for (String extension : mExtensions) {
            if (path.endsWith(extension)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Result load(Request data, int arg1) throws IOException {
        Bitmap bm = ThumbnailUtils.createVideoThumbnail(data.uri.getPath(), MediaStore.Images.Thumbnails.MINI_KIND);
        return new Result(bm, Picasso.LoadedFrom.DISK);
    }
}
