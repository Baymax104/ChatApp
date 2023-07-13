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

            // accept
            val json = IOUtil.read(socket.getInputStream())
            val request = Parser.decode<Request<Any>>(json)
            log.info("accept {src=${request.src}, dest=${request.dest}, path=${request.path}} in $this")

            // exit
            if (exit(request, socket)) break

            // dispatch
            val response = dispatcher.dispatch(request)

            // respond
            if (ThreadMap[request.dest.toInt()] == null) {
                val res = Response<Any>("error", "404", "用户未上线", body = Unit)
                IOUtil.write(Parser.encode(res), socket.getOutputStream())
            } else {
                response.src = request.src
                response.dest = request.dest
                val res = Parser.encode(response)
                log.info("respond {src=${response.src}, dest=${response.dest}, path=${response.path}} in $this")
                val destSocket = ThreadMap[response.dest.toInt()]!!.socket
                IOUtil.write(res, destSocket.getOutputStream())
            }
        }
    }

    private fun exit(request: Request<Any>, socket: Socket): Boolean {
        if (request.path != "/exit") return false

        val response = Response<Any>("success", "200", path = "/exit", body = Unit)
        val json = Parser.encode(response)
        IOUtil.write(json, socket.getOutputStream())

        ThreadMap -= request.src.toInt()
        socket.shutdownOutput()
        socket.close()
        log.info("exit $this, current thread map: $ThreadMap")
        return true
    }

    override fun toString(): String {
        return "ServerThread(id=$id, socket=$socket)"
    }

}