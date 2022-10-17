package com.liaopeixin.lib_mvvm

import android.app.Activity
import android.content.Context
import android.text.TextUtils
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import java.util.*

/**
 * author：Codyfjm on 2022/5/25
 * email：fengjunming@ysbang.cn
 * company:Ysbang
 * Copyright © 2021 Ysbang. All rights reserved.
 */
open abstract class BaseViewBindingAdapter<T : Any, VB : ViewBinding>(
    context: Context?,
    list: MutableList<T>?
) :
    RecyclerView.Adapter<BaseViewBindingHolder>() {

    // 普通的item ViewType
    private val TYPE_ITEM = 1

    // 空布局的ViewType
    private val TYPE_EMPTY = 2


    // 是否显示空布局，默认不显示
    private var showEmptyView = false

    protected var mList: MutableList<T>? = ArrayList()

    private var emptyView: View? = null
    protected var mContext: Context? = null
    protected var mActivity: Activity? = null
    protected var mOnItemClickListener: OnItemClickListener? = null
    protected var rootView: View? = null
    protected var viewModel: BaseViewModel? = null
    protected var mSpectrumLoadingDialog: SpectrumLoadingDialog? = null

    init {
        mList = list
        mContext = context
        mActivity = context as Activity
    }

    constructor(context: Context?, viewModel: BaseViewModel, list: MutableList<T>?) : this(
        context,
        list
    ) {
        this.viewModel = viewModel
    }

    open fun getDatas(): MutableList<T>? {
        return mList
    }

    open fun setData(list: MutableList<T>?) {
        mList = list
    }

    open fun addData(list: MutableList<T>?) {
        if (mList != null && list != null) {
            mList!!.addAll(list)
        }
    }

    open fun clear() {
        mList!!.clear()
        notifyDataSetChanged()
    }

    open fun getContext(): Context? {
        return mContext
    }

    //onCreateViewHolder用来给rv创建缓存
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewBindingHolder {
        val view: View
        val baseHolder: BaseViewBindingHolder
        if (viewType == TYPE_EMPTY) {
            // 创建空布局item
            baseHolder = BaseViewBindingHolder(getEmptyView(parent)!!, null)
        } else {
            // 创建普通的item
            var binding: VB?  = initViewBinding()
            baseHolder = BaseViewBindingHolder(rootView!!, binding)
        }
        return baseHolder
    }

    override fun onBindViewHolder(holder: BaseViewBindingHolder, position: Int) {
        // 如果是空布局item，不需要绑定数据
        if (isEmptyPosition(position)) {
            //处理空布局的相关ui和事件
        } else {
            if (mList == null || mList!!.size <= 0) {
                return
            }
            val item = mList!![position]
            if (holder.binding != null) {
                var binding = holder.binding as VB?
                setListener(holder, binding,item, position)
                setContent(holder, binding,item, position)
            }
        }
    }

    private fun initViewBinding():VB {
        var binding = ViewBindingCreator.createViewBinding<VB>(javaClass, mActivity?.layoutInflater)!!
        rootView =
            generateContentView(if (binding == null) getContentView() else binding!!.root)
        return binding
    }

    protected open fun getContentView(): View? {
        return FrameLayout(mContext!!)
    }

    protected open fun generateContentView(contentView: View?): View? {
        return contentView
    }

    /**
     * 设置内容
     *
     * @param holder
     * @param item
     */
    protected abstract fun setContent(holder: BaseViewBindingHolder?,binding:VB?, item: T, position: Int)

    /**
     * 设置点击事件
     *
     * @param holder
     * @param item
     */
    protected abstract fun setListener(holder: BaseViewBindingHolder?,binding:VB?, item: T, position: Int)

    //获取记录数据
    override fun getItemCount(): Int {
        // 判断数据是否空，如果没有数据，并且需要显示空布局，就返回1。
        val count = if (mList != null) mList!!.size else 0
        return if (count > 0) {
            count
        } else if (showEmptyView) {
            // 显示空布局
            1
        } else {
            0
        }
    }


    open fun onDestroy() {}

    /**
     * 获取空布局
     */
    open fun getEmptyView(parent: ViewGroup?): View? {
//        View view = LayoutInflater.from(mContext).inflate(emptyLayoutId, parent, false);
        return emptyView
    }


    override fun getItemViewType(position: Int): Int {
        return if (isEmptyPosition(position)) {
            // 空布局
            TYPE_EMPTY
        } else {
            TYPE_ITEM
        }
    }

    /**
     * 判断是否是空布局
     */
    open fun isEmptyPosition(position: Int): Boolean {
        val count = if (mList != null) mList!!.size else 0
        return position == 0 && showEmptyView && count == 0
    }

    /**
     * 设置空布局显示。默认不显示
     */
    open fun showEmptyView(isShow: Boolean) {
        showEmptyView = isShow
        notifyDataSetChanged()
    }

    open fun setEmptyView(empty: View?) {
        showEmptyView = true
        emptyView = empty
    }

    open fun isShowEmptyView(): Boolean {
        return showEmptyView
    }

    /**
     * 添加点击事件监听器
     *
     * @param onItemClickListener
     */
    open fun setOnItemClickListener(onItemClickListener: OnItemClickListener?) {
        mOnItemClickListener = onItemClickListener
    }

    interface OnItemClickListener {
        fun onItemClick(position: Int, item: Any)
    }

    private fun showLoadingDialog() {
        showLoadingDialog("")
    }

    private fun showLoadingDialog(title: String?) {
        var title = title
        if (TextUtils.isEmpty(title)) {
            title = mContext?.getString(R.string.app_loading)!!
        }
        if (null == mSpectrumLoadingDialog) {
            mSpectrumLoadingDialog = SpectrumLoadingDialog(mContext!!)
        }
        mSpectrumLoadingDialog?.setLoadingTitle(title)
        mSpectrumLoadingDialog?.show()
    }

    open fun showLoadingDialog(title: String, canClose: Boolean) {
        var title = title
        if (TextUtils.isEmpty(title)) {
            title = mContext?.getString(R.string.app_loading)!!
        }
        if (null == mSpectrumLoadingDialog) {
            mSpectrumLoadingDialog = SpectrumLoadingDialog(mContext!!)
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
    private fun isActivityEnable(): Boolean {
        return (!mActivity!!.isDestroyed && !mActivity!!.isFinishing)
    }

    open fun initViewObservable(){

    }
}