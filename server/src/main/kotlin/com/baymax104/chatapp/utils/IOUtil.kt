package com.baymax104.chatapp.utils

import java.io.InputStream
import java.io.InputStreamReader
import java.io.OutputStream
import java.io.OutputStreamWriter

/**
 *@Description
 *@Author John
 *@Date 2023/7/11 15:19
 *@Version 1
 */
object IOUtil {

    fun read(input: InputStream): String {
        val reader = InputStreamReader(input, Charsets.UTF_8)
        val builder = StringBuilder()
        var ch = 0
        while (ch != '\n'.code) {
            ch = reader.read()
            if (ch != '\n'.code) {
                builder.append(ch.toChar())
            }
        }
        return String(builder)
    }

    fun write(str: String, output: OutputStream) {
        val writer = OutputStreamWriter(output, Charsets.UTF_8)
        writer.write("$str\n")
        writer.flush()
    }
}