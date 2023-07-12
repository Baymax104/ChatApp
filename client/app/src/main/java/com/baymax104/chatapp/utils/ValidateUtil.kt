package com.baymax104.chatapp.utils

import java.util.regex.Pattern

/**
 * 验证工具类
 */
object ValidateUtil {

    /**
     * 验证密码格式, 6-15位英文字母或数字的组合
     * @param password 密码字符串
     * @return true/false
     */
    @JvmStatic
    fun validatePassword(password: String?): Boolean {
        if (password == null) {
            return false
        }
        // 6-15位英文字母与数字的组合
        val pattern = "^[a-zA-Z\\d]+$"
        val match = Pattern.matches(pattern, password)
        val length = password.length in (6..15)
        return match && length
    }

    /**
     * 验证用户名格式，2-12个中英文或数字的组合
     * @param username 用户名字符串
     * @return true/false
     */
    @JvmStatic
    fun validateUsername(username: String?): Boolean {
        if (username == null) {
            return false
        }
        // 2-12个中英文或数字的组合
        val pattern = "^[a-zA-Z\\d\\u4e00-\\u9fa5]+$"
        val isMatch = Pattern.matches(pattern, username)
        val length = username.length
        val lengthValidity = length in (2..12)
        return isMatch && lengthValidity
    }

    /**
     * 验证账号格式，2-12个中英文或数字的组合
     * @param account
     * @return true/false
     */
    @JvmStatic
    fun validateAccount(account: String?): Boolean {
        if (account == null) return false
        // 2-12个中英文或数字的组合
        val pattern = "^[a-zA-Z\\d\\u4e00-\\u9fa5]+$"
        val isMatch = Pattern.matches(pattern, account)
        val length = account.length
        val lengthValidity = length in (2..12)
        return isMatch && lengthValidity
    }

}