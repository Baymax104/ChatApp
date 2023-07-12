package com.baymax104.basemvvm.web

/**
 * 网络生命周期接口，具有Start和Finish两个周期
 */
interface NetLifeCycle {

    fun onStart()

    fun onFinish()
}

object NoNetLifeCycle : NetLifeCycle {
    override fun onStart() {
    }

    override fun onFinish() {
    }
}