package com.baymax104.chatapp.base.dispatch

import com.baymax104.chatapp.base.ioc.IOC
import com.baymax104.chatapp.base.ioc.PathMapping
import com.baymax104.chatapp.dto.Request
import com.baymax104.chatapp.dto.Response
import com.baymax104.chatapp.service.UserService
import com.baymax104.chatapp.utils.Parser
import com.google.gson.Gson

class Dispatcher(val ioc: IOC) {

    /**
     * 用户通信线程请求分发
     * @param request 请求
     * @return 响应
     */
    fun dispatch(request: Request<Any>): Response<Any> {
        val service = ioc[UserService::class]
        val path = request.path
        val body = Parser.encode(request.body)

        try {
            // 反射获取匹配的方法，解析参数调用
            val methods = service.javaClass.declaredMethods
            for (method in methods) {
                if (!method.isAnnotationPresent(PathMapping::class.java)) {
                    continue
                }
                val annotation = method.getAnnotation(PathMapping::class.java)
                val value = annotation.value
                if (value != path) {
                    continue
                }
                method.isAccessible = true
                val parameterTypes = method.genericParameterTypes
                val res = if (parameterTypes.isEmpty()) {
                    method(service)
                } else {
                    val type = parameterTypes[0]
                    val param = Gson().fromJson<Any>(body, type)
                    method(service, param)
                }
                return Parser.transform<Response<Any>>(res)
            }
            throw Exception("方法不存在")
        } catch (e: Exception) {
            return Response("error", "404", e.message ?: "")
        }
    }

}