package com.dewarder.pickerok;

import java.util.Collection;

public interface PickerTarget<T> {

    void addData(Collection<T> data);

    void setOnPickListener(OnPickListener<T> listener);
}
