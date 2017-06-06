package com.dewarder.pickerok;

import android.net.Uri;

import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FilesystemScannerPickerDataProvider implements PickerDataProvider<Uri> {

    private static final int DEFAULT_BATCH_SIZE = 25;

    private final ExecutorService mExecutor = Executors.newSingleThreadExecutor();
    private int mBatchSize = DEFAULT_BATCH_SIZE;

    @Override
    public void request(final Callback<Uri> callback) {
        final int batchSize = mBatchSize;

        mExecutor.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    final ArrayList<File> files = new ArrayList<>();
                    walk(new File("/sdcard"), new FileConsumer() {
                        @Override
                        public void consume(File file) {
                            if (file.getName().endsWith(".mp3")) {
                                files.add(file);
                            }
                        }
                    });

                    ArrayList<Uri> uris = new ArrayList<>(files.size());
                    for (File file : files) {
                        uris.add(Uri.fromFile(file));
                    }
                    callback.onNext(uris);
                    callback.onComplete();
                } catch (Exception e) {
                    callback.onError(e);
                }
            }
        });
    }

    private void walk(File root, FileConsumer consumer) {
        File[] list = root.listFiles();
        if (list == null) {
            return;
        }

        for (File f : list) {
            if (f.isDirectory()) {
                walk(f, consumer);
            } else {
                consumer.consume(f.getAbsoluteFile());
            }
        }
    }

    private interface FileConsumer {

        void consume(File file);
    }
}
