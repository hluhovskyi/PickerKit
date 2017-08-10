package com.dewarder.pickerkit.activity;

import android.app.Activity;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;

import com.dewarder.pickerkit.DefaultPickerOpener;
import com.dewarder.pickerkit.PickerOpener;
import com.dewarder.pickerkit.R;
import com.dewarder.pickerkit.config.PanelPickerBuilder;
import com.dewarder.pickerkit.config.PickerConfig;
import com.dewarder.pickerkit.result.OnPickerGalleryResultListener;
import com.dewarder.pickerkit.result.OnPickerImageResultListener;
import com.dewarder.pickerkit.result.OnPickerVideoResultListener;
import com.dewarder.pickerkit.result.PickerGalleryResult;
import com.dewarder.pickerkit.utils.Lists;
import com.dewarder.pickerkit.utils.Objects;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public final class PickerKit {

    volatile static PickerKit instance;

    private final List<WeakReference<OnPickerGalleryResultListener>> galleryListeners = new CopyOnWriteArrayList<>();
    private final List<WeakReference<OnPickerImageResultListener>> imageListeners = new CopyOnWriteArrayList<>();
    private final List<WeakReference<OnPickerVideoResultListener>> videoListeners = new CopyOnWriteArrayList<>();

    private final List<PickerOpener> defaultOpeners = new ArrayList<>();
    private final List<PickerOpener> customOpeners = new CopyOnWriteArrayList<>();

    private volatile boolean notifyOnCancel = false;

    {
        defaultOpeners.add(DefaultPickerOpener.of(R.id.picker_category_gallery, PickerGalleryFolderActivity::openForResult));
    }

    private PickerKit() {

    }

    public static PickerKit getInstance() {
        if (instance == null) {
            synchronized (PickerKit.class) {
                if (instance == null) {
                    instance = new PickerKit();
                }
            }
        }
        return instance;
    }

    public static PanelPickerBuilder buildPanelPicker() {
        return new PanelPickerBuilder();
    }

    public void addOnPickerGalleryResultListener(@NonNull OnPickerGalleryResultListener listener) {
        Objects.requireNonNull(listener);
        Lists.addWeekReference(galleryListeners, listener);
    }

    public void removeOnPickerGalleryResultListener(@NonNull OnPickerGalleryResultListener listener) {
        Objects.requireNonNull(listener);
        Lists.removeWeekReference(galleryListeners, listener);
    }

    public void addOnPickerImageResultListener(@NonNull OnPickerImageResultListener listener) {
        Objects.requireNonNull(listener);
        Lists.addWeekReference(imageListeners, listener);
    }

    public void removeOnPickerImageResultListener(@NonNull OnPickerImageResultListener listener) {
        Objects.requireNonNull(listener);
        Lists.removeWeekReference(imageListeners, listener);
    }

    public void addOnPickerVideoResultListener(@NonNull OnPickerVideoResultListener listener) {
        Objects.requireNonNull(listener);
        Lists.addWeekReference(videoListeners, listener);
    }

    public void removeOnPickerVideoResultListener(@NonNull OnPickerVideoResultListener listener) {
        Objects.requireNonNull(listener);
        Lists.removeWeekReference(videoListeners, listener);
    }

    public void addPickerOpener(@NonNull PickerOpener opener) {
        Objects.requireNonNull(opener);
        customOpeners.add(opener);
    }

    public void removePickerOpener(@NonNull PickerOpener opener) {
        Objects.requireNonNull(opener);
        customOpeners.remove(opener);
    }

    public void setNotifyOnCancel(boolean shouldNotify) {
        notifyOnCancel = shouldNotify;
    }

    void requestOpenPicker(@NonNull Activity activity, @IdRes int id, @NonNull PickerConfig config) {
        for (PickerOpener opener : customOpeners) {
            if (opener.canOpen(activity, id)) {
                opener.open(activity, id, config);
                return;
            }
        }

        for (PickerOpener opener : defaultOpeners) {
            if (opener.canOpen(activity, id)) {
                opener.open(activity, id, config);
                return;
            }
        }

        throw new IllegalStateException("Unsupported picker id " + id + ". Add custom opener or use predefined ids");
    }

    void notifyGallerySelected(@NonNull PickerGalleryResult result) {
        if (result.isSubmitted() || (notifyOnCancel && result.isCanceled())) {
            Lists.forEach(galleryListeners, listener -> listener.onPickerGalleryResult(result));
        }
    }
}
