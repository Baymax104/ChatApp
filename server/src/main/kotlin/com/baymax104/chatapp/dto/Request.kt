package com.baymax104.chatapp.dto

import com.baymax104.chatapp.utils.DateDetailFormatter
import java.util.*

/**
 * Message
 * @author John
 */
class Request<T>() {

    var src: String = "."
    var dest: String = "."
    var timestamp: String = DateDetailFormatter.format(Date())
    var path: String = ""
    var body: T? = null

    constructor(path: String, src: String = ".", dest: String = ".", body: T? = null) : this() {
        this.src = src
        this.dest = dest
        this.path = path
        this.body = body
    }

    override fun toString(): String {
        return "Request(src='$src', dest='$dest', timestamp='$timestamp', path='$path', body=$body)"
    }

}