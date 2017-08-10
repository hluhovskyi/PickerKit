package com.dewarder.pickerkit.config;

import android.app.Activity;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.dewarder.pickerkit.ImmutablePoint;
import com.dewarder.pickerkit.activity.PanelPickerActivity;
import com.dewarder.pickerkit.panel.PickerCategory;
import com.dewarder.pickerkit.utils.Objects;

import java.util.ArrayList;
import java.util.List;

public final class PanelPickerBuilder {

    private View startPointView;
    private ImmutablePoint startPoint = ImmutablePoint.empty();

    private PickerConfig uiConfig;
    private PickerDataConfig dataConfig;

    private ArrayList<PickerCategory> pickerCategories = new ArrayList<>();

    public PanelPickerBuilder setStartPoint(@NonNull ImmutablePoint point) {
        Objects.requireNonNull(point);
        startPoint = point;
        return this;
    }

    public PanelPickerBuilder setStartPoint(@Nullable View view) {
        startPointView = view;
        return this;
    }

    public PanelPickerBuilder setStartPoint(@IntRange(from = 0, to = Integer.MAX_VALUE) int x,
                                            @IntRange(from = 0, to = Integer.MAX_VALUE) int y) {

        startPoint = ImmutablePoint.of(x, y);
        return this;
    }

    public PanelPickerBuilder addPickerCategory(@NonNull PickerCategory category) {
        Objects.requireNonNull(category);
        pickerCategories.add(category);
        return this;
    }

    public PanelPickerBuilder addPickerCategories(@NonNull List<PickerCategory> categories) {
        Objects.requireNonNull(categories);
        pickerCategories.addAll(categories);
        return this;
    }

    public PanelPickerBuilder setUIConfig(@NonNull PickerConfig uiConfig) {
        Objects.requireNonNull(uiConfig);
        this.uiConfig = uiConfig;
        return this;
    }

    public void start(Activity activity) {
        PanelPickerActivity.start(activity, build());
    }

    public PickerPanelConfig build() {
        return new PickerPanelConfig(this);
    }

    ImmutablePoint getStartPoint() {
        if (startPointView != null) {
            int[] location = new int[2];
            startPointView.getLocationInWindow(location);
            return ImmutablePoint.of(location[0] + startPointView.getWidth() / 2, location[1] + startPointView.getHeight() / 2);
        }
        return startPoint;
    }

    List<PickerCategory> getCategories() {
        return pickerCategories;
    }
}
