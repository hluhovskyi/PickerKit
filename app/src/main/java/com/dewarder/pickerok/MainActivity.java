package com.dewarder.pickerok;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.annimon.stream.Stream;
import com.dewarder.pickerok.panel.AttachmentPanelView;

import java.io.File;
import java.util.Collection;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final int PERMISSION_REQUEST_CODE = 10;
    private static final int PICKER_REQUEST_CODE = 100;

    private AttachmentPanelView mAttachmentPanel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.started).setOnClickListener(v -> {
            //ActivityCompat.requestPermissions(
            //        this,
            //        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
            //        PERMISSION_REQUEST_CODE);
        });

        mAttachmentPanel = (AttachmentPanelView) findViewById(R.id.attachment_panel);

        FilesystemScannerPickerDataProvider scanner = new FilesystemScannerPickerDataProvider.Builder()
                .setRoot("/sdcard")
                .setExtensions(".jpg", ".png")
                .build();

        new MediaStorePickerDataProvider(this).request(new PickerDataProvider.Callback<File>() {
            @Override
            public void onNext(Collection<File> data) {
                List<PickerData> pickerData = Stream.of(data).map(PickerData::new).toList();
                mAttachmentPanel.post(() -> mAttachmentPanel.setData(pickerData));
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
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startActivityForResult(new Intent(this, CategoryActivity.class), PICKER_REQUEST_CODE);
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
            List<File> result = CategoryActivity.getResult(data);
            Toast.makeText(this, Stream.of(result).map(File::getName).toList().toString(), Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "Picker canceled", Toast.LENGTH_LONG).show();
        }
    }
}
