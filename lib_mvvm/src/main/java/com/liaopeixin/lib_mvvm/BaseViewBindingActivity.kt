package com.liaopeixin.lib_mvvm

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.content.res.Resources
import android.graphics.Color
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.*
import android.widget.FrameLayout
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.viewbinding.ViewBinding


/**
 *
 * author：fengjunming on 2022/4/24
 * email：fengjunming@ysbang.cn
 * company:ysbang
 * remark：
 * 1.绑定基础的base view model
 * 2.绑定对应的view binding
 * 3.loading状态的显示和隐藏
 */
open abstract class BaseViewBindingActivity<VM : BaseViewModel, VB : ViewBinding> : ITaskActivity(),
    IBaseView {
    protected lateinit var mContext: Context
    protected lateinit var mActivity: Activity
    protected var binding: VB? = null
    protected var viewModel: VM? = null
    protected var rootView: View? = null
    protected var mSpectrumLoadingDialog: SpectrumLoadingDialog? = null
    private var tv_config_view: TextView? = null
    var popupWindowBgView: View? = null
    var isPopupWindowBgAdded = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mContext = this
        mActivity = this
        initScreen()
        //初始化ViewBinding
        initViewBinding()
        //初始化ViewModel
        initViewModel()
        //私有的ViewModel与View的契约事件回调逻辑
        registorUIChangeLiveDataCallBack()
        //添加环境配置
        addConfigView()
        //初始化数据
        initData()
        //初始化组件
        initView()
        //初始化响应事件
        initEvent()
        //配置权限
        checkPermissions()
        //初始化数据观察者
        initViewObservable()
        //加载数据
        loadData()
    }


    //注册ViewModel与View的契约UI回调事件
    private fun registorUIChangeLiveDataCallBack() {
        //加载对话框显示
        viewModel?.getUC()?.showDialogEvent?.observe(this,
            Observer<MutableMap<String, Any>> { map ->
                showLoadingDialog(
                    map["title"] as String,
                    map["canClose"] as Boolean
                )
            })
        //加载对话框消失
        viewModel?.getUC()?.dismissDialogEvent?.observe(this,
            Observer<Void?> { dismissLoadingDialog() })
        //关闭界面
        viewModel?.getUC()?.finishEvent?.observe(this,
            Observer<Void?> { finish() })
        //关闭上一层
        viewModel?.getUC()?.onBackPressedEvent?.observe(this,
            Observer<Void?> { onBackPressed() })
    }

    private fun showLoadingDialog() {
        showLoadingDialog("")
    }

    private fun showLoadingDialog(title: String?) {
        var title = title
        if (TextUtils.isEmpty(title)) {
            title = getString(R.string.app_loading)
        }
        if (null == mSpectrumLoadingDialog) {
            mSpectrumLoadingDialog = SpectrumLoadingDialog(this)
        }
        mSpectrumLoadingDialog?.setLoadingTitle(title)
        mSpectrumLoadingDialog?.show()
    }

    open fun showLoadingDialog(title: String, canClose: Boolean) {
        var title = title
        if (TextUtils.isEmpty(title)) {
            title = getString(R.string.app_loading)
        }
        if (null == mSpectrumLoadingDialog) {
            mSpectrumLoadingDialog = SpectrumLoadingDialog(this)
        }
        mSpectrumLoadingDialog!!.isCancelable(canClose)
        mSpectrumLoadingDialog!!.setLoadingTitle(title)
        if (isActivityEnable()) {
            mSpectrumLoadingDialog!!.show()
        }
    }

    /**
     * Activity是否已被销毁
     *
     * @return
     */
    open fun isActivityEnable(): Boolean {
        return !(isDestroyed || isFinishing)
    }

    open fun dismissLoadingDialog() {
        if (null != mSpectrumLoadingDialog) {
            mSpectrumLoadingDialog!!.dismiss()
        }
    }


    private fun initScreen() {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setTranslucentStatus()
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
    }

    private fun initViewModel() {
        if (viewModel == null) {
            val modelClass: Class<VM> = ViewModelCreator.createViewModel(javaClass)
            viewModel =
                ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())[modelClass]
        }
        (viewModel as BaseViewModel).bindView(this)
    }


    private fun initViewBinding() {
        try {
            binding = ViewBindingCreator.createViewBinding<VB>(javaClass, layoutInflater)!!
            rootView =
                generateContentView(if (binding == null) getContentView() else binding!!.root)
            setContentView(rootView)
        } catch (e: Exception) {
        }


    }

    protected open fun getContentView(): View? {
        return FrameLayout(this)
    }

    protected open fun generateContentView(contentView: View?): View? {
        return contentView
    }

    override fun initData() {

    }

    override fun initView() {

    }

    override fun initEvent() {

    }

    override fun initViewObservable() {

    }

    override fun loadData() {

    }

    fun checkPermissions() {

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onDestroy() {
        super.onDestroy()
        dismissLoadingDialog()
        com.liaopeixin.lib_base.utils.DialogHelper.dismissAllDialog()
        mSpectrumLoadingDialog = null
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        if (newConfig.fontScale != 1f) //非默认值
            resources
        super.onConfigurationChanged(newConfig)
    }

    override fun getResources(): Resources? {
        val res = super.getResources()
        if (res.configuration.fontScale != 1f) { //非默认值
            val newConfig = Configuration()
            newConfig.setToDefaults() //设置默认
            res.updateConfiguration(newConfig, res.displayMetrics)
        }
        return res
    }

    open fun setViewVisibility(view: View?, isVisible: Boolean) {
        if (null != view) {
            view.visibility = if (isVisible) View.VISIBLE else View.GONE
        }
    }

    /**
     * 设置状态栏透明
     */
    protected open fun setTranslucentStatus() {
        // 第二个参数是状态栏色值, 第三个是兼容5.0到6.0之间状态栏颜色字体只能是白色。
        // 如果沉浸的颜色与状态栏颜色冲突, 设置一层浅色对比能显示出状态栏字体
        // 如果您的项目是6.0以上机型, 推荐使用两个参数的setUseStatusBarColor。
        com.liaopeixin.lib_base.utils.StatusUtil.setUseStatusBarColor(this, Color.TRANSPARENT, Color.parseColor("#ffffff"))

        //设置状态栏和导航栏透明
        com.liaopeixin.lib_base.utils.StatusUtil.setNavAndStatusBarTransparent(this)

        // 第二个参数是是否沉浸,第三个参数是状态栏字体是否为黑色
        com.liaopeixin.lib_base.utils.StatusUtil.setSystemStatus(this, true, true)
    }

    /**
     * 添加环境配置信息展示
     */
    private fun addConfigView() {
        val configUtil = com.liaopeixin.lib_base.utils.ConfigUtil.getInstance()
        val buildConfigUtil = com.liaopeixin.lib_base.utils.BuildConfigUtil.getInstance()
        if (!(configUtil.isPro && !buildConfigUtil.isDebug)) {
            tv_config_view = TextView(this)
            val content =
                "SN:" + buildConfigUtil.gitBuildCode + ";" + buildConfigUtil.gitHashCode  + ";"+ BaseUIHelper.userInfo +" " //git build version
            val params = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            val versionInfo = content + String.format(
                "%s_%s_%s_%s_%s",
                "ysb",
                if (configUtil.appEve!=null&&configUtil.appEve.startsWith("http")) "test" else configUtil.appEve,
                buildConfigUtil.buildType,
                com.liaopeixin.lib_base.utils.AppUtils.getVersionName(this.application),
                configUtil.url
            )
            tv_config_view?.text = versionInfo
            tv_config_view?.textSize = 9f
            tv_config_view?.setTextColor(-0x77ffff78)
            tv_config_view?.gravity = Gravity.CENTER_HORIZONTAL
            tv_config_view?.setPadding(0, com.liaopeixin.lib_base.utils.DensityUtil.dip2px(this, 35f), 0, 0)
            addContentView(tv_config_view, params)
        }
    }

    open fun getConfigView(): View? {
        return tv_config_view
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        PermissionUtil.onRequestPermissionsResult(this, requestCode, permissions, grantResults)
    }


    open fun setConfigViewText() {
        val configUtil = com.liaopeixin.lib_base.utils.ConfigUtil.getInstance()
        val versionInfo = String.format(
            "%s_%s_%s_%s_%s",
            "ysb",
            if (configUtil.appEve.startsWith("http")) "test" else configUtil.appEve,
            com.liaopeixin.lib_base.utils.BuildConfigUtil.getInstance().buildType,
            com.liaopeixin.lib_base.utils.AppUtils.getVersionName(this.application),
            configUtil.url
        )
        tv_config_view?.setText(versionInfo)
    }

    /**
     * 显示弹窗半透明背景
     */
    open fun showPopupWindowBg() {
        try {
            if (!isPopupWindowBgAdded) {
                popupWindowBgView = View.inflate(this, R.layout.base_popupwindow_outside_bg, null)
                window.addContentView(
                    popupWindowBgView,
                    ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                    )
                )
                isPopupWindowBgAdded = true
                Log.i("PopupWindowBgAdded", "added")
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        Log.i("PopupWindowBgAdded", "show")
        popupWindowBgView?.visibility = View.VISIBLE
    }

    /**
     * 隐藏弹窗半透明背景
     */
    open fun hidePopupWindowBg() {
        if (isPopupWindowBgAdded && popupWindowBgView?.getVisibility() == View.VISIBLE) {
            popupWindowBgView?.visibility = View.GONE
        }
    }

    //1.触摸事件接口
    interface MyOnTouchListener {
        fun onTouch(ev: MotionEvent?): Boolean
    }

    //2. 保存MyOnTouchListener接口的列表
    protected var onTouchListeners = ArrayList<MyOnTouchListener>()

    //3.分发触摸事件给所有注册了MyOnTouchListener的接口
    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        for (listener in onTouchListeners) {
            listener.onTouch(ev)
        }
        return super.dispatchTouchEvent(ev)
    }

    //4.提供给Fragment通过getActivity()方法来注册自己的触摸事件的方法
    open fun registerMyOnTouchListener(myOnTouchListener: MyOnTouchListener) {
        onTouchListeners.add(myOnTouchListener)
    }

    //5.提供给Fragment通过getActivity()方法来注销自己的触摸事件的方法
    open fun unregisterMyOnTouchListener(myOnTouchListener: MyOnTouchListener) {
        onTouchListeners.remove(myOnTouchListener)
    }
}