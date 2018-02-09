package com.dewarder.pickerkit.core.impl

import android.app.Activity
import com.dewarder.pickerkit.utils.SimpleActivityLifecycleCallbacks

//TODO: Poor implementation
internal class ActivityRegistry : SimpleActivityLifecycleCallbacks() {

    private var currentActivity: Activity? = null

    override fun onActivityResumed(activity: Activity) {
        currentActivity = activity
    }

    override fun onActivityPaused(activity: Activity) {
    }

    fun getCurrentActivity(): Activity {
        return currentActivity ?: TODO("add message")
    }
}