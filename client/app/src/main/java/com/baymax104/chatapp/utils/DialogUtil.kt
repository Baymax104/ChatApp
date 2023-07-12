package com.baymax104.chatapp.utils

import android.content.Context
import android.view.View
import com.baymax104.chatapp.view.NetLoadingView
import com.lxj.xpopup.XPopup
import com.lxj.xpopup.core.AttachPopupView
import com.lxj.xpopup.core.BasePopupView
import com.lxj.xpopup.impl.LoadingPopupView
import java.util.*
import kotlin.reflect.KClass


/**
 * 反射调用构造器Constructor(Context context)创建XPopup对话框并显示.
 * @param context context
 * @param type 对话框Class对象
 * @param builder 对话框builder对象
 * @param <T> 对话框类型
 */
@JvmOverloads
@Suppress("Unchecked_Cast")
fun <T : BasePopupView> Context.create(
    type: KClass<T>,
    builder: XPopup.Builder? = null
): T {
    val mBuilder = builder ?: XPopup.Builder(this)
    val constructor = type.java.getConstructor(Context::class.java)
    val dialog = constructor.newInstance(this)
    return mBuilder.asCustom(dialog) as T
}

/**
 * 创建普通Loading对话框.
 * @param context Context
 * @return 对话框对象
 */
fun Context.createLoading(): LoadingPopupView {
    return XPopup.Builder(this)
        .dismissOnTouchOutside(false)
        .asLoading()
}


/**
 * 创建依附布局的对话框.
 * @param T [AttachPopupView]子类类型
 * @param context Context
 * @param cl [AttachPopupView]子类Class对象
 * @param view 对话框依附的布局
 * @return 依附对话框对象
 */
@Suppress("Unchecked_Cast")
fun <T : AttachPopupView> Context.createAttachDialog(cl: KClass<T>, view: View): T {
    val builder = XPopup.Builder(this)
        .hasShadowBg(false)
        .isDestroyOnDismiss(true)
        .isLightStatusBar(true)
        .atView(view)
    val constructor = cl.java.getConstructor(Context::class.java)
    val dialog = constructor.newInstance(this)
    return builder.asCustom(dialog) as T
}

/**
 * 创建可观察网络生命周期的加载对话框.
 * @see NetLoadingView
 * @return 加载对话框对象
 */
fun Context.createNetLoading(): NetLoadingView {
    val dialog = NetLoadingView(this)
    return XPopup.Builder(this)
        .dismissOnTouchOutside(false)
        .asCustom(dialog) as NetLoadingView
}


