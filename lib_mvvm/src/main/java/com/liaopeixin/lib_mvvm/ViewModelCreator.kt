package com.liaopeixin.lib_mvvm

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.ViewModel
import java.lang.Exception
import java.lang.reflect.InvocationTargetException
import java.lang.reflect.ParameterizedType

/**
 * author：fengjunming on 2022/4/25
 * email：fengjunming@ysbang.cn
 * company:ysbang
 * remark：
 * 通过反射初始化ViewModel
 */
object ViewModelCreator {
    @SuppressLint("NewApi")
    @SuppressWarnings("unchecked")
    fun <VM : ViewModel?> createViewModel(
        targetClass: Class<*>): Class<VM> {
        var modelClass: Class<VM> = BaseViewModel::class.java as Class<VM>
        val type = targetClass.genericSuperclass
        if (type is ParameterizedType) {
            try {
                val types = type.actualTypeArguments
                for (type1 in types) {
                    if (type1.typeName
                            .endsWith("ViewModel")
                    ) {
                        modelClass = type1 as Class<VM>
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                if (e is InvocationTargetException){
                    Log.e("ViewModelCreator", (e).targetException.toString())
                }else{
                    Log.e("ViewModelCreator", e.message.toString())
                }
            }
        }
        return modelClass
    }
}