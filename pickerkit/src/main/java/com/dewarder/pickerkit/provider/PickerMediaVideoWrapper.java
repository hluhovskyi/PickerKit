package com.dewarder.pickerkit.provider;

import com.annimon.stream.Stream;
import com.dewarder.pickerkit.model.PickerMedia;
import com.dewarder.pickerkit.model.PickerVideo;

import java.util.Collection;

class PickerMediaVideoWrapper implements PickerDataProvider<PickerMedia> {

    private final PickerDataProvider<PickerVideo> delegate;

    private PickerMediaVideoWrapper(PickerDataProvider<PickerVideo> delegate) {
        this.delegate = delegate;
    }

    public static PickerDataProvider<PickerMedia> wrap(PickerDataProvider<PickerVideo> provider) {
        return new PickerMediaVideoWrapper(provider);
    }

    @Override
    public void request(Callback<PickerMedia> callback) {
        delegate.request(new Callback<PickerVideo>() {
            @Override
            public void onNext(Collection<PickerVideo> data) {
                callback.onNext(
                        Stream.of(data)
                                .map(PickerVideo::getSource)
                                .map(PickerMedia.Companion::video)
                                .toList());
            }

            @Override
            public void onComplete() {
                callback.onComplete();
            }

            @Override
            public void onError(Throwable throwable) {
                callback.onError(throwable);
            }
        });
    }
}
