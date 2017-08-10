package com.dewarder.pickerkit.result;

import com.dewarder.pickerkit.model.PickerMedia;
import com.dewarder.pickerkit.model.PickerMediaType;

import java.util.EnumSet;

public interface PickerGalleryResult extends PickerBaseMediaResult<PickerMedia> {

    PickerImageResult getImageResult();

    PickerVideoResult getVideoResult();

    EnumSet<PickerMediaType> getTypes();

    boolean containsType(PickerMediaType type);
}
