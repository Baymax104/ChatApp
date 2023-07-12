package com.baymax104.chatapp.dto

import com.baymax104.chatapp.utils.DateDetailFormatter
import java.util.*

/**
 * Response
 * @author John
 */
class Response<E>() {

    var status: String = ""
    var code: String = ""
    var message: String = ""
    var src: String = "."
    var dest: String = "."
    var timestamp: String = DateDetailFormatter.format(Date())
    var path: String = ""
    var body: E? = null

    constructor(status: String, code: String, message: String = "", body: E? = null) : this() {
        this.status = status
        this.code = code
        this.message = message
        this.body = body
    }

    override fun toString(): String {
        return "Response(status='$status', code='$code', src='$src', dest='$dest', timestamp='$timestamp', path='$path', body=$body)"
    }

}