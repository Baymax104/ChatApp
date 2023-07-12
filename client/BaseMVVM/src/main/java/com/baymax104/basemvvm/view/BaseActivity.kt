package com.baymax104.basemvvm.view

import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.util.forEach
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.baymax104.basemvvm.R
import com.jaeger.library.StatusBarUtil

/**
 * 该类为项目中的所有 Activity 父类，包含了通用的初始化流程，同时重写了一些原生通用方法.
 * 初始化流程如下
 *
 *  * 使用 [BaseActivity.configBinding] 方法配置的布局ID创建视图的 ViewDataBinding 对象并进行设置
 *  * 若布局中包含自定义 Toolbar ，则替换默认 Actionbar
 *  * 将 ViewDataBinding 对象传入 [BaseActivity.configUIComponent]中，由子类设置UI控件
 *  * 遍历配置的绑定参数，将其绑定到视图中
 *
 * @param <B> 子类 DataBinding 类型
 * @author John
 */
abstract class BaseActivity<B : ViewDataBinding> : AppCompatActivity() {

    private var mBinding: ViewDataBinding? = null

    protected lateinit var activity: AppCompatActivity

    protected class WindowConfig {
        var back = false
        var lightTheme = true
        var menuId: Int? = null
        var extentView: View? = null
        companion object {
            inline fun build(block: WindowConfig.() -> Unit = {}) = WindowConfig().apply(block)
        }
    }


    /**
     * 该方法必须由子类重写，返回子类绑定到视图中的参数.
     * @return ViewConfig 封装了子类需要绑定到视图中的参数
     */
    protected abstract fun configBinding(): ViewConfig

    /**
     * 该方法由子类选择性重写，子类可以使用 binding 参数获取视图中的UI控件进行设置.
     * @param binding 子类 DataBinding 对象，由 BaseActivity 提供
     */
    protected open fun configUIComponent(binding: B) {
    }

    /**
     * 当子类需要配置Toolbar时，重写该方法.
     * @return Toolbar配置
     */
    protected open fun configWindow(): WindowConfig = WindowConfig.build()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity = this
        ActivityStack.push(this)
        val config = configBinding()
        val binding: B = DataBindingUtil.setContentView(this, config.layout)
        binding.lifecycleOwner = this
        applyToolbar(binding.root)
        configUIComponent(binding)
        config.params.forEach { key, value -> binding.setVariable(key, value) }
        mBinding = binding
    }

    private fun applyToolbar(root: View) {
        val toolbar = root.findViewById<Toolbar>(R.id.base_toolbar) ?: return
        setSupportActionBar(toolbar)
        val config = configWindow()
        supportActionBar?.apply {
            setDisplayShowTitleEnabled(false)
            setDisplayHomeAsUpEnabled(config.back)
            if (config.back) {
                setHomeAsUpIndicator(R.drawable.arrow_left)
            }
        }
        if (config.lightTheme) {
            StatusBarUtil.setLightMode(this)
        } else {
            StatusBarUtil.setDarkMode(this)
        }
        if (config.extentView != null) {
            if (config.extentView!!.background != null) {
                val background = config.extentView!!.background!!
                if (background is ColorDrawable) {
                    StatusBarUtil.setColor(this, background.color)
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val config = configWindow()
        if (config.menuId != null) {
            menuInflater.inflate(config.menuId!!, menu)
            return true
        }
        return false
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            ActivityStack.pop()
        }
        return true
    }

    override fun onDestroy() {
        super.onDestroy()
        mBinding!!.unbind()
        mBinding = null
    }
}