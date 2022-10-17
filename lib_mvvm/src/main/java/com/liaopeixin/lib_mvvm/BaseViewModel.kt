package com.liaopeixin.lib_mvvm

import android.app.Activity
import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel

/**
 *
 * author：fengjunming on 2022/4/24
 * email：fengjunming@ysbang.cn
 * company:ysbang
 * remark：
 * base viewmodel:
 * 1.tag
 * 2.绑定生命周期
 * 3.显示loading
 * 4.接口回调状态回调
 * 5.基础属性：Gson
 */
open class BaseViewModel : ViewModel(), BaseLifecycle {
    var TAG: String = this.javaClass.name
    lateinit var mActivity: Activity
    lateinit var mContext:Context
    private var uc: UIChangeLiveData? = null


    fun bindView(activity: FragmentActivity) {
        //注册生命周期事件
        activity.lifecycle.addObserver(this)
        mActivity = activity
        mContext = activity
    }

    fun bindView(fragment: Fragment) {
        fragment.activity?.lifecycle?.addObserver(this)
        mActivity = fragment.requireActivity()
        mContext = fragment.requireContext()
    }


    override fun onAny(owner: LifecycleOwner, event: Lifecycle.Event) {
//        LogUtil.i(TAG, "----------onAny-----------")
    }

    override fun onCreate() {
//        LogUtil.i(TAG, "----------onCreate-----------")
    }

    override fun onStart() {
//        LogUtil.i(TAG, "----------onStart-----------")
    }

    override fun onResume() {
//        LogUtil.i(TAG, "----------onResume-----------")
    }

    override fun onPause() {
//        LogUtil.i(TAG, "----------onPause-----------")
    }

    override fun onStop() {
//        LogUtil.i(TAG, "----------onStop-----------")
    }

    override fun onDestroy() {
//        LogUtil.i(TAG, "----------onDestroy-----------")
    }

    fun getUC(): UIChangeLiveData? {
        if (uc == null) {
            uc = UIChangeLiveData()
        }
        return uc
    }

    /**
     * loading
     */
    fun showLoadingDialog() {
        showLoadingDialog("加载中...")
    }

    //显示loading
    fun showLoadingDialog(title: String) {
        var map = HashMap<String, Any>()
        map["title"] = title
        map["canClose"] = true
        uc?.showDialogEvent?.postValue(map)
    }

    fun showLoadingDialog(title: String, canClose: Boolean) {
        var map = HashMap<String, Any>()
        map["title"] = title
        map["canClose"] = canClose
        uc?.showDialogEvent?.postValue(map)
    }

    //隐藏loading
    fun dismissLoadingDialog() {
        uc?.dismissDialogEvent?.call()
    }

    //关闭页面
    fun finish() {
        uc?.finishEvent?.call()
    }

    fun onBackPressed() {
        uc?.onBackPressedEvent?.call()
    }

    class UIChangeLiveData : SingleLiveEvent<Any>() {
        //显示加载loading
        var showDialogEvent: SingleLiveEvent<MutableMap<String, Any>> = SingleLiveEvent()
            get() = createLiveData(field).also { field = it }
            private set
        //取消加载loading
        var dismissDialogEvent: SingleLiveEvent<Void> = SingleLiveEvent()
            get() = createLiveData(field).also { field = it }
            private set
        var startActivityEvent: SingleLiveEvent<MutableMap<String, Any>> = SingleLiveEvent()
            get() = createLiveData(field).also { field = it }
            private set
        var startContainerActivityEvent: SingleLiveEvent<MutableMap<String, Any>> =
            SingleLiveEvent()
            get() = createLiveData(field).also { field = it }
            private set
        var finishEvent: SingleLiveEvent<Void> = SingleLiveEvent()
            get() = createLiveData(field).also { field = it }
            private set
        var onBackPressedEvent: SingleLiveEvent<Void> = SingleLiveEvent()
            get() = createLiveData(field).also { field = it }
            private set
        //接口请求回调
        var callBackEvent: SingleLiveEvent<BaseCallbackData<Any>> = SingleLiveEvent()
            get() = createLiveData(field).also { field = it }
            private set


        private fun <T> createLiveData(liveData: SingleLiveEvent<T>): SingleLiveEvent<T> {
            var liveData = liveData
            if (liveData == null) {
                liveData = SingleLiveEvent()
            }
            return liveData
        }


    }
}