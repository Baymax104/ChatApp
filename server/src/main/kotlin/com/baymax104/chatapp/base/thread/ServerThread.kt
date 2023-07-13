package com.baymax104.chatapp.base.thread

import com.baymax104.chatapp.base.dispatch.Dispatcher
import com.baymax104.chatapp.dto.Request
import com.baymax104.chatapp.dto.Response
import com.baymax104.chatapp.utils.IOUtil
import com.baymax104.chatapp.utils.Parser
import org.slf4j.LoggerFactory
import java.net.Socket

class ServerThread(
    val id: Int,
    val socket: Socket,
    val dispatcher: Dispatcher
) : Thread() {

    val log = LoggerFactory.getLogger(ServerThread::class.java)!!

    override fun run() {
        while (true) {
            val json = IOUtil.read(socket.getInputStream())
            log.info("accept request: $this")
            val request = Parser.decode<Request<Any>>(json)
            // exit
            if (request.path == "/exit") {
                ThreadMap -= request.src.toInt()
                socket.close()
                log.info("exit: $this")
                break
            }
            val response = dispatcher.dispatch(request)
            if (ThreadMap[request.dest.toInt()] == null) {
                val res = Response<Any>("error", "404", "用户未上线", body = Unit)
                IOUtil.write(Parser.encode(res), socket.getOutputStream())
            } else {
                response.src = request.src
                response.dest = request.dest
                val res = Parser.encode(response)
                log.info(res)
                val destSocket = ThreadMap[response.dest.toInt()]!!.socket
                IOUtil.write(res, destSocket.getOutputStream())
            }
        }
    }

    override fun toString(): String {
        return "ServerThread(id=$id, socket=$socket)"
    }

}