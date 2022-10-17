package com.liaopeixin.lib_mvvm

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import androidx.lifecycle.LifecycleObserver
import androidx.viewbinding.ViewBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.liaopeixin.lib_base.utils.DensityUtil

/**
 * 列表数据处理工具类
 * author：Codyfjm on 2022/5/25
 * email：fengjunming@ysbang.cn
 * company:Ysbang
 * Copyright © 2021 Ysbang. All rights reserved.
 */
open class BaseViewBindingBottomDialog<VB : ViewBinding>(context: Context) :
    BottomSheetDialog(context), LifecycleObserver {
    protected var mContext: Context? = null
    protected var mActivity: Activity? = null
    protected var binding: VB? = null
    protected var mViewModel: BaseViewModel? = null
    protected var rootView: View? = null
    private var mMaxHeight = -1.0f

    protected var mDialogListener: DialogListener? = null

    constructor(context: Context, maxHeight: Float) : this(context) {
        mMaxHeight = maxHeight
    }

    constructor(context: Context,viewModel: BaseViewModel,maxHeight: Float) : this(context) {
        mMaxHeight = maxHeight
        mViewModel = viewModel
    }

    init {
        mContext = context
        mActivity = context as Activity
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.initViewBinding()
        this.setMaxHeight()
        this.initData()
        this.initView()
        this.initEvent()
        this.loadData()
    }

    private fun initViewBinding() {
        try {
            binding =
                ViewBindingCreator.createViewBinding<VB>(javaClass, mActivity?.layoutInflater)!!
            rootView = (if (binding == null) getContentView() else binding!!.root)
            setContentView(rootView!!)
        } catch (e: Exception) {
        }
    }

    override fun setContentView(view: View) {
        super.setContentView(view)
        this.setContentParentViewBackground(view)
        this.setPeekHeight(view)
    }


    protected open fun getContentView(): View? {
        return if (mContext != null) {
            FrameLayout(mContext!!)
        } else {
            null
        }
    }

    private fun setMaxHeight() {
        this.window!!.setLayout(-1, if (mMaxHeight != -1.0f) com.liaopeixin.lib_base.utils.DensityUtil.dip2px(mMaxHeight) else -2)
        this.window!!.setGravity(80)
    }

    private fun setContentParentViewBackground(contentView: View) {
        (contentView.parent as View).setBackgroundResource(R.color.transparent)
    }

    private fun setPeekHeight(view: View) {
        if (mMaxHeight != -1.0f) {
            val dialogBehavior = BottomSheetBehavior.from(view.parent as View)
            dialogBehavior.peekHeight = com.liaopeixin.lib_base.utils.DensityUtil.dip2px(mMaxHeight)
        }
    }

    open fun initData(){}

    open fun initView() {}

    open fun initEvent() {}

    open fun loadData() {}

    protected fun setDialogExpanded() {
        val dialogBehavior = BottomSheetBehavior.from(rootView?.parent as View)
        dialogBehavior.state =
            BottomSheetBehavior.STATE_EXPANDED
    }

    fun setListener(dialogListener: DialogListener) {
        mDialogListener = dialogListener
    }

    interface DialogListener {
        fun onListen(type: Int, item: Any?)
    }

    open fun initViewObservable(){

    }
}