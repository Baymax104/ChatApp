package com.baymax104.chatapp.utils.binding

import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.baymax104.chatapp.utils.DateFormatter
import java.util.*

/**
 *@Description
 *@Author John
 *@email
 *@Date 2023/6/16 17:21
 *@Version 1
 */
object FormatAdapter {

    @JvmStatic
    @BindingAdapter("text_year_month")
    fun TextView.yearMonth(date: Date?) {
        text = date?.let {
            val string = DateFormatter.format(it)
            string.substring(0..string.length - 4)
        } ?: ""
    }

    @JvmStatic
    @BindingAdapter("text_day")
    fun TextView.day(date: Date?) {
        text = date?.let {
            val string = DateFormatter.format(it)
            string.substring(string.length - 2 until string.length)
        } ?: ""
    }

    @JvmStatic
    @BindingAdapter("text_date")
    fun TextView.date(date: Date?) {
        text = date?.let { DateFormatter.format(date) } ?: ""
    }


}