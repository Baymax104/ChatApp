package com.baymax104.chatapp.entity

import com.baymax104.chatapp.utils.DateDetailFormatter
import java.util.Date

/**
 * Request
 * @author John
 */
class Request<E>() {

    var src: String = "."
    var dest: String = "."
    var timestamp: String = DateDetailFormatter.format(Date())
    var path: String = ""
    var body: E? = null

    constructor(path: String, src: String = ".", dest: String = ".", body: E? = null) : this() {
        this.src = src
        this.dest = dest
        this.path = path
        this.body = body
    }

    override fun toString(): String {
        return "Request(src='$src', dest='$dest', timestamp='$timestamp', path='$path', body=$body)"
    }

}