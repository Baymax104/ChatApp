package com.baymax104.basemvvm.view

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.util.forEach
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment

/**
 * 该类为项目中的所有 Activity 父类，包含了通用的初始化流程，同时重写了一些原生通用方法.
 * @see BaseActivity
 *
 * @param <B> Fragment 子类的 DataBinding 类型
 * @author John
</B> */
abstract class BaseFragment<B : ViewDataBinding> : Fragment() {

    private var mBinding: ViewDataBinding? = null

    protected lateinit var activity: AppCompatActivity

    /**
     * @see BaseActivity.configBinding
     */
    protected abstract fun configBinding(): ViewConfig

    /**
     * 对于 Fragment ，需要设置的控件较少，子类可选择性重写.
     * @see BaseActivity.configUIComponent
     */
    protected open fun initUIComponent(binding: B) {
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        activity = context as AppCompatActivity
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val config = configBinding()
        val binding: B = DataBindingUtil.inflate(inflater, config.layout, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        initUIComponent(binding)
        config.params.forEach { key, value -> binding.setVariable(key, value) }
        mBinding = binding
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mBinding!!.unbind()
        mBinding = null
    }
}