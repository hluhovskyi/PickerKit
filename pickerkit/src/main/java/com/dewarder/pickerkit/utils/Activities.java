package com.dewarder.pickerkit.utils;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.IdRes;
import android.view.View;

@SuppressWarnings("unchecked")
public final class Activities {

    private Activities() {
        throw new UnsupportedOperationException();
    }

    public static <T extends Parcelable> T getParcelableArgument(Activity activity, String key) {
        Bundle extras = getExtras(activity);
        requireArgument(extras, key);
        return extras.getParcelable(key);
    }

    public static <T> T getSerializableArgument(Activity activity, String key) {
        Bundle extras = getExtras(activity);
        requireArgument(extras, key);
        return (T) extras.getSerializable(key);
    }

    public static Bundle getExtras(Activity activity) {
        Intent intent = activity.getIntent();
        if (intent == null) {
            throw new IllegalStateException("Can't get extras. Intent is null.");
        }
        Bundle extras = intent.getExtras();
        if (extras == null) {
            throw new IllegalStateException("No extras provided");
        }
        return extras;
    }

    private static void requireArgument(Bundle bundle, String key) {
        if (!bundle.containsKey(key)) {
            throw new IllegalStateException("Value with key " + key + " must be passed");
        }
    }
}
