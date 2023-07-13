package com.baymax104.chatapp.service

import com.baymax104.basemvvm.vm.Requester.ReqCallback
import com.baymax104.chatapp.entity.Request
import com.baymax104.chatapp.entity.Response
import com.baymax104.chatapp.repository.AppKey
import com.baymax104.chatapp.repository.ClientCoroutine
import com.baymax104.chatapp.repository.CoroutineHolder
import com.baymax104.chatapp.repository.UserStore
import com.baymax104.chatapp.utils.IOUtil
import com.baymax104.chatapp.utils.Parser
import com.baymax104.chatapp.utils.mainLaunch
import com.blankj.utilcode.util.LogUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.InetAddress
import java.net.Socket

/**
 * 网络服务
 * @author John
 */
object WebService {

    /**
     * 注册与登录的请求方法
     *
     * 注册和登录是客户端与服务端主线程通信，且需要及时回复
     *
     * 在登录成功后创建后台协程与服务端后台线程通信
     * @param request [Request]对象
     * @param block [ReqCallback]回调
     */
    fun requestOnce(request: Request<Any>, block: ReqCallback<Response<Any>>.() -> Unit) =
        mainLaunch {
            val json = Parser.encode(request)
            val callback = ReqCallback<Response<Any>>().apply(block)
            // 已经存在后台协程，不需要注册或登录
            if (CoroutineHolder.coroutine != null) {
                callback.onSuccess(Response("success", "200", body = UserStore.user))
                return@mainLaunch
            }
            try {
                val response = withContext(Dispatchers.IO) {
                    val socket = Socket(InetAddress.getByName(AppKey.SERVER_IP), AppKey.SERVER_PORT)
                    callback.lifeCycle.onStart()

                    // 注册和登录是客户端与服务端主线程通信，且都是及时回复
                    IOUtil.write(json, socket.getOutputStream())
                    LogUtils.iTag(
                        "chat-web",
                        "request {src=${request.src}, dest=${request.dest}, path=${request.path}}"
                    )
                    // 等待Server回复
                    val res = IOUtil.read(socket.getInputStream())
                    val response = Parser.decode<Response<Any>>(res)
                    LogUtils.iTag(
                        "chat-web",
                        "accept {status=${response.status}, path=${response.path}, src=${response.src}, dest=${response.dest}}"
                    )
                    if (response.status != "success") {
                        throw Exception("Error${response.code}: ${response.message}")
                    }
                    // 开启客户端后台协程并保存
                    if (request.path == "/login") {
                        val coroutine = ClientCoroutine(socket)
                        coroutine.start()
                        CoroutineHolder.coroutine = coroutine
                    }
                    response
                }
                callback.onSuccess(response)
            } catch (e: Exception) {
                callback.onFail(e.message ?: "requestOnce Error")
                LogUtils.eTag("chat-web", "requestOnce Error")
            } finally {
                callback.lifeCycle.onFinish()
            }
        }

    /**
     * 连接建立后，使用通信协程进行请求
     * @param request [Request]对象
     * @param block [ReqCallback]回调
     */
    fun request(request: Request<Any>, block: ReqCallback<Response<Any>>.() -> Unit) {
        val json = Parser.encode(request)
        val callback = ReqCallback<Response<Any>>().apply(block)
        if (CoroutineHolder.coroutine == null) return
        mainLaunch {
            runCatching {
                withContext(Dispatchers.IO) {
                    CoroutineHolder.coroutine!!.registerCallback(request.path, callback)
                    val socket = CoroutineHolder.coroutine!!.socket
                    IOUtil.write(json, socket.getOutputStream())
                    LogUtils.iTag(
                        "chat-web",
                        "request {src=${request.src}, dest=${request.dest}, path=${request.path}}"
                    )
                }
            }.onFailure {
                callback.onFail(it.message ?: "request Error")
                LogUtils.eTag("chat-web", it.message ?: "request Error")
            }
        }
    }

    /**
     * 向服务端发送退出请求，服务端接收后取消对应的通信线程
     */
    fun exit() {
        val request = Request<Any>("/exit", src = UserStore.id.toString())
        val json = Parser.encode(request)
        mainLaunch {
            runCatching {
                withContext(Dispatchers.IO) {
                    CoroutineHolder.coroutine!!.callbacks.clear()
                    val socket = CoroutineHolder.coroutine!!.socket
                    IOUtil.write(json, socket.getOutputStream())
                    LogUtils.iTag(
                        "chat-web",
                        "request {src=${request.src}, dest=${request.dest}, path=${request.path}}"
                    )
                }
            }.onFailure {
                LogUtils.eTag("chat-web", it.message ?: "request Error")
            }
        }
    }

    fun send(request: Request<Any>) {
        val json = Parser.encode(request)
        if (CoroutineHolder.coroutine == null) return
        mainLaunch {
            runCatching {
                withContext(Dispatchers.IO) {
                    val socket = CoroutineHolder.coroutine!!.socket
                    IOUtil.write(json, socket.getOutputStream())
                    LogUtils.iTag(
                        "chat-web",
                        "request {src=${request.src}, dest=${request.dest}, path=${request.path}}"
                    )
                }
            }.onFailure {
                LogUtils.eTag("chat-web", it.message ?: "request Error")
            }
        }
    }

}