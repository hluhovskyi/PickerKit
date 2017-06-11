package com.dewarder.pickerok;

import com.annimon.stream.Stream;
import com.annimon.stream.function.Consumer;
import com.annimon.stream.function.Predicate;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public final class FilesystemScannerPickerDataProvider implements PickerDataProvider<File> {

    public interface PostFilter extends Predicate<File> {

    }

    private final ExecutorService mExecutor = Executors.newSingleThreadExecutor();

    private final File mRoot;
    private final String[] mExtensions;
    private final PostFilter mPostFilter;

    private FilesystemScannerPickerDataProvider(File root,
                                                String[] extensions,
                                                PostFilter postFilter) {
        mRoot = root;
        mExtensions = extensions;
        mPostFilter = postFilter;
    }

    @Override
    public void request(final Callback<File> callback) {
        mExecutor.submit(() -> {
            try {
                final ArrayList<File> files = new ArrayList<>();
                walk(mRoot, file -> {
                    for (String extension : mExtensions) {
                        if (file.getName().endsWith(extension)) {
                            files.add(file);
                        }
                    }
                });

                final List<File> data = Stream.of(files).filter(mPostFilter).toList();
                callback.onNext(data);
                callback.onComplete();
            } catch (Exception e) {
                callback.onError(e);
            }
        });
    }

    private void walk(File root, Consumer<File> consumer) {
        File[] list = root.listFiles();
        if (list == null) {
            return;
        }

        for (File f : list) {
            if (f.isHidden()) {
                continue;
            }

            if (f.isDirectory()) {
                walk(f, consumer);
            } else {
                consumer.accept(f.getAbsoluteFile());
            }
        }
    }

    public static class Builder {

        private File mRoot;
        private String[] mExtensions;
        private PostFilter mPostFilter = f -> true;

        public Builder setRoot(File root) {
            mRoot = root;
            return this;
        }

        public Builder setRoot(String root) {
            mRoot = new File(root);
            return this;
        }

        public Builder setExtensions(String... extensions) {
            mExtensions = extensions;
            return this;
        }

        public Builder setPostFilter(PostFilter filter) {
            mPostFilter = filter;
            return this;
        }

        public FilesystemScannerPickerDataProvider build() {
            return new FilesystemScannerPickerDataProvider(mRoot, mExtensions, mPostFilter);
        }
    }

}
