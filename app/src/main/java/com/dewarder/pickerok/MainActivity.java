package com.dewarder.pickerok;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.util.Collection;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = MainActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new FilesystemScannerPickerDataProvider().request(new PickerDataProvider.Callback<Uri>() {
            @Override
            public void onNext(Collection<Uri> data) {
                Log.v(TAG, "onNext, data " + data);
            }

            @Override
            public void onComplete() {
                Log.v(TAG, "onComlete");
            }

            @Override
            public void onError(Throwable throwable) {
                Log.v(TAG, "onError, throwable " + throwable.getMessage());
                throwable.printStackTrace();
            }
        });
    }
}
