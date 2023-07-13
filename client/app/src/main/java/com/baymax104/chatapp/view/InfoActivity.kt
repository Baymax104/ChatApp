package com.baymax104.chatapp.view

import android.os.Bundle
import android.view.View.OnClickListener
import androidx.databinding.adapters.TextViewBindingAdapter.AfterTextChanged
import com.baymax104.basemvvm.view.ActivityStack
import com.baymax104.basemvvm.view.BaseActivity
import com.baymax104.basemvvm.view.ViewConfig
import com.baymax104.basemvvm.vm.MessageHolder
import com.baymax104.basemvvm.vm.State
import com.baymax104.basemvvm.vm.StateHolder
import com.baymax104.basemvvm.vm.activityViewModels
import com.baymax104.basemvvm.vm.applicationViewModels
import com.baymax104.chatapp.BR
import com.baymax104.chatapp.R
import com.baymax104.chatapp.databinding.ActivityInfoBinding
import com.baymax104.chatapp.entity.User
import com.baymax104.chatapp.repository.UserStore
import com.baymax104.chatapp.service.UserRequester
import com.baymax104.chatapp.service.WebService
import com.baymax104.chatapp.utils.create
import com.blankj.utilcode.util.ToastUtils

class InfoActivity : BaseActivity<ActivityInfoBinding>() {

    private val states by activityViewModels<States>()
    private val messenger by applicationViewModels<Messenger>()
    private val requester by applicationViewModels<UserRequester>()
    private val exitMessenger by applicationViewModels<ExitDialog.Messenger>()

    class States : StateHolder() {
        val user = State(User())
    }

    class Messenger : MessageHolder() {
        val show = Event<User, Unit>()
        val update = Event<Unit, Unit>()
    }

    inner class Handler {

        val confirm = OnClickListener {
            requester.updateInfo(states.user.value) {
                success {
                    UserStore.setUserInfo(states.user.value)
                    messenger.update.send(Unit)
                    finish()
                }
                fail { ToastUtils.showShort(it) }
            }
        }

        val exit = OnClickListener { create(ExitDialog::class).show() }

        val setUsername = AfterTextChanged { states.user.value.username = it.toString() }
        val setGender = AfterTextChanged { states.user.value.gender = it.toString() }
        val setAge = AfterTextChanged { states.user.value.age = it.toString() }
    }

    override fun configBinding(): ViewConfig {
        return ViewConfig(R.layout.activity_info).add(
            BR.state to states,
            BR.handler to Handler()
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        messenger.show.observeSend(this, true) { states.user.value = User(it) }
        exitMessenger.exit.observeSend(this) {
            UserStore.initUser()
            WebService.exit()
            ActivityStack.finishAll()
        }
    }

    override fun configWindow(): WindowConfig {
        return WindowConfig.build { back = true }
    }
}