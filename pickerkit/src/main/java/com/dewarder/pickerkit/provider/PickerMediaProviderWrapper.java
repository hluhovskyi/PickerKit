package com.dewarder.pickerkit.provider;

import com.dewarder.pickerkit.model.PickerImage;
import com.dewarder.pickerkit.model.PickerMedia;
import com.dewarder.pickerkit.model.PickerVideo;

public final class PickerMediaProviderWrapper {

    public static PickerDataProvider<PickerMedia> wrapImage(PickerDataProvider<PickerImage> provider) {
        return PickerMediaImageWrapper.wrap(provider);
    }

    public static PickerDataProvider<PickerMedia> wrapVideo(PickerDataProvider<PickerVideo> provider) {
        return PickerMediaVideoWrapper.wrap(provider);
    }
}
