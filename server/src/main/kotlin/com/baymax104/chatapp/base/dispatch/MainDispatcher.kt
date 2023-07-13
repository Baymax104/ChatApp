package com.baymax104.chatapp.base.dispatch

import com.baymax104.chatapp.base.ioc.IOC
import com.baymax104.chatapp.base.thread.ServerThread
import com.baymax104.chatapp.base.thread.ThreadMap
import com.baymax104.chatapp.dto.Request
import com.baymax104.chatapp.dto.Response
import com.baymax104.chatapp.service.UserService
import com.baymax104.chatapp.utils.IOUtil
import com.baymax104.chatapp.utils.Parser
import org.slf4j.LoggerFactory
import java.net.Socket

class MainDispatcher(val ioc: IOC) {

    val log = LoggerFactory.getLogger(MainDispatcher::class.java)!!

    fun dispatch(request: Request<Any>, socket: Socket) {
        when (request.path) {
            "/login" -> login(request, socket)
            "/register" -> register(request, socket)
        }
    }

    private fun login(request: Request<Any>, socket: Socket) {
        val service = ioc[UserService::class]
        val map = Parser.transform<Map<String, String>>(request.body)
        val res = service.login(map)
        val response: Response<Any> = with(res) {
            Response(status, code, message, path, Parser.transform<Any>(res.body))
        }
        val json = Parser.encode(response)
        IOUtil.write(json, socket.getOutputStream())

        // 登录成功后开启线程与用户通信并添加到Map中
        if (res.status == "success") {
            val serverThread = ServerThread(res.body!!.id, socket, Dispatcher(ioc))
            serverThread.name = "User-${res.body!!.id} Thread"
            ThreadMap[res.body!!.id] = serverThread
            log.info("Login Success: uid=${res.body!!.id}")
            serverThread.start()
        }
    }

    private fun register(request: Request<Any>, socket: Socket) {
        val service = ioc[UserService::class]
        val map = Parser.decode<Map<String, String>>(Parser.encode(request.body))
        val res = service.register(map)
        val response: Response<Any> = with(res) {
            Response(status, code, message, path, Parser.transform<Any>(res.body))
        }
        val json = Parser.encode(response)
        IOUtil.write(json, socket.getOutputStream())
    }
}