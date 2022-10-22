package com.liaopeixin.maven

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.liaopeixin.lib_mvvm.BaseViewModel
import com.liaopeixin.lib_network.MessageManager
import com.liaopeixin.lib_network.base.*
import com.liaopeixin.lib_utils.ToastUtil

/**
 * author : toby
 * e-mail : 16620129640@163.com
 * time : 2022/10/22
 * desc :
 */
class TestNetworkViewModel : BaseViewModel() {

    var mDataList: MutableLiveData<List<MessageResp.ArticleData>> =
        MutableLiveData<List<MessageResp.ArticleData>>()

    //    var mManager: MessageManager? = MessageManager(mContext)
    private val mManager by lazy {
        MessageManager(mContext)
    }

    fun getData() {
        mManager?.getData1(object : HttpResponse.ResponseListener {
            override fun onResponse(httpResponse: HttpResponse?) {
                if (httpResponse != null) {
                    val request: HttpRequest = httpResponse.requestInfo
                    if (request != null) {
                        val requestId: Int = request.requestId
                        if (requestId == 1000) {
                            val messageResp: MessageResp = httpResponse.getData() as MessageResp
                            if (messageResp == null) {
                                handleResponseError(httpResponse)
                                return
                            }
                            if (messageResp != null) {
                                val dataList: List<MessageResp.ArticleData> = messageResp.getData()
                                if (dataList != null && dataList.isNotEmpty()) {
                                    mDataList.value = dataList
                                }
                            }
                        }
                    }
                }
            }
        })
    }

    fun handleResponseError(response: HttpResponse?) {
        if (response != null) {
            handleError(mContext, response, 0)
        }
    }

    fun handleError(context: Context, response: HttpResponse, otherErrMsgResId: Int) {
        var text: CharSequence
        val responseData: Any = response.data
        text = if (responseData is BaseResp) {
            val parsed: BaseResp = responseData as BaseResp
            parsed.message
        } else {
            response.message
        }
        if (text == null) {
            when (response.code) {
                HttpErrorCode.NO_NETWORK -> {
                    text = context.getString(R.string.network_un_enable)
                }
                HttpErrorCode.ACTION_FAILED -> {
                    text = context.getString(R.string.action_failed)
                }
                HttpErrorCode.NETWORK_BROKEN -> {
                    text = context.getString(R.string.network_un_enable)
                }
                HttpErrorCode.NETWORK_EXCEPTION -> {
                    text = context.getString(R.string.network_exception)
                }
                HttpErrorCode.NETWORK_TIMEOUT -> {
                    text = context.getString(R.string.network_timeout)
                }
            }
        }
        if (text == null && otherErrMsgResId != 0) {
            text = context.getString(otherErrMsgResId)
        }
        if (text != null) {
            ToastUtil.showLongToast(context, text)
        }
    }

}