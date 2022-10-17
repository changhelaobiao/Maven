package com.liaopeixin.lib_mvvm

import android.content.Context
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.viewbinding.ViewBinding

/**
 *
 * author：fengjunming on 2022/4/24
 * email：fengjunming@ysbang.cn
 * company:ysbang
 * remark：
 */
open abstract class BaseViewBindingFragment<VM : BaseViewModel, VB : ViewBinding> : Fragment(),
    IBaseView {
    protected var TAG = javaClass.simpleName
    protected var binding: VB? = null
    protected var viewModel: VM? = null
    protected var rootView: View? = null
    protected var mSpectrumLoadingDialog: SpectrumLoadingDialog? = null
    protected var mContext: Context? = null
    protected var mActivity: FragmentActivity? = null

    fun BaseViewBindingFragment() {
        // Required empty public constructor
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //初始化ViewBinding
        initViewBinding()
        return rootView
    }

    private fun initViewBinding() {
        if (binding == null) {
            binding = ViewBindingCreator.createViewBinding<VB>(javaClass, layoutInflater)!!
            rootView =
                generateContentView(if (binding == null) getContentView() else binding!!.root)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mContext = requireContext()
        mActivity = requireActivity()
        //初始化数据
        initData()
        //初始化ViewModel
        initViewModel()
        //私有的ViewModel与View的契约事件回调逻辑
        registorUIChangeLiveDataCallBack()
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

    private fun getContentView(): View? {
        return null
    }

    private fun generateContentView(contentView: View?): View? {
        return contentView
    }


    fun checkPermissions() {

    }

    private fun registorUIChangeLiveDataCallBack() {
        //加载对话框显示
        viewModel?.getUC()?.showDialogEvent?.observe(viewLifecycleOwner,
            Observer<MutableMap<String, Any>> { map ->
                showLoadingDialog(map["title"] as String, map["canClose"] as Boolean)
            })
        //加载对话框消失
        viewModel?.getUC()?.dismissDialogEvent?.observe(viewLifecycleOwner,
            Observer<Void?> { dismissLoadingDialog() })
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
            mSpectrumLoadingDialog = SpectrumLoadingDialog(requireContext())
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
            mSpectrumLoadingDialog = SpectrumLoadingDialog(requireContext())
        }
        mSpectrumLoadingDialog!!.isCancelable(canClose)
        mSpectrumLoadingDialog!!.setLoadingTitle(title)
        if (isActivityEnable()) {
            mSpectrumLoadingDialog!!.show()
        }
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if (!hidden){
//           LogUtil.d("baseFragment",javaClass.simpleName)
        }
    }

    /**
     * Activity是否已被销毁
     *
     * @return
     */
    private fun isActivityEnable(): Boolean {
        return (!requireActivity()!!.isDestroyed && !requireActivity().isFinishing && isAdded)
    }

    private fun dismissLoadingDialog() {
        if (null != mSpectrumLoadingDialog) {
            mSpectrumLoadingDialog!!.dismiss()
        }
    }


    private fun initViewModel() {
        if (viewModel == null) {
            val modelClass: Class<VM> = ViewModelCreator.createViewModel(javaClass)
            viewModel =
                ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())[modelClass]
        }
        (viewModel as BaseViewModel).bindView(this)
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }

    override fun onDestroy() {
        super.onDestroy()
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

}