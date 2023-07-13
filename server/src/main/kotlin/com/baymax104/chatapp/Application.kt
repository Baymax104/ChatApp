package com.baymax104.chatapp

import com.baymax104.chatapp.base.dispatch.MainDispatcher
import com.baymax104.chatapp.base.ioc.IOC
import com.baymax104.chatapp.base.ioc.PackageScanner
import com.baymax104.chatapp.base.ioc.Service
import com.baymax104.chatapp.base.thread.ThreadMap
import com.baymax104.chatapp.dto.Request
import com.baymax104.chatapp.utils.IOUtil
import com.baymax104.chatapp.utils.Parser
import org.slf4j.LoggerFactory
import java.net.ServerSocket

/**
 * Application
 */
class Application {

    private val ioc = IOC()

    private val mainDispatcher = MainDispatcher(ioc)

    val log = LoggerFactory.getLogger(Application::class.java)!!

    /**
     * 初始化服务
     */
    init {
        log.info("Application initializing...")
        val packageName = "com.baymax104.chatapp.service"
        PackageScanner.scan(packageName, Service::class).forEach {
            val instance = it.getDeclaredConstructor().newInstance()
            ioc[it.kotlin] = instance
        }
    }

    /**
     * 监听端口
     */
    fun listen(): Unit = try {
        log.info("Application is listening at 6666 port...")
        ServerSocket(6666).use { server ->
            while (true) {
                // listening...
                val socket = server.accept()
                val json = IOUtil.read(socket.getInputStream())
                val request = Parser.decode<Request<Any>>(json)
                log.info("accept in main, current thread map: $ThreadMap")
                mainDispatcher.dispatch(request, socket)
            }
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }

}