package com.dewarder.pickerkit.result;

import com.dewarder.pickerkit.model.PickerMedia;
import com.dewarder.pickerkit.model.PickerMediaType;

import java.util.EnumSet;
import java.util.List;

public interface PickerGalleryResult {

    List<PickerMedia> getSelected();

    PickerImageResult getImageResult();

    PickerVideoResult getVideoResult();

    EnumSet<PickerMediaType> getTypes();

    boolean containsType(PickerMediaType type);
}
