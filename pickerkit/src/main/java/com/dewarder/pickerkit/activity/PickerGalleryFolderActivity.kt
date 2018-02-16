package com.dewarder.pickerkit.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.PopupMenu
import android.support.v7.widget.RecyclerView
import android.view.ContextThemeWrapper
import android.view.Gravity
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import com.annimon.stream.Stream
import com.dewarder.pickerkit.*
import com.dewarder.pickerkit.model.PickerImage
import com.dewarder.pickerkit.model.PickerMedia
import com.dewarder.pickerkit.model.PickerMediaFolder
import com.dewarder.pickerkit.model.PickerVideo
import com.dewarder.pickerkit.provider.MediaStoreImagePickerDataProvider
import com.dewarder.pickerkit.provider.MediaStoreVideoPickerDataProvider
import com.dewarder.pickerkit.provider.PickerDataProvider
import com.dewarder.pickerkit.provider.PickerMediaProviderWrapper
import com.dewarder.pickerkit.result.PickerGalleryResult
import com.dewarder.pickerkit.utils.Recyclers
import com.dewarder.pickerkit.utils.argument
import com.dewarder.pickerkit.utils.putArgument
import com.github.rahatarmanahmed.cpv.CircularProgressView
import java.util.*

class PickerGalleryFolderActivity : AppCompatActivity(), OnCategoryClickListener<PickerMediaFolder>, PickerPanelView.OnSubmitClickListener, PickerPanelView.OnCancelClickListener, PickerPanelView.OnCounterClickListener {

    private val galleryLimit: Int by argument()

    private lateinit var imageDataProvider: PickerDataProvider<PickerImage>
    private lateinit var videoDataProvider: PickerDataProvider<PickerVideo>
    private lateinit var dataProvider: PickerDataProvider<PickerMedia>

    private lateinit var title: TextView
    private lateinit var panel: PickerPanelView
    private lateinit var progress: CircularProgressView

    private lateinit var categoryRecycler: RecyclerView
    private lateinit var categoryAdapter: PickerFolderAdapter

    private var categoryItemMinSize: Int = 0
    private var categoryItemSpacing: Int = 0

    private val selected = ArrayList<PickerMedia>()

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.PickerActivityTheme)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category)

        categoryItemMinSize = resources.getDimensionPixelSize(R.dimen.item_category_min_size)
        categoryItemSpacing = resources.getDimensionPixelSize(R.dimen.spacing_default)

        imageDataProvider = MediaStoreImagePickerDataProvider.of(this)
        videoDataProvider = MediaStoreVideoPickerDataProvider.Builder(this)
                .setExtensions(".mp4")
                .build()

        dataProvider = PickerMediaProviderWrapper.wrapImage(imageDataProvider)

        setSupportActionBar(findViewById(R.id.toolbar))
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowTitleEnabled(false)

        title = findViewById(R.id.title)
        title.setOnClickListener(this::showCategoryPopup)

        panel = findViewById(R.id.picker_panel)
        panel.setOnSubmitClickListener(this)
        panel.setOnCancelClickListener(this)
        panel.setOnCounterClickListener(this)
       // panel.setAccentColor(mAccentColor)

        progress = findViewById(R.id.progress)
      //  progress.color = mAccentColor

        categoryRecycler = findViewById(R.id.category_recycler)
        categoryRecycler.post {
            val spanCount = Recyclers.calculateSpanCount(
                    recyclerView = categoryRecycler,
                    minItemSize = categoryItemMinSize
            )

            val itemSize = Recyclers.calculateItemSize(
                    recyclerView = categoryRecycler,
                    spanCount = spanCount,
                    minItemSize = categoryItemMinSize,
                    itemSpacing = categoryItemSpacing
            )

            categoryAdapter = PickerFolderAdapter(PickerMediaFolderPreviewFetcher(this))
            categoryAdapter.setCategoryItemSize(itemSize)
            categoryAdapter.setOnCategoryClickListener(this)

            categoryRecycler.adapter = categoryAdapter
            categoryRecycler.layoutManager = GridLayoutManager(this, spanCount)
            categoryRecycler.addItemDecoration(GridSpacingItemDecoration(spanCount, categoryItemSpacing, true))

            requestData()
        }
    }

    private fun requestData() {
        categoryAdapter.clearCategories()
        progress.startAnimation()
        categoryRecycler.visibility = View.INVISIBLE
        progress.visibility = View.VISIBLE

        dataProvider.request(object : PickerDataProvider.Callback<PickerMedia> {
            override fun onNext(data: Collection<PickerMedia>) {
                val categories = data
                        .groupBy { media -> media.source.lastPathSegment }
                        .map { (_, medias) -> PickerMediaFolder.fromMedia(medias) }

                categoryRecycler.post { categoryAdapter.appendCategories(categories) }
            }

            override fun onComplete() {
                progress.post {
                    progress.stopAnimation()
                    progress.visibility = View.GONE
                    categoryRecycler.visibility = View.VISIBLE
                    panel.visibility = View.VISIBLE
                }
            }

            override fun onError(throwable: Throwable) {
            }
        })
    }

    override fun onCategoryClicked(category: PickerMediaFolder) {
        val pickedPart = Stream.of(selected)
                .filter { f -> category.children.contains(f) }
                .toList()

        PickerGalleryActivity.Starter(this)
                .setRequestCode(IMAGE_PICKER_ACTIVITY_REQUEST_CODE)
                .setTitle(category.name)
                //  .setAccentColor(mAccentColor)
                .setData(category.children)
                .setSelected(pickedPart)
                .setTotalSelected(selected.size)
                .setLimit(galleryLimit)
                .start()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                cancel()
            }
        }
        return false
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != Activity.RESULT_OK) {
            return
        }

        if (requestCode == IMAGE_PICKER_ACTIVITY_REQUEST_CODE) {
            val result = PickerGalleryActivity.getGalleryResult(data)
            if (result.isCanceled) {
                finish()
                return
            }

            selected.removeAll(result.unselected)
            selected.addAll(result.selected)

            if (result.isSubmitted) {
                submit()
            } else {
                panel.setPickedCount(selected.size)
            }
        }
    }

    private fun showCategoryPopup(anchor: View) {
        val wrapper = ContextThemeWrapper(this, R.style.PopupMenuOverlapAnchor)
        val popupMenu = PopupMenu(wrapper, anchor, Gravity.TOP)
        popupMenu.menu.add(0, 0, 0, R.string.label_photos)
        popupMenu.menu.add(0, 1, 0, R.string.label_videos)
        popupMenu.setOnMenuItemClickListener { item ->
            title.setText(
                    if (item.itemId == 0) R.string.label_photos
                    else R.string.label_videos
            )

            dataProvider = if (item.itemId == 0) {
                PickerMediaProviderWrapper.wrapImage(imageDataProvider)
            } else {
                PickerMediaProviderWrapper.wrapVideo(videoDataProvider)
            }

            requestData()
            true
        }
        popupMenu.show()
    }

    override fun onSubmitClicked() {
        submit()
    }

    override fun onCancelClicked() {
        cancel()
    }

    override fun onCounterClicked() {
        PickerGalleryActivity.Starter(this)
                .setRequestCode(IMAGE_PICKER_ACTIVITY_REQUEST_CODE)
                .setTitle(getString(R.string.label_picked))
                //.setAccentColor(mAccentColor)
                .setData(selected)
                .setSelected(selected)
                .setTotalSelected(selected.size)
                .setLimit(galleryLimit)
                .start()
    }

    private fun submit() {
        val intent = Intent()
        //TODO: Unselected isn't handled
        val result = ResultGallery.submit(selected, emptyList())
        intent.putExtra(EXTRA_RESULT, result)
        setResult(Activity.RESULT_OK, intent)
        finish()
    }

    private fun cancel() {
        setResult(Activity.RESULT_CANCELED)
        finish()
    }

    class Starter(val context: Context) {

        private val intent = Intent(context, PickerGalleryFolderActivity::class.java)

        fun setGalleryLimit(limit: Int): Starter = apply {
            intent.putArgument(PickerGalleryFolderActivity::galleryLimit, limit)
        }

        fun start() {
            context.startActivity(intent)
        }
    }

    companion object {

        private const val EXTRA_RESULT = "EXTRA_RESULT"

        private const val IMAGE_PICKER_ACTIVITY_REQUEST_CODE = 1

        fun getGalleryResult(intent: Intent): PickerGalleryResult {
            return intent.getParcelableExtra(EXTRA_RESULT)
        }

    }
}
