package com.dewarder.pickerkit.provider

import com.dewarder.pickerkit.gallery.model.PickerMedia
import com.dewarder.pickerkit.gallery.model.PickerVideo

internal class PickerMediaVideoWrapper private constructor(
        private val delegate: PickerDataProvider<PickerVideo>
) : PickerDataProvider<PickerMedia> {

    override fun request(callback: PickerDataProvider.Callback<PickerMedia>) {
        delegate.request(object : PickerDataProvider.Callback<PickerVideo> {
            override fun onNext(data: Collection<PickerVideo>) {
                callback.onNext(
                        data.map { it.source }.map { PickerMedia.video(it) }
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

        fun wrap(provider: PickerDataProvider<PickerVideo>): PickerDataProvider<PickerMedia> =
                PickerMediaVideoWrapper(provider)
    }
}
