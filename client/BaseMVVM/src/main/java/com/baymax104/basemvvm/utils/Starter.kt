package com.baymax104.basemvvm.utils

import android.content.Context
import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import kotlin.reflect.KClass

/**
 * 启动Activity
 * @param cl 目的Activity的Class对象
 */
infix fun Context.actionStart(cl: KClass<*>) {
    startActivity(Intent(this, cl.java))
}

infix fun Context.actionStart(intent: Intent) {
    startActivity(intent)
}

fun AppCompatActivity.registerLauncher(onSuccess: () -> Unit): ActivityResultLauncher<Intent> {
    val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == -1) {
            onSuccess()
        }
    }
    lifecycle.addObserver(LifecycleEventObserver { _, event ->
        if (event == Lifecycle.Event.ON_DESTROY) {
            launcher.unregister()
        }
    })
    return launcher
}



