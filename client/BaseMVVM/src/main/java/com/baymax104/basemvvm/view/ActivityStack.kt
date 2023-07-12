package com.baymax104.basemvvm.view

import androidx.appcompat.app.AppCompatActivity
import java.util.Stack

/**
 * Activity管理栈
 * @author John
 */
object ActivityStack {

    private val stack: Stack<AppCompatActivity> = Stack()

    fun push(activity: AppCompatActivity) {
        stack.push(activity)
    }

    fun pop() {
        stack.pop().finish()
    }

    fun finishAll() {
        val size = stack.size
        repeat(size) { pop() }
    }
}