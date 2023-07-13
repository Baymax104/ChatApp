package com.baymax104.chatapp.repository

import com.baymax104.basemvvm.vm.MessageHolder
import com.baymax104.basemvvm.vm.MessageHolder.Event
import com.baymax104.basemvvm.vm.Requester.ReqCallback
import com.baymax104.chatapp.exception.WebException
import com.baymax104.chatapp.entity.Response
import com.baymax104.chatapp.utils.IOUtil
import com.baymax104.chatapp.utils.Parser
import com.blankj.utilcode.util.LogUtils
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.Socket
import java.net.SocketException
import kotlin.coroutines.CoroutineContext

/**
 * Client协程
 * @author John
 */
class ClientCoroutine(val socket: Socket) : CoroutineScope {

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Default +
                CoroutineName("User Coroutine")

    val callbacks: MutableMap<String, MutableList<ReqCallback<Response<Any>>>> = mutableMapOf()

    lateinit var job: Job

    fun start() {
        job = launch(coroutineContext) {
            while (true) {
                try {
                    // accept
                    val json = withContext(Dispatchers.IO) {
                        IOUtil.read(socket.getInputStream())
                    }
                    val response = Parser.decode<Response<Any>>(json)
                    LogUtils.iTag("chat-web",
                        "accept {status=${response.status}, path=${response.path}, src=${response.src}, dest=${response.dest}} in ClientCoroutine")

                    // judge status
                    if (response.status != "success") {
                        throw WebException("Error${response.code}: ${response.message}", response)
                    }
                    if (exit(response, socket)) break

                    // consume callback
                    withContext(Dispatchers.Main) {
                        callbacks[response.path]?.forEach { it.onSuccess(response) }
                    }
                    if (!response.path.startsWith("/chat")) {
                        unregisterCallback(response.path)
                    }
                } catch(e: Exception) {
                    if (e is WebException) {
                        val response = e.response
                        withContext(Dispatchers.Main) {
                            callbacks[response.path]?.forEach { it.onFail(e.message ?: "ClientCoroutine Error") }
                        }
                        if (!response.path.startsWith("/chat")) {
                            unregisterCallback(response.path)
                        }
                    }
                    LogUtils.eTag("chat-web", e.message ?: "ClientCoroutine Error")
                }
            }
        }
    }

    private fun exit(response: Response<Any>, socket: Socket): Boolean {
        if (response.path != "/exit") return false
        CoroutineHolder.coroutine = null
        socket.close()
        return true
    }

    fun registerCallback(tag: String, callback: ReqCallback<Response<Any>>) {
        if (callbacks[tag] == null) {
            callbacks[tag] = mutableListOf()
        }
        callbacks[tag]!!.add(callback)
    }

    fun registerCallback(tag: String, block: ReqCallback<Response<Any>>.() -> Unit) {
        if (callbacks[tag] == null) {
            callbacks[tag] = mutableListOf()
        }
        callbacks[tag]!!.add(ReqCallback.build(block))
    }

    fun unregisterCallback(tag: String) {
        callbacks.remove(tag)
    }

    fun stop() {
        try {
            job.cancel()
        } catch (e: Exception) {
            LogUtils.eTag("chat-web", "User Coroutine is not alive")
        }
    }
}