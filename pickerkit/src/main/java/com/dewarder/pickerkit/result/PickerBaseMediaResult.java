package com.dewarder.pickerkit.result;

import java.util.List;

public interface PickerBaseMediaResult<T> extends PickerResult {

    List<T> getSelected();

    List<T> getUnselected();
}
