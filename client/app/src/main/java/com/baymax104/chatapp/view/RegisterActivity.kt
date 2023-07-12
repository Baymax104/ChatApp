package com.baymax104.chatapp.view

import android.view.View.OnClickListener
import androidx.databinding.adapters.TextViewBindingAdapter.AfterTextChanged
import com.baymax104.basemvvm.view.ActivityStack
import com.baymax104.basemvvm.view.BaseActivity
import com.baymax104.basemvvm.view.ViewConfig
import com.baymax104.basemvvm.vm.State
import com.baymax104.basemvvm.vm.StateHolder
import com.baymax104.basemvvm.vm.activityViewModels
import com.baymax104.basemvvm.vm.applicationViewModels
import com.baymax104.chatapp.BR
import com.baymax104.chatapp.R
import com.baymax104.chatapp.databinding.ActivityRegisterBinding
import com.baymax104.chatapp.service.UserRequester
import com.baymax104.chatapp.utils.ValidateUtil
import com.blankj.utilcode.util.ToastUtils

class RegisterActivity : BaseActivity<ActivityRegisterBinding>() {

    private val states by activityViewModels<States>()

    private val requester by applicationViewModels<UserRequester>()

    class States : StateHolder() {
        val account = State("")
        val password = State("")
        val repeat = State("")
    }

    inner class Handler {
        val register = OnClickListener {
            val account = states.account.value
            val pwd = states.password.value
            val repeat = states.repeat.value
            if (!ValidateUtil.validateAccount(account) || !ValidateUtil.validatePassword(pwd)) {
                ToastUtils.showShort("账号或密码格式错误")
                return@OnClickListener
            }
            if (repeat != pwd) {
                ToastUtils.showShort("重复输入不一致")
                return@OnClickListener
            }
            requester.register(account, pwd) {
                success {
                    ToastUtils.showShort("注册成功")
                    ActivityStack.pop()
                }
                fail { ToastUtils.showShort(it) }
            }
        }

        val setAccount = AfterTextChanged { states.account.value = it.toString() }
        val setPassword = AfterTextChanged { states.password.value = it.toString() }
        val setRepeat = AfterTextChanged { states.repeat.value = it.toString() }
    }

    override fun configBinding(): ViewConfig {
        return ViewConfig(R.layout.activity_register).add(
            BR.state to states,
            BR.handler to Handler()
        )
    }

    override fun configWindow(): WindowConfig {
        return WindowConfig.build { back = true }
    }
}