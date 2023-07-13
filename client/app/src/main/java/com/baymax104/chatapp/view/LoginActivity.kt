package com.baymax104.chatapp.view

import android.view.KeyEvent
import android.view.View.OnClickListener
import androidx.databinding.adapters.TextViewBindingAdapter.AfterTextChanged
import com.baymax104.basemvvm.utils.actionStart
import com.baymax104.basemvvm.view.ActivityStack
import com.baymax104.basemvvm.view.BaseActivity
import com.baymax104.basemvvm.view.ViewConfig
import com.baymax104.basemvvm.vm.State
import com.baymax104.basemvvm.vm.StateHolder
import com.baymax104.basemvvm.vm.activityViewModels
import com.baymax104.basemvvm.vm.applicationViewModels
import com.baymax104.chatapp.BR
import com.baymax104.chatapp.R
import com.baymax104.chatapp.databinding.ActivityLoginBinding
import com.baymax104.chatapp.repository.UserStore
import com.baymax104.chatapp.service.UserRequester
import com.baymax104.chatapp.utils.ValidateUtil
import com.blankj.utilcode.util.ToastUtils

class LoginActivity : BaseActivity<ActivityLoginBinding>() {

    private val states by activityViewModels<States>()
    private val requester by applicationViewModels<UserRequester>()

    class States : StateHolder() {
        val account: State<String> = State("")
        val password: State<String> = State("")
    }

    inner class Handler {

        val login = OnClickListener {
            val account = states.account.value
            val password = states.password.value
            if (!ValidateUtil.validateAccount(account) || !ValidateUtil.validatePassword(password)) {
                ToastUtils.showShort("账号或密码格式错误")
                return@OnClickListener
            }
            requester.login(account, password) {
                success {
                    ToastUtils.showShort("登录成功")
                    UserStore.user = it
                    activity actionStart MainActivity::class
                }
                fail { ToastUtils.showShort(it) }
            }
        }

        val register = OnClickListener { activity actionStart RegisterActivity::class }

        val setAccount = AfterTextChanged { states.account.value = it.toString() }
        val setPassword = AfterTextChanged { states.password.value = it.toString() }
    }

    override fun configBinding(): ViewConfig =
        ViewConfig(R.layout.activity_login).add(
            BR.state to states,
            BR.handler to Handler()
        )

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.repeatCount == 0) {
            ActivityStack.finishAll()
            return true
        }
        return super.onKeyDown(keyCode, event)
    }
}