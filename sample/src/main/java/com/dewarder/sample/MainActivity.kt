package com.dewarder.sample

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Toast
import com.dewarder.pickerkit.ImagePicker
import com.dewarder.pickerkit.ImmutablePoint
import com.dewarder.pickerkit.chooser.DefaultChooser
import com.dewarder.pickerkit.core.PickerKit
import com.dewarder.pickerkit.result.PickerGalleryResult

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<View>(R.id.opener).setOnClickListener {
            PickerKit.getInstance()
                    .openChooser(DefaultChooser)
                    .revealPoint(ImmutablePoint(500, 500))
                    .start()
        }


        PickerKit.getInstance()
                .listenResults()
                .observe<PickerGalleryResult>(ImagePicker, { result ->
                    Toast.makeText(this,
                            result.imageResult.selected.map { it.source }.joinToString(", "),
                            Toast.LENGTH_LONG
                    ).show()
                })
                .build()
    }
}
