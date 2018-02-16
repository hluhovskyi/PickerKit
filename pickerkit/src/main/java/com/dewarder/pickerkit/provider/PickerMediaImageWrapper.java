package com.dewarder.pickerkit.provider;

import com.annimon.stream.Stream;
import com.dewarder.pickerkit.model.PickerImage;
import com.dewarder.pickerkit.model.PickerMedia;

import java.util.Collection;

class PickerMediaImageWrapper implements PickerDataProvider<PickerMedia> {

    private final PickerDataProvider<PickerImage> delegate;

    private PickerMediaImageWrapper(PickerDataProvider<PickerImage> delegate) {
        this.delegate = delegate;
    }

    public static PickerDataProvider<PickerMedia> wrap(PickerDataProvider<PickerImage> provider) {
        return new PickerMediaImageWrapper(provider);
    }

    @Override
    public void request(Callback<PickerMedia> callback) {
        delegate.request(new Callback<PickerImage>() {
            @Override
            public void onNext(Collection<PickerImage> data) {
                callback.onNext(
                        Stream.of(data)
                                .map(PickerImage::getSource)
                                .map(PickerMedia.Companion::image)
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
