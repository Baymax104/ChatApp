package com.baymax104.chatapp.view

import android.os.Bundle
import android.view.View.OnClickListener
import androidx.core.text.isDigitsOnly
import androidx.databinding.adapters.TextViewBindingAdapter.AfterTextChanged
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

class InfoActivity : BaseActivity<ActivityInfoBinding>() {

    private val states by activityViewModels<States>()

    private val messenger by applicationViewModels<Messenger>()


    class States : StateHolder() {
        val user = State(User())
    }

    class Messenger : MessageHolder() {
        val show = Event<User, Unit>()
    }

    inner class Handler {

        val confirm = OnClickListener {

        }

        val setUsername = AfterTextChanged { states.user.value.username = it.toString() }
        val setGender = AfterTextChanged { states.user.value.gender = it.toString() }
        val setAge = AfterTextChanged {
            states.user.value.age = if (it.toString().isDigitsOnly()) it.toString().toInt() else 0
        }
    }

    override fun configBinding(): ViewConfig {
        return ViewConfig(R.layout.activity_info).add(
            BR.state to states,
            BR.handler to Handler()
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        messenger.show.observeSend(this, true) { states.user.value = it }
    }

    override fun configWindow(): WindowConfig {
        return WindowConfig.build { back = true }
    }
}