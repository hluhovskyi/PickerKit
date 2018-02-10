package com.dewarder.pickerkit.core.impl

import android.app.Activity
import android.os.Bundle
import com.dewarder.pickerkit.utils.SimpleActivityLifecycleCallbacks

//TODO: Poor implementation
internal class ActivityRegistry : SimpleActivityLifecycleCallbacks() {

    private var currentActivity: Activity? = null

    override fun onActivityResumed(activity: Activity) {

    }

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        currentActivity = activity
    }

    override fun onActivityPaused(activity: Activity) {
    }

    fun getCurrentActivity(): Activity {
        return currentActivity ?: TODO("add message")
    }
}