package com.baymax104.chatapp.utils

import java.text.SimpleDateFormat
import java.util.Locale


/**
 * 日期-字符串转换器，日期格式为yyyy-MM-dd
 */
val DateFormatter = SimpleDateFormat("yyyy-MM-dd", Locale.CHINA)

/**
 * 日期-字符串转换器，日期格式为yyyyMMdd_HHmmss
 */
val DateDetailFormatter = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.CHINA)
