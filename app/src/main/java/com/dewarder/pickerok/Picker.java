package com.dewarder.pickerok;

public interface Picker<IN, OUT> {

    void setPickerDataProvider(PickerDataProvider<IN> dataProvider);

    void setPickerTarget(PickerTarget<OUT> target);
}
