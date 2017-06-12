package com.dewarder.pickerok;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.annimon.stream.Stream;

import java.io.File;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final int PICKER_REQUEST_CODE = 100;

    public static final String TAG = MainActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.started).setOnClickListener(v -> {
            startActivityForResult(new Intent(this, CategoryActivity.class), PICKER_REQUEST_CODE);
        });
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
