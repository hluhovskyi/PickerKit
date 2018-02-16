package com.dewarder.pickerkit.gallery

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.annotation.IntRange
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.MenuItem
import com.dewarder.pickerkit.*
import com.dewarder.pickerkit.core.PickerKit
import com.dewarder.pickerkit.gallery.model.PickerMedia
import com.dewarder.pickerkit.gallery.results.ResultGallery
import com.dewarder.pickerkit.result.PickerGalleryResult
import com.dewarder.pickerkit.utils.Recyclers
import com.dewarder.pickerkit.utils.argument
import com.dewarder.pickerkit.utils.argumentMutable
import com.dewarder.pickerkit.utils.putArgument
import java.util.*
import kotlin.collections.ArrayList

class PickerGalleryActivity : AppCompatActivity(),
        PickerPanelView.OnSubmitClickListener,
        PickerPanelView.OnCancelClickListener,
        PickerItemAdapter.DataController<PickerMedia>,
        PickerItemAdapter.AccessibilityController<PickerMedia> {

    private val initialPicked = HashSet<PickerMedia>()

    private lateinit var recycler: RecyclerView
    private lateinit var panel: PickerPanelView

    private var mItemMinSize: Int = 0
    private var mItemSpacing: Int = 0

    private val accentColor: Int by argument()
    private val title: String by argument()
    private val limit: Int by argument()

    private val data: ArrayList<PickerMedia> by argument()
    private val selected: ArrayList<PickerMedia> by argument()

    private var totalSelected: Int by argumentMutable()

    private val checked: List<PickerMedia>
        get() = selected.filterNot(initialPicked::contains)

    private val unchecked: List<PickerMedia>
        get() = data.filterNot(selected::contains)

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.PickerActivityTheme)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.acitivty_picker)

        mItemMinSize = resources.getDimensionPixelSize(R.dimen.item_picker_image_min_size)
        mItemSpacing = resources.getDimensionPixelSize(R.dimen.spacing_default)

        initialPicked += selected

        setSupportActionBar(findViewById(R.id.toolbar))
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.title = title

        panel = findViewById(R.id.picker_panel)
        //TODO: panel.setAccentColor(ContextCompat.getColor(this, accentColor))
        panel.setOnSubmitClickListener(this)
        panel.setOnCancelClickListener(this)
        panel.setPickedCount(totalSelected)

        recycler = findViewById(R.id.picker_recycler)
        recycler.post {
            val spanCount = Recyclers.calculateSpanCount(recycler, mItemMinSize)
            val itemSize = Recyclers.calculateItemSize(recycler, spanCount, mItemMinSize, mItemSpacing)

            recycler.layoutManager = GridLayoutManager(this, spanCount)
            recycler.addItemDecoration(GridSpacingItemDecoration(spanCount, mItemSpacing, true))

            recycler.adapter = PickerItemAdapter.Builder<PickerMedia>()
                    .setPreviewFetcher(PickerPreviewFetcher(this))
                    .setDataController(this)
                    .setAccessibilityController(this)
                    .setPreviewParams(PreviewFetcher.Params.of(itemSize, itemSize))
                    .setData(data)
                    .setPickEnabled(limit - totalSelected > 0)
                    .build()
        }
    }

    override fun getPicked(): List<PickerMedia> = selected.toList()

    override fun isPicked(item: PickerMedia): Boolean = item in selected

    override fun onPick(item: PickerMedia) {
        totalSelected++
        panel.setPickedCount(totalSelected)

        selected += item
    }

    override fun onUnpick(item: PickerMedia) {
        totalSelected--
        panel.setPickedCount(totalSelected)

        selected -= item
    }

    override fun clearPicked() {
        selected.clear()
    }

    override fun canPickMore(items: Collection<PickerMedia>): Boolean {
        return limit <= 0 || limit - totalSelected > 0
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finishWithResult(ResultGallery.of(checked, unchecked))
            }
        }
        return true
    }

    override fun onBackPressed() {
        finishWithResult(ResultGallery.of(checked, unchecked))
    }

    override fun onCancelClicked() {
        finishWithResult(ResultGallery.cancel())
    }

    override fun onSubmitClicked() {
        finishWithResult(ResultGallery.submit(checked, unchecked))
    }

    private fun finishWithResult(result: PickerGalleryResult) {
        if (callingActivity == null) {
            PickerKit.getInstance()
                    .provideResults()
                    .push(result)
                    .commit()
        } else {
            val intent = Intent()
            intent.putExtra(EXTRA_RESULT, result)
            setResult(Activity.RESULT_OK, intent)
        }

        finish()
    }

    class Starter(
            private val activity: Activity
    ) {

        private val intent = Intent(activity, PickerGalleryActivity::class.java)

        private var requestCode = -1

        fun setRequestCode(requestCode: Int): Starter = apply {
            this.requestCode = requestCode
        }

        fun setTitle(title: String): Starter = apply {
            intent.putArgument(PickerGalleryActivity::title, title)
        }

        fun setData(data: List<PickerMedia>): Starter = apply {
            intent.putArgument(PickerGalleryActivity::data, ArrayList(data))
        }

        fun setSelected(selected: List<PickerMedia>): Starter = apply {
            intent.putArgument(PickerGalleryActivity::selected, ArrayList(selected))
        }

        fun setTotalSelected(@IntRange(from = 0) totalSelected: Int): Starter = apply {
            intent.putArgument(PickerGalleryActivity::totalSelected, totalSelected)
        }

        fun setLimit(@IntRange(from = 0) limit: Int): Starter = apply {
            intent.putArgument(PickerGalleryActivity::limit, limit)
        }

        fun start() {
            activity.startActivity(intent)
        }

        fun startForResult() {
            val code = if (requestCode == -1) RequestCodeGenerator.generate() else requestCode
            activity.startActivityForResult(intent, code)
        }
    }

    companion object {

        private const val EXTRA_RESULT = "EXTRA_RESULT"

        fun getGalleryResult(data: Intent): PickerGalleryResult =
                data.getParcelableExtra(EXTRA_RESULT)
    }
}
