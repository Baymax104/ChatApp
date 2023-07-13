package com.baymax104.chatapp.exception

import com.baymax104.chatapp.entity.Response

/**
 * 网络异常
 * @author John
 */
class WebException(msg: String) : Exception(msg) {

    var response: Response<Any> = Response()

    constructor(msg: String, response: Response<Any>) : this(msg) {
        this.response = response
    }
}