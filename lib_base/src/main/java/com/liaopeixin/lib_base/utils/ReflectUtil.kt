package com.liaopeixin.lib_base.utils

/**
 *
 * author：fengjunming on 2022/4/28
 * email：fengjunming@ysbang.cn
 * company:ysbang
 * remark：
 */
class ReflectUtil {

    companion object {
        /**
         * 获取当前调用的方法
         */
        @JvmStatic
        fun getCurrentMethod(className: String): String {
            var method = ""
            try {
                val clazzes = Thread.currentThread().stackTrace
                for (clazz in clazzes) {
                    if (clazz.className.contains(className)) {
                        method = clazz.methodName
                        break
                    }
                }
            } catch (e: Exception) {

            }
            return method
        }

        /**
         * 获取上一级的方法
         */
        @JvmStatic
        fun getSuperV1Method(className: String): String {
            var method = ""
            try {
                val clazzes = Thread.currentThread().stackTrace
                for (i in clazzes.indices) {
                    if (clazzes[i].className.contains(className)) {
                        if (i < clazzes.size) {
                            method = clazzes[i + 1].methodName
                        } else {
                            method = clazzes[i].methodName
                        }
                        break
                    }
                }
            } catch (e: Exception) {

            }
            return method
        }
    }
}