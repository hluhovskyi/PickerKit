package com.dewarder.pickerkit.gallery

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.annotation.IdRes
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.ViewAnimationUtils
import com.dewarder.pickerkit.ImmutablePoint
import com.dewarder.pickerkit.R
import com.dewarder.pickerkit.chooser.DefaultChooserStarter
import com.dewarder.pickerkit.core.Picker
import com.dewarder.pickerkit.gallery.model.PickerImage
import com.dewarder.pickerkit.gallery.model.PickerMedia
import com.dewarder.pickerkit.panel.OnPickerPanelCategoryClickListener
import com.dewarder.pickerkit.panel.PickerCategories
import com.dewarder.pickerkit.panel.PickerPanelView
import com.dewarder.pickerkit.provider.MediaStoreImagePickerDataProvider
import com.dewarder.pickerkit.provider.PickerDataProvider
import com.dewarder.pickerkit.utils.argument
import com.dewarder.pickerkit.utils.putArgument
import com.sothree.slidinguppanel.SlidingUpPanelLayout

class GalleryPickerActivity : AppCompatActivity(), OnPickerPanelCategoryClickListener {

    private lateinit var slidingPanel: SlidingUpPanelLayout
    private lateinit var pickerPanel: PickerPanelView

    private val revealPoint: ImmutablePoint by argument(default = ImmutablePoint.empty())

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.PickerActivityTransparentTheme)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_picker_panel)

        slidingPanel = findViewById(R.id.sliding_panel)
        slidingPanel.addPanelSlideListener(object : SlidingUpPanelLayout.PanelSlideListener {
            override fun onPanelSlide(panel: View, slideOffset: Float) {

            }

            override fun onPanelStateChanged(panel: View, previousState: SlidingUpPanelLayout.PanelState, newState: SlidingUpPanelLayout.PanelState) {
                if (newState == SlidingUpPanelLayout.PanelState.COLLAPSED) {
                    finish()
                }
            }
        })

        pickerPanel = findViewById(R.id.attachment_panel)
        pickerPanel.addCategories(PickerCategories.all(this))
        pickerPanel.setOnAttachmentPanelCategoryClickListener(this)

        if (!revealPoint.isEmpty) {
            slidingPanel.post {
                ViewAnimationUtils.createCircularReveal(
                        slidingPanel,
                        revealPoint.x,
                        revealPoint.y,
                        0f,
                        5000f
                ).setDuration(1000).start()
            }
        }

        MediaStoreImagePickerDataProvider.of(this).request(object : PickerDataProvider.Callback<PickerImage> {
            override fun onNext(data: Collection<PickerImage>) {
                val pickerData = data
                        .map { it.source }
                        .map { PickerMedia.image(it) }
                        .toList()

                pickerPanel.post { pickerPanel.setData(pickerData) }
            }

            override fun onComplete() {

            }

            override fun onError(throwable: Throwable) {

            }
        })
    }

    override fun onPickerCategoryClicked(@IdRes id: Int) {}

    internal class Starter(
            val context: Context
    ) : DefaultChooserStarter {

        private val intent = Intent(context, GalleryPickerActivity::class.java)

        init {
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
        }

        override fun revealPoint(point: ImmutablePoint) = apply {
            intent.putArgument(GalleryPickerActivity::revealPoint, point)
        }

        override fun only(vararg pickers: Picker<*, *>) = apply {
        }

        override fun excluding(vararg picker: Picker<*, *>) = apply {
        }

        override fun start() {
            context.startActivity(intent)
        }
    }
}
