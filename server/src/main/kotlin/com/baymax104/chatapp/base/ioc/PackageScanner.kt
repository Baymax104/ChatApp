package com.baymax104.chatapp.base.ioc

import cn.hutool.core.io.file.PathUtil
import java.lang.reflect.Modifier
import java.nio.file.Files
import java.nio.file.Paths
import kotlin.io.path.isRegularFile
import kotlin.reflect.KClass

object PackageScanner {

    fun scan(packageName: String, annotationClass: KClass<out Annotation>): List<Class<*>> {
        val classes = mutableListOf<Class<*>>()
        val path = packageName.replace(".", "/")
        val loader = Thread.currentThread().contextClassLoader

        try {
            val url = loader.getResource(path)!!
            val classPath = Paths.get(url.toURI())
            Files.walk(classPath)
                .filter { it.isRegularFile() }
                .forEach {
                    val className = PathUtil.getName(it).replace(".class", "")
                    val clazz = Class.forName("$packageName.$className")
                    if (!clazz.isInterface && !Modifier.isAbstract(clazz.modifiers)
                        && clazz.isAnnotationPresent(annotationClass.java)
                    ) {
                        classes += clazz
                    }
                }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return classes
    }
}