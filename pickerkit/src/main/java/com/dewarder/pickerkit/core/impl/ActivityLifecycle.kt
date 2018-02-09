package com.dewarder.pickerkit.core.impl

import android.app.Activity

internal class ActivityLifecycle(
        val activityClass: Class<Activity>
): GenericLifecycle {

    override fun onResume() {
    }

    override fun onPause() {
    }
}