package com.dewarder.pickerkit.provider;

import com.dewarder.pickerkit.gallery.model.PickerImage;
import com.dewarder.pickerkit.gallery.model.PickerMedia;
import com.dewarder.pickerkit.gallery.model.PickerVideo;

public final class PickerMediaProviderWrapper {

    public static PickerDataProvider<PickerMedia> wrapImage(PickerDataProvider<PickerImage> provider) {
        return PickerMediaImageWrapper.Companion.wrap(provider);
    }

    public static PickerDataProvider<PickerMedia> wrapVideo(PickerDataProvider<PickerVideo> provider) {
        return PickerMediaVideoWrapper.Companion.wrap(provider);
    }
}
