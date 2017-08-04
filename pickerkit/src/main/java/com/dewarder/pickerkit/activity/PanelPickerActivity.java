package com.dewarder.pickerkit.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewAnimationUtils;

import com.annimon.stream.Stream;
import com.dewarder.pickerkit.utils.Activities;
import com.dewarder.pickerkit.FilePickerData;
import com.dewarder.pickerkit.ImmutablePoint;
import com.dewarder.pickerkit.MediaStoreImagePickerDataProvider;
import com.dewarder.pickerkit.PickerDataProvider;
import com.dewarder.pickerkit.R;
import com.dewarder.pickerkit.config.PanelPickerConfig;
import com.dewarder.pickerkit.panel.AttachmentPanelView;
import com.dewarder.pickerkit.panel.OnAttachmentPanelCategoryClickListener;
import com.dewarder.pickerkit.panel.PickerCategories;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.io.File;
import java.util.Collection;
import java.util.List;

public final class PanelPickerActivity extends AppCompatActivity implements OnAttachmentPanelCategoryClickListener {

    private static final String EXTRA_CONFIG = "EXTRA_CONFIG";

    private SlidingUpPanelLayout mSlidingPanel;
    private AttachmentPanelView mPickerPanel;

    public static void start(Activity activity) {

    }

    public static void start(Activity activity, PanelPickerConfig config) {
        Intent intent = new Intent(activity, PanelPickerActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        intent.putExtra(EXTRA_CONFIG, config);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setTheme(R.style.PickerActivityTransparentTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picker_panel);

        PanelPickerConfig config = Activities.getParcelableArgument(this, EXTRA_CONFIG);

        mSlidingPanel = Activities.view(this, R.id.sliding_panel);
        mSlidingPanel.addPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {
            @Override
            public void onPanelSlide(View panel, float slideOffset) {

            }

            @Override
            public void onPanelStateChanged(View panel, SlidingUpPanelLayout.PanelState previousState, SlidingUpPanelLayout.PanelState newState) {
                if (newState == SlidingUpPanelLayout.PanelState.COLLAPSED) {
                    finish();
                }
            }
        });

        mPickerPanel = Activities.view(this, R.id.attachment_panel);
        mPickerPanel.addCategories(PickerCategories.all(this));
        mPickerPanel.setOnAttachmentPanelCategoryClickListener(this);

        mSlidingPanel.post(() -> {
            ImmutablePoint startPoint = config.getStartPoint();
            ViewAnimationUtils.createCircularReveal(mSlidingPanel, startPoint.getX(), startPoint.getY(), 0, 5000).setDuration(1000).start();
        });

        new MediaStoreImagePickerDataProvider(this).request(new PickerDataProvider.Callback<File>() {
            @Override
            public void onNext(Collection<File> data) {
                List<FilePickerData> pickerData = Stream.of(data).map(FilePickerData::from).toList();
                mPickerPanel.post(() -> mPickerPanel.setData(pickerData));
            }

            @Override
            public void onComplete() {

            }

            @Override
            public void onError(Throwable throwable) {

            }
        });
    }

    @Override
    public void onPanelPickerClicked(@IdRes int id) {
/*        switch (id) {
            case R.id.attachment_panel_category_gallery: {
                new CategoryActivity.Builder(this)
                        .setAccentColor(ContextCompat.getColor(this, R.color.colorAccent))
                        .setRequestCode(PICKER_REQUEST_CODE)
                        .setLimit(4)
                        .start();
                break;
            }

            case R.id.attachment_panel_category_hide: {
                mSlidingPanel.setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN);
                break;
            }

            case R.id.attachment_panel_category_send: {
                Toast.makeText(this, Stream.of(mPickerPanel.getPicked())
                        .map(File::getName)
                        .toList()
                        .toString(), Toast.LENGTH_LONG).show();

                mPickerPanel.clearPicked();
                mPickerPanel.replaceCategory(
                        R.id.attachment_panel_category_send,
                        PickerCategories.hide(this));
                mSlidingPanel.setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN);
                break;
            }
        }*/
    }
}
