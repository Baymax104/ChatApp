package com.baymax104.chatapp.view

import android.net.Uri
import android.os.Bundle
import android.view.KeyEvent
import android.widget.TextView
import androidx.appcompat.widget.Toolbar.OnMenuItemClickListener
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.baymax104.basemvvm.utils.actionStart
import com.baymax104.basemvvm.view.ActivityStack
import com.baymax104.basemvvm.view.BaseActivity
import com.baymax104.basemvvm.view.BaseAdapter.ListHandlerFactory
import com.baymax104.basemvvm.view.BaseAdapter.ListHandlerFactory.OnItemClickListener
import com.baymax104.basemvvm.view.BindingConfig
import com.baymax104.basemvvm.view.ViewConfig
import com.baymax104.basemvvm.vm.State
import com.baymax104.basemvvm.vm.StateHolder
import com.baymax104.basemvvm.vm.applicationViewModels
import com.baymax104.chatapp.BR
import com.baymax104.chatapp.R
import com.baymax104.chatapp.adapter.OnlineUserAdapter
import com.baymax104.chatapp.databinding.ActivityMainBinding
import com.baymax104.chatapp.entity.ChatDirection
import com.baymax104.chatapp.entity.ChatMessage
import com.baymax104.chatapp.entity.ChatType
import com.baymax104.chatapp.entity.OnlineUser
import com.baymax104.chatapp.repository.ChatMap
import com.baymax104.chatapp.repository.CoroutineHolder
import com.baymax104.chatapp.repository.UserStore
import com.baymax104.chatapp.service.UserRequester
import com.baymax104.chatapp.service.WebService
import com.baymax104.chatapp.utils.CameraUtil
import com.baymax104.chatapp.utils.Parser
import com.blankj.utilcode.util.ToastUtils
import com.scwang.smart.refresh.layout.listener.OnRefreshListener

class MainActivity : BaseActivity<ActivityMainBinding>() {

    private val states by applicationViewModels<States>()
    private val chatMessenger by applicationViewModels<ChatActivity.Messenger>()
    private val requester by applicationViewModels<UserRequester>()
    private val infoMessenger by applicationViewModels<InfoActivity.Messenger>()

    class States : StateHolder() {
        val users = State<List<OnlineUser>>(listOf())
        val isUsersEmpty = State(true)
        val username = State("")
        val isForeground = State(false)
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
                success { users ->
                    states.users.value = users.map { OnlineUser(it) }
                    states.isUsersEmpty.value = users.isEmpty()
                    ChatMap.init(users)
                    layout.finishRefresh()
                }
                fail { ToastUtils.showShort(it) }
            }
        }
    }

    inner class ListHandler : ListHandlerFactory() {

        private val itemClick: OnItemClickListener<OnlineUser> = OnItemClickListener { data, _ ->
            chatMessenger.user.send(data.user)
            data.notice = false
            activity actionStart ChatActivity::class
        }

        override fun getBindingConfig(): BindingConfig = BindingConfig.add(
            BR.itemClick to itemClick
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycle.addObserver(LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_START -> states.isForeground.value = true
                Lifecycle.Event.ON_STOP -> states.isForeground.value = false
                else -> {}
            }
        })

        UserStore.userLoginEvent.observeSend(this, true) { it ->
            if (it) {
                states.username.value = UserStore.username
                requester.getOnline {
                    success { users ->
                        states.users.value = users.map { OnlineUser(it) }
                        states.isUsersEmpty.value = users.isEmpty()
                        ChatMap.init(users)
                        listenChat()
                    }
                    fail { ToastUtils.showShort(it) }
                }
            }
        }

        infoMessenger.update.observeSend(this) {
            states.username.value = UserStore.username
            requester.getOnline {
                success { users ->
                    states.users.value = users.map { OnlineUser(it) }
                    states.isUsersEmpty.value = users.isEmpty()
                    ChatMap.init(users)
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

    private fun listenChat() {

        CoroutineHolder.coroutine!!.registerCallback("/chat/text") {
            success { response ->
                if (states.isForeground.value) {
                    val onlineUser = states.users.value.find { it.user.id == response.src.toInt() }
                        ?: return@success
                    val message = ChatMessage(
                        onlineUser.user.username,
                        Parser.transform(response.body),
                        ChatDirection.REPLY,
                        ChatType.TEXT
                    )
                    ChatMap[onlineUser.user.id]?.apply { add(message) }
                    onlineUser.notice = true
                }
            }
            fail { ToastUtils.showShort("消息文本接收失败") }
        }

        CoroutineHolder.coroutine!!.registerCallback("/chat/image") {
            success { response ->
                if (states.isForeground.value) {
                    val onlineUser = states.users.value.find { it.user.id == response.src.toInt() }
                        ?: return@success
                    val bytes = Parser.transform<ByteArray>(response.body)
                    val file = CameraUtil.bytesToFile(bytes) ?: return@success
                    val uri = Uri.fromFile(file)?.toString() ?: return@success
                    val message = ChatMessage(
                        onlineUser.user.username,
                        uri,
                        ChatDirection.REPLY,
                        ChatType.IMAGE
                    )
                    ChatMap[onlineUser.user.id]?.apply { add(message) }
                    onlineUser.notice = true
                }
            }
            fail { ToastUtils.showShort("消息文本接收失败") }
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
        if (keyCode == KeyEvent.KEYCODE_BACK && event.repeatCount == 0) {
            WebService.exit()
            ActivityStack.finishAll()
            return true
        }
        return super.onKeyDown(keyCode, event)
    }

}


