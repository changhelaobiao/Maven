package com.liaopeixin.lib_mvvm


/**
 *
 * author：fengjunming on 2022/4/27
 * email：fengjunming@ysbang.cn
 * company:ysbang
 * remark：
 */
data class BaseCallbackData<T : Any>(
    var state: CallbackState = CallbackState.SUCCESS,
    var method: String = "",
    var data: T? = null,
    var msg: String? = ""
) : BaseModel() {
    enum class CallbackState {
        SUCCESS, ERROR
    }
}