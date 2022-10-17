package com.liaopeixin.lib_mvvm

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import androidx.viewbinding.ViewBinding
import java.lang.Exception
import java.lang.reflect.InvocationTargetException
import java.lang.reflect.ParameterizedType

/**
 * author：fengjunming on 2022/4/25
 * email：fengjunming@ysbang.cn
 * company:ysbang
 * remark：
 * 通过反射初始化ViewBinding
 */
object ViewBindingCreator {
    @SuppressLint("NewApi")
    @SuppressWarnings("unchecked")
    fun <VB : ViewBinding?> createViewBinding(
        targetClass: Class<*>,
        layoutInflater: LayoutInflater?
    ): VB? {
        val type = targetClass.genericSuperclass
        if (type is ParameterizedType) {
            try {
                val types = type.actualTypeArguments
                for (type1 in types) {
                    if (type1.typeName
                            .endsWith("Binding")
                    ) {
                        val method = (type1 as Class<VB>).getMethod(
                            "inflate",
                            LayoutInflater::class.java
                        )
                        return method.invoke(null, layoutInflater) as VB
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()

                if (e is InvocationTargetException){
                    Log.e("ViewBindingCreator", (e).targetException.toString())
                }else{
                    Log.e("ViewBindingCreator", e.message.toString())
                }
            }
        }
        return null
    }
}