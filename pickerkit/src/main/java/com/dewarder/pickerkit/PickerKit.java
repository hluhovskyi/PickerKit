package com.dewarder.pickerkit;

import android.support.annotation.NonNull;

import com.dewarder.pickerkit.config.PanelPickerBuilder;
import com.dewarder.pickerkit.result.OnPickerImageResultListener;
import com.dewarder.pickerkit.result.OnPickerVideoResultListener;
import com.dewarder.pickerkit.utils.Lists;
import com.dewarder.pickerkit.utils.Objects;

import java.lang.ref.WeakReference;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public final class PickerKit {

    static PickerKit instance = new PickerKit();

    private final List<WeakReference<OnPickerImageResultListener>> imageListeners = new CopyOnWriteArrayList<>();
    private final List<WeakReference<OnPickerVideoResultListener>> videoListeners = new CopyOnWriteArrayList<>();

    private PickerKit() {

    }

    public static PickerKit getInstance() {
        return instance;
    }

    public static PanelPickerBuilder buildPanelPicker() {
        return new PanelPickerBuilder();
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
}
