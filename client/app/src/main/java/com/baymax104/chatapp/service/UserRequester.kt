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

        val map = mapOf(
            "account" to account,
            "password" to password
        )
        val request = Request("/login", body = map)
        val req = Parser.transform<Request<Any>>(request)
        WebService.requestOnce(req) {
            success {
                val res = Parser.transform<User>(it.body)
                callback.onSuccess(res)
            }
            fail { callback.onFail(it) }
        }
    }

    fun register(account: String, password: String, block: ReqCallback<Any>.() -> Unit) {
        val callback = ReqCallback<Any>().apply(block)

        val map = mapOf(
            "account" to account,
            "password" to password
        )
        val request = Request("/register", body = map)
        val req = Parser.transform<Request<Any>>(request)
        WebService.requestOnce(req) {
            success {
                val res = Parser.transform<Any>(it.body)
                callback.onSuccess(res)
            }
            fail { callback.onFail(it) }
        }
    }

    fun getOnline(block: ReqCallback<List<User>>.() -> Unit) {
        val callback = ReqCallback<List<User>>().apply(block)
        val request = Request<Any>("/online", src = UserStore.id.toString())
        WebService.request(request) {
            success {
                val res = Parser.transform<List<User>>(it.body)
                callback.onSuccess(res)
            }
            fail { callback.onFail(it) }
        }
    }

    fun chatImage(file: File, block: ReqCallback<Any>.() -> Unit) {
        val callback = ReqCallback<Any>().apply(block)

        val bytes = FileIOUtils.readFile2BytesByStream(file)!!

//        val request = Request("/chat", src = UserStore.id, )
    }
}