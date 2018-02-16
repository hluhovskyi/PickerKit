package com.dewarder.pickerkit.provider

import com.dewarder.pickerkit.gallery.model.PickerImage
import com.dewarder.pickerkit.gallery.model.PickerMedia

internal class PickerMediaImageWrapper constructor(
        private val delegate: PickerDataProvider<PickerImage>
) : PickerDataProvider<PickerMedia> {

    override fun request(callback: PickerDataProvider.Callback<PickerMedia>) {
        delegate.request(object : PickerDataProvider.Callback<PickerImage> {
            override fun onNext(data: Collection<PickerImage>) {
                callback.onNext(
                        data.map(PickerImage::source).map(PickerMedia.Companion::image)
                )
            }

            override fun onComplete() {
                callback.onComplete()
            }

            override fun onError(throwable: Throwable) {
                callback.onError(throwable)
            }
        })
    }

    companion object {

        fun wrap(provider: PickerDataProvider<PickerImage>): PickerDataProvider<PickerMedia> =
                PickerMediaImageWrapper(provider)
    }
}
