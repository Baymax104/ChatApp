package com.baymax104.chatapp.view

import android.content.Context
import android.view.View.OnClickListener
import com.baymax104.basemvvm.view.BindingConfig
import com.baymax104.basemvvm.vm.MessageHolder
import com.baymax104.chatapp.BR
import com.baymax104.chatapp.R
import com.baymax104.chatapp.utils.applicationViewModels
import com.baymax104.chatapp.utils.bind
import com.lxj.xpopup.core.CenterPopupView

/**
 * 退出登录对话框
 * @author John
 */
class ExitDialog(context: Context) : CenterPopupView(context) {

    private val messenger by applicationViewModels<Messenger>()

    class Messenger : MessageHolder() {
        val exit = Event<Unit, Unit>()
    }

    inner class Handler {
        val cancel = OnClickListener { dismiss() }
        val confirm = OnClickListener { messenger.exit.send(Unit) }
    }

    override fun getImplLayoutId(): Int = R.layout.dialog_exit

    override fun onCreate() {
        super.onCreate()
        bind(BindingConfig.add(BR.handler to Handler()))
    }
}