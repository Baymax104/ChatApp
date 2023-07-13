package com.baymax104.chatapp.view

import android.os.Bundle
import android.view.KeyEvent
import android.widget.TextView
import androidx.appcompat.widget.Toolbar.OnMenuItemClickListener
import com.baymax104.basemvvm.utils.actionStart
import com.baymax104.basemvvm.view.ActivityStack
import com.baymax104.basemvvm.view.BaseActivity
import com.baymax104.basemvvm.view.BaseAdapter.ListHandlerFactory
import com.baymax104.basemvvm.view.BaseAdapter.ListHandlerFactory.OnItemClickListener
import com.baymax104.basemvvm.view.BindingConfig
import com.baymax104.basemvvm.view.ViewConfig
import com.baymax104.basemvvm.vm.State
import com.baymax104.basemvvm.vm.StateHolder
import com.baymax104.basemvvm.vm.activityViewModels
import com.baymax104.basemvvm.vm.applicationViewModels
import com.baymax104.chatapp.BR
import com.baymax104.chatapp.R
import com.baymax104.chatapp.adapter.OnlineUserAdapter
import com.baymax104.chatapp.databinding.ActivityMainBinding
import com.baymax104.chatapp.entity.User
import com.baymax104.chatapp.repository.UserStore
import com.baymax104.chatapp.service.UserRequester
import com.baymax104.chatapp.service.WebService
import com.blankj.utilcode.util.ToastUtils
import com.scwang.smart.refresh.layout.listener.OnRefreshListener

class MainActivity : BaseActivity<ActivityMainBinding>() {

    private val states by activityViewModels<States>()
    private val chatMessenger by applicationViewModels<ChatActivity.Messenger>()
    private val requester by applicationViewModels<UserRequester>()
    private val infoMessenger by applicationViewModels<InfoActivity.Messenger>()

    class States : StateHolder() {
        val users = State<List<User>>(listOf())
        val isUsersEmpty = State(true)
    }

    inner class Handler {
        val menuClick = OnMenuItemClickListener { item ->
            val id = item.itemId
            if (id == R.id.menu_user) {
                infoMessenger.show.send(UserStore.user)
                activity actionStart InfoActivity::class
            }
            true
        }

        val refresh = OnRefreshListener { layout ->
            requester.getOnline {
                success {
                    states.users.value = it
                    states.isUsersEmpty.value = it.isEmpty()
                    layout.finishRefresh()
                }
                fail { ToastUtils.showShort(it) }
            }
        }
    }

    inner class ListHandler : ListHandlerFactory() {

        private val itemClick: OnItemClickListener<User> = OnItemClickListener { data, _ ->
            chatMessenger.user.send(data)
            activity actionStart ChatActivity::class
        }

        override fun getBindingConfig(): BindingConfig = BindingConfig.add(
            BR.itemClick to itemClick
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        UserStore.userLoginEvent.observeSend(this, true) { it ->
            if (it) {
                requester.getOnline {
                    success {
                        states.users.value = it
                        states.isUsersEmpty.value = it.isEmpty()
                    }
                    fail { ToastUtils.showShort(it) }
                }
            }
        }

        infoMessenger.update.observeSend(this) {
            requester.getOnline {
                success {
                    states.users.value = it
                    states.isUsersEmpty.value = it.isEmpty()
                }
                fail { ToastUtils.showShort(it) }
            }
        }

        // 若从登录页跳转，则后台协程已创建，否则验证登录，同时建立连接
        requester.login(UserStore.account, UserStore.password) {
            success { UserStore.user = it }
            fail {
                ToastUtils.showShort("登录状态异常，请重新登录")
                activity actionStart LoginActivity::class
            }
        }

    }

    override fun configBinding(): ViewConfig {
        return ViewConfig(R.layout.activity_main).add(
            BR.state to states,
            BR.handler to Handler(),
            BR.adapter to OnlineUserAdapter().apply { factory = ListHandler() }
        )
    }

    override fun configWindow(): WindowConfig = WindowConfig.build { menuId = R.menu.toolbar }

    override fun configUIComponent(binding: ActivityMainBinding) {
        binding.state.onEmpty {
            findViewById<TextView>(R.id.empty_txt)?.apply { text = it as String? }
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.repeatCount == 0){
            WebService.exit()
            ActivityStack.finishAll()
            return true
        }
        return super.onKeyDown(keyCode, event)
    }

}


