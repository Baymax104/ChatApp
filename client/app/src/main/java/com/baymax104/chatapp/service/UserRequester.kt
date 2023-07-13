package com.baymax104.chatapp.service

import com.baymax104.basemvvm.vm.Requester
import com.baymax104.chatapp.entity.Request
import com.baymax104.chatapp.entity.User
import com.baymax104.chatapp.repository.UserStore
import com.baymax104.chatapp.utils.Parser
import com.blankj.utilcode.util.FileIOUtils
import java.io.File

/**
 *
 * @author John
 */
class UserRequester : Requester() {

    fun login(account: String, password: String, block: ReqCallback<User>.() -> Unit) {
        val callback = ReqCallback<User>().apply(block)
        val map = mapOf("account" to account, "password" to password)
        val request = Request("/login", body = map)
            .let { Parser.transform<Request<Any>>(it) }
        WebService.requestOnce(request) {
            success { it -> Parser.transform<User>(it.body).let { callback.onSuccess(it) } }
            fail { callback.onFail(it) }
        }
    }

    fun register(account: String, password: String, block: ReqCallback<Any>.() -> Unit) {
        val callback = ReqCallback<Any>().apply(block)
        val map = mapOf("account" to account, "password" to password)
        val request = Request("/register", body = map)
            .let { Parser.transform<Request<Any>>(it) }
        WebService.requestOnce(request) {
            success { it -> Parser.transform<Any>(it.body).let { callback.onSuccess(it) } }
            fail { callback.onFail(it) }
        }
    }

    fun getOnline(block: ReqCallback<List<User>>.() -> Unit) {
        val callback = ReqCallback<List<User>>().apply(block)
        val request = Request<Any>(
            "/online",
            src = UserStore.id.toString(),
            dest = UserStore.id.toString()
        )
        WebService.request(request) {
            success { it -> Parser.transform<List<User>>(it.body).let { callback.onSuccess(it) } }
            fail { callback.onFail(it) }
        }
    }

    fun updateInfo(user: User, block: ReqCallback<Any>.() -> Unit) {
        val callback = ReqCallback<Any>().apply(block)
        val request = Request(
            "/update",
            src = UserStore.id.toString(),
            dest = UserStore.id.toString(),
            body = user
        ).let { Parser.transform<Request<Any>>(it) }
        WebService.request(request) {
            success { it -> Parser.transform<Any>(it.body).let { callback.onSuccess(it) } }
            fail { callback.onFail(it) }
        }
    }

    fun chatText(content: String, userId: Int) {
        Request(
            "/chat/text",
            src = UserStore.id.toString(),
            dest = userId.toString(),
            body = content
        ).let { Parser.transform<Request<Any>>(it) }
            .let { WebService.send(it) }
    }

    fun chatImage(file: File, userId: Int) {
        val bytes = FileIOUtils.readFile2BytesByStream(file)!!
        Request(
            "/chat/image",
            src = UserStore.id.toString(),
            dest = userId.toString(),
            body = bytes
        ).let { Parser.transform<Request<Any>>(it) }
            .let { WebService.send(it) }

    }
}