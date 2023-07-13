package com.baymax104.chatapp.view

import android.Manifest
import android.net.Uri
import android.os.Bundle
import android.view.View.OnClickListener
import androidx.databinding.adapters.TextViewBindingAdapter.AfterTextChanged
import com.baymax104.basemvvm.utils.Permission
import com.baymax104.basemvvm.utils.registerLauncher
import com.baymax104.basemvvm.view.BaseActivity
import com.baymax104.basemvvm.view.ViewConfig
import com.baymax104.basemvvm.vm.MessageHolder
import com.baymax104.basemvvm.vm.Requester
import com.baymax104.basemvvm.vm.State
import com.baymax104.basemvvm.vm.StateHolder
import com.baymax104.basemvvm.vm.applicationViewModels
import com.baymax104.chatapp.BR
import com.baymax104.chatapp.R
import com.baymax104.chatapp.adapter.MessageAdapter
import com.baymax104.chatapp.databinding.ActivityChatBinding
import com.baymax104.chatapp.entity.ChatDirection.REPLY
import com.baymax104.chatapp.entity.ChatDirection.SEND
import com.baymax104.chatapp.entity.ChatMessage
import com.baymax104.chatapp.entity.ChatType.IMAGE
import com.baymax104.chatapp.entity.ChatType.TEXT
import com.baymax104.chatapp.entity.User
import com.baymax104.chatapp.repository.CoroutineHolder
import com.baymax104.chatapp.repository.UserStore
import com.baymax104.chatapp.service.UserRequester
import com.baymax104.chatapp.utils.CameraUtil
import com.baymax104.chatapp.utils.Parser
import com.baymax104.chatapp.utils.create
import com.blankj.utilcode.util.ToastUtils
import com.blankj.utilcode.util.UriUtils

/**
 * 聊天页
 * @author John
 */
class ChatActivity : BaseActivity<ActivityChatBinding>() {

    private val states by applicationViewModels<States>()

    private val messenger by applicationViewModels<Messenger>()

    private val moreMessenger by applicationViewModels<ChatMoreDialog.Messenger>()

    private val requester by applicationViewModels<UserRequester>()

    private val photoLauncher = registerLauncher {
        val file = UriUtils.uri2File(states.photo) ?: return@registerLauncher
        CameraUtil.compress(this, file) { f ->
            val uri = Uri.fromFile(f)?.toString() ?: return@compress
            val message = ChatMessage("lalala", uri, SEND, IMAGE)
            states.messages.value.apply { add(message) }.let { states.messages.value = it }
        }
    }

    class States : StateHolder() {
        val user = State(User())
        val messages = State<MutableList<ChatMessage>>(mutableListOf())
        val content = State("")
        var photo: Uri? = null
    }

    class Messenger : MessageHolder() {
        val user = Event<User, Unit>()
    }

    inner class Handler {

        val send = OnClickListener {
            val content = states.content.value
            if (content.isNotEmpty()) {
                requester.chatText(content, states.user.value.id)
                val message = ChatMessage(UserStore.username, content, SEND, TEXT)
                states.messages.value.apply { add(message) }.let { states.messages.value = it }
                states.content.value = ""
            }
        }

        val more = OnClickListener { create(ChatMoreDialog::class).show() }
        val setContent = AfterTextChanged { states.content.value = it.toString() }
    }

    override fun configBinding(): ViewConfig {
        return ViewConfig(R.layout.activity_chat).add(
            BR.state to states,
            BR.handler to Handler(),
            BR.adapter to MessageAdapter()
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        messenger.user.observeSend(this, true) { states.user.value = it }

        moreMessenger.action.observeSend(this) {
            Permission.permission(Manifest.permission.CAMERA)
                .granted { takePhoto() }
                .denied { ToastUtils.showShort("权限申请失败，请到权限中心开启权限") }
                .request()
        }

        CoroutineHolder.coroutine!!.callback = Requester.ReqCallback.build {
            success { it ->
                val content = Parser.transform<String>(it.body)
                val message = ChatMessage(states.user.value.username, content, REPLY, TEXT)
                states.messages.value.apply { add(message) }.let { states.messages.value = it }
            }
            fail { ToastUtils.showShort("消息接收失败") }
        }
    }

    private fun takePhoto() {
        val file = CameraUtil.createNewFile(false)
        states.photo = UriUtils.file2Uri(file)
        val intent = CameraUtil.startCamera(file)
        intent?.let { photoLauncher.launch(it) }
    }

    override fun configWindow(): WindowConfig {
        return WindowConfig.build { back = true }
    }
}