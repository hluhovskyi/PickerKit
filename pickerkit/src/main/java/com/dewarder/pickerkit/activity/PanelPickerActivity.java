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
import com.dewarder.pickerkit.config.PickerConfig;
import com.dewarder.pickerkit.model.PickerImage;
import com.dewarder.pickerkit.model.PickerMedia;
import com.dewarder.pickerkit.utils.Activities;
import com.dewarder.pickerkit.ImmutablePoint;
import com.dewarder.pickerkit.provider.MediaStoreImagePickerDataProvider;
import com.dewarder.pickerkit.provider.PickerDataProvider;
import com.dewarder.pickerkit.R;
import com.dewarder.pickerkit.config.PickerPanelConfig;
import com.dewarder.pickerkit.panel.PickerPanelView;
import com.dewarder.pickerkit.panel.OnPickerPanelCategoryClickListener;
import com.dewarder.pickerkit.panel.PickerCategories;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.util.Collection;
import java.util.List;

public final class PanelPickerActivity extends AppCompatActivity implements OnPickerPanelCategoryClickListener {

    private static final String EXTRA_CONFIG = "EXTRA_CONFIG";

    private SlidingUpPanelLayout mSlidingPanel;
    private PickerPanelView mPickerPanel;

    public static void start(Activity activity) {

    }

    public static void start(Activity activity, PickerPanelConfig config) {
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

        PickerPanelConfig config = Activities.getParcelableArgument(this, EXTRA_CONFIG);

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

        MediaStoreImagePickerDataProvider.of(this).request(new PickerDataProvider.Callback<PickerImage>() {
            @Override
            public void onNext(Collection<PickerImage> data) {
                List<PickerMedia> pickerData = Stream.of(data)
                        .map(PickerImage::getSource)
                        .map(PickerMedia::image)
                        .toList();

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
    public void onPickerCategoryClicked(@IdRes int id) {
        PickerKit.getInstance().requestOpenPicker(this, id, PickerConfig.defaultInstance());
    }
}
