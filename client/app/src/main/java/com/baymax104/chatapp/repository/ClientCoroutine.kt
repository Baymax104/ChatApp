package com.baymax104.chatapp.repository

import com.baymax104.basemvvm.vm.Requester
import com.baymax104.chatapp.entity.Response
import com.baymax104.chatapp.utils.IOUtil
import com.baymax104.chatapp.utils.Parser
import com.blankj.utilcode.util.LogUtils
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.Socket
import kotlin.coroutines.CoroutineContext

/**
 * Client协程
 * @author John
 */
class ClientCoroutine(val socket: Socket) : CoroutineScope {

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Default +
                CoroutineName("User Coroutine")

    var callback: Requester.ReqCallback<Response<Any>>? = null

    lateinit var job: Job

    fun start() {
        job = launch(coroutineContext) {
            while (true) {
                if (callback != null) {
                    runCatching {
                        val json = withContext(Dispatchers.IO) {
                            IOUtil.read(socket.getInputStream())
                        }
                        LogUtils.iTag("chat-web", json)
                        val response = Parser.decode<Response<Any>>(json)
                        if (response.status != "success") {
                            throw Exception("Error${response.code}: ${response.message}")
                        }
                        response
                    }.onSuccess {
                        withContext(Dispatchers.Main) { callback!!.onSuccess(it) }
                    }.onFailure {
                        withContext(Dispatchers.Main) {
                            callback!!.onFail(it.message ?: "ClientCoroutine Error")
                        }
                        LogUtils.eTag("chat-web", it.message ?: "ClientCoroutine Error")
                    }
                }
            }
        }
    }

    fun stop() {
        try {
            job.cancel()
        } catch (e: Exception) {
            LogUtils.eTag("chat-web", "User Coroutine is not alive")
        }
    }
}