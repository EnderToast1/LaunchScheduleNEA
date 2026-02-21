package com.niaw.launchnea.navigation

import androidx.activity.OnBackPressedCallback
import androidx.activity.ComponentActivity
import com.niaw.launchnea.ActivityHolder

private var callback: OnBackPressedCallback? = null

actual fun setBackHandler(enabled: Boolean, onBack: () -> Unit) {
    val activity = ActivityHolder.current ?: return // get your activity reference
    callback?.remove()

    if (enabled) {
        callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                onBack()
            }
        }
        activity.onBackPressedDispatcher.addCallback(callback!!)
    }
}