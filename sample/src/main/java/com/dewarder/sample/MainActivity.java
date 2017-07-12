package com.dewarder.sample;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.annimon.stream.Stream;
import com.dewarder.pickerkit.CategoryActivity;
import com.dewarder.pickerkit.MediaStoreImagePickerDataProvider;
import com.dewarder.pickerkit.FilePickerData;
import com.dewarder.pickerkit.PickerDataProvider;
import com.dewarder.pickerkit.Result;
import com.dewarder.pickerkit.panel.AttachmentPanelCategories;
import com.dewarder.pickerkit.panel.AttachmentPanelView;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.io.File;
import java.util.Collection;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final int PERMISSION_REQUEST_CODE = 10;
    private static final int PICKER_REQUEST_CODE = 100;

    private MediaStoreImagePickerDataProvider mPickerDataProvider;

    private SlidingUpPanelLayout mSlidingPanel;
    private AttachmentPanelView mAttachmentPanel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mPickerDataProvider = new MediaStoreImagePickerDataProvider(this);

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
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    PERMISSION_REQUEST_CODE);
        });

        mAttachmentPanel = (AttachmentPanelView) findViewById(R.id.attachment_panel);
        mAttachmentPanel.addCategories(
                AttachmentPanelCategories.camera(this),
                AttachmentPanelCategories.gallery(this),
                AttachmentPanelCategories.video(this),
                AttachmentPanelCategories.music(this),
                AttachmentPanelCategories.file(this),
                AttachmentPanelCategories.contact(this),
                AttachmentPanelCategories.location(this),
                AttachmentPanelCategories.hide(this));

        mAttachmentPanel.setOnPickerItemCheckListener((item, checked) -> {
            int pickedCount = mAttachmentPanel.getPicked().size();
            if (pickedCount == 0) {
                mAttachmentPanel.replaceCategory(
                        R.id.attachment_panel_category_send,
                        AttachmentPanelCategories.hide(this));
            } else if (pickedCount == 1 && checked) {
                mAttachmentPanel.replaceCategory(
                        R.id.attachment_panel_category_hide,
                        AttachmentPanelCategories.send(this, pickedCount));
            } else {
                mAttachmentPanel.replaceCategory(
                        R.id.attachment_panel_category_send,
                        AttachmentPanelCategories.send(this, pickedCount));
            }
        });

        mAttachmentPanel.setOnPickerItemClickListener(item -> {

        });

        mAttachmentPanel.setOnAttachmentPanelCategoryClickListener(id -> {
            switch (id) {
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
                    Toast.makeText(this, Stream.of(mAttachmentPanel.getPicked())
                            .map(File::getName)
                            .toList()
                            .toString(), Toast.LENGTH_LONG).show();
                    mAttachmentPanel.clearPicked();
                    mAttachmentPanel.replaceCategory(
                            R.id.attachment_panel_category_send,
                            AttachmentPanelCategories.hide(this));
                    mSlidingPanel.setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN);
                    break;
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                mPickerDataProvider.request(new PickerDataProvider.Callback<File>() {
                    @Override
                    public void onNext(Collection<File> data) {
                        List<FilePickerData> pickerData = Stream.of(data).map(FilePickerData::from).toList();
                        mAttachmentPanel.post(() -> mAttachmentPanel.setData(pickerData));
                    }

                    @Override
                    public void onComplete() {
                        mSlidingPanel.post(() -> mSlidingPanel.setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED));
                    }

                    @Override
                    public void onError(Throwable throwable) {

                    }
                });
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode != PICKER_REQUEST_CODE) {
            return;
        }

        if (resultCode == RESULT_OK) {
            Result result = CategoryActivity.getResult(data);
            Toast.makeText(this, Stream.of(result.getPicked()).toList().toString(), Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "Picker canceled", Toast.LENGTH_LONG).show();
        }
    }
}
