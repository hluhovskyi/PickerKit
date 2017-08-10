package com.dewarder.sample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.dewarder.pickerkit.provider.MediaStoreImagePickerDataProvider;
import com.dewarder.pickerkit.activity.PickerKit;
import com.dewarder.pickerkit.config.PanelPickerBuilder;
import com.dewarder.pickerkit.panel.PickerCategories;
import com.dewarder.pickerkit.panel.PickerPanelView;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

public class MainActivity extends AppCompatActivity {

    private SlidingUpPanelLayout mSlidingPanel;
    private PickerPanelView mAttachmentPanel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new PanelPickerBuilder()
                .addPickerCategories(PickerCategories.all(this))
                .setStartPoint(mAttachmentPanel)
                .build();

        mSlidingPanel = (SlidingUpPanelLayout) findViewById(R.id.sliding_panel);
        mSlidingPanel.setFadeOnClickListener(v -> mSlidingPanel.setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN));
        mSlidingPanel.addPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {
            @Override
            public void onPanelSlide(View panel, float slideOffset) {

            }

            @Override
            public void onPanelStateChanged(View panel, SlidingUpPanelLayout.PanelState previousState, SlidingUpPanelLayout.PanelState newState) {
                switch (newState) {
                    case HIDDEN: {
                        mAttachmentPanel.clearPicked();
                        break;
                    }
                }
            }
        });

        findViewById(R.id.opener).setOnClickListener(v -> {
            PickerKit.buildPanelPicker()
                    .setStartPoint(v)
                    .addPickerCategories(PickerCategories.all(this))
                    .start(this);
        });

        mAttachmentPanel = (PickerPanelView) findViewById(R.id.attachment_panel);
        mAttachmentPanel.addCategories(
                PickerCategories.camera(this),
                PickerCategories.gallery(this),
                PickerCategories.video(this),
                PickerCategories.music(this),
                PickerCategories.file(this),
                PickerCategories.contact(this),
                PickerCategories.location(this),
                PickerCategories.hide(this));

        mAttachmentPanel.setOnPickerItemCheckListener((item, checked) -> {
            int pickedCount = mAttachmentPanel.getPicked().size();
            if (pickedCount == 0) {
                mAttachmentPanel.replaceCategory(
                        R.id.picker_category_send,
                        PickerCategories.hide(this));
            } else if (pickedCount == 1 && checked) {
                mAttachmentPanel.replaceCategory(
                        R.id.picker_category_hide,
                        PickerCategories.send(this, pickedCount));
            } else {
                mAttachmentPanel.replaceCategory(
                        R.id.picker_category_send,
                        PickerCategories.send(this, pickedCount));
            }
        });

        mAttachmentPanel.setOnPickerItemClickListener(item -> {

        });

        mAttachmentPanel.setOnAttachmentPanelCategoryClickListener(id -> {

        });
    }
}
