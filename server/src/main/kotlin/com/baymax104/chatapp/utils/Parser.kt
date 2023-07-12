package com.baymax104.chatapp.utils

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


/**
 * Json序列化
 */
object Parser {

    val gson = Gson()

    /**
     * Json序列化
     * @param T 任意对象类型
     * @param obj 任意对象
     * @return Json字符串
     */
    inline fun <reified T> encode(obj: T): String {
        val typeToken = object : TypeToken<T>() {}.type
        return gson.toJson(obj, typeToken)
    }

    /**
     * Json反序列化
     * @param T 任意对象类型
     * @param json Json字符串
     * @return 对象实例
     */
    inline fun <reified T> decode(json: String): T {
        val typeToken = object : TypeToken<T>() {}.type
        return gson.fromJson(json, typeToken)
    }

    /**
     * Json实现类型转换
     * @param T 转换目标对象类型
     * @param obj 转换对象
     * @return 目标对象
     */
    inline fun <reified T> transform(obj: Any?): T {
        return decode(encode(obj))
    }

}
