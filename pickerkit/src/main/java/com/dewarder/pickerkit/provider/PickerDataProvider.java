package com.dewarder.pickerkit.provider;

import java.util.Collection;

public interface PickerDataProvider<T> {

    void request(Callback<T> callback);

    interface Callback<T> {

        void onNext(Collection<T> data);

        void onComplete();

        void onError(Throwable throwable);
    }
}
