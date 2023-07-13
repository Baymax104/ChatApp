package com.baymax104.chatapp.view

import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import androidx.lifecycle.lifecycleScope
import com.baymax104.basemvvm.utils.actionStart
import com.baymax104.basemvvm.view.BaseActivity
import com.baymax104.basemvvm.view.ViewConfig
import com.baymax104.chatapp.R
import com.baymax104.chatapp.databinding.ActivityWelcomeBinding
import com.baymax104.chatapp.repository.UserStore
import com.baymax104.chatapp.utils.MainScope
import com.baymax104.chatapp.utils.MainScopeContext
import com.jaeger.library.StatusBarUtil
import kotlinx.coroutines.cancel
import java.util.Timer
import kotlin.concurrent.timerTask

class WelcomeActivity : BaseActivity<ActivityWelcomeBinding>() {

    override fun configBinding(): ViewConfig {
        return ViewConfig(R.layout.activity_welcome)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        StatusBarUtil.setLightMode(this)

        MainScope = lifecycleScope
        MainScopeContext = MainScope.coroutineContext

        val task = timerTask {
            if (!UserStore.isLogin) {
                val intent = Intent(activity, LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP.or(Intent.FLAG_ACTIVITY_SINGLE_TOP)
                activity actionStart intent
            } else {
                val intent = Intent(activity, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP.or(Intent.FLAG_ACTIVITY_SINGLE_TOP)
                activity actionStart intent
            }
        }

        val timer = Timer()
        timer.schedule(task, 1000)
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.repeatCount == 0) {
            return true
        }
        return super.onKeyDown(keyCode, event)
    }

    override fun onDestroy() {
        super.onDestroy()
        MainScope.cancel()
    }
}