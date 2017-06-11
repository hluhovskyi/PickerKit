package com.dewarder.pickerok;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public final class ActivityUtils {

    private ActivityUtils() {
        throw new UnsupportedOperationException();
    }

    public static boolean hasArguments(Activity activity) {
        return activity.getIntent() != null && activity.getIntent().getExtras() != null;
    }

    public static <T> T getSerializableArgument(Activity activity, String key) {
        Intent intent = activity.getIntent();
        if (intent == null) {
            return null;
        }
        Bundle args = intent.getExtras();
        if (args == null) {
            return null;
        }
        return (T) args.getSerializable(key);
    }
}
