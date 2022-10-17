package com.liaopeixin.lib_mvvm

import android.content.Context
import android.graphics.drawable.Drawable
import android.text.Spanned
import android.text.TextUtils
import android.text.TextWatcher
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.util.HashMap

/**
 * RecyclerView Holder基类
 */
class BaseViewBindingHolder(private val mRootView: View, var binding: Any?) :
    RecyclerView.ViewHolder(
        mRootView
    ) {
    //不写死控件变量，而采用Map方式
    private val mViews = HashMap<Int, View?>()

    /**
     * 获取控件的方法
     */
    fun <T> getView(viewId: Int): T? {
        //根据保存变量的类型 强转为该类型
        var view = mViews[viewId]
        if (view == null) {
            view = itemView.findViewById(viewId)
            //缓存
            mViews[viewId] = view
        }
        return view as T?
    }

    /**
     * 传入文本控件id和设置的文本值，设置文本
     */
    fun setTextViewText(view: TextView?, value: String?): BaseViewBindingHolder {
        view?.text = value
        return this
    }

    fun setTextViewText(view: TextView?, value: Spanned?): BaseViewBindingHolder {
        view?.text = value
        return this
    }

    /**
     * 传入编辑文本控件id和设置的文本值，设置文本
     */
    fun setEditViewText(view: EditText?, value: String?): BaseViewBindingHolder {
        view?.setText(value)
        return this
    }

    /**
     * 传入编辑文本控件id和设置可否编辑值
     */
    fun setEditViewEditable(view: EditText?, isEditable: Boolean): BaseViewBindingHolder {
        view?.isEnabled = isEditable
        return this
    }

    /**
     * 传入编辑文本控件的文本改变监听事件
     */
    fun setEditViewTextChangedListener(
        view: EditText?,
        textWatcher: TextWatcher?
    ): BaseViewBindingHolder {
        view?.addTextChangedListener(textWatcher)
        return this
    }

    /**
     * 传入文本控件id和设置的文本值，设置背景
     */
    fun setBackground(view: View?, drawable: Drawable?): BaseViewBindingHolder {
        if (null == drawable) {
            view?.background = null
        }
        view?.background = drawable
        return this
    }

    /**
     * 传入文本控件id和设置的文本值，设置背景
     */
    fun setBackgroundResource(view: View?, resid: Int?): BaseViewBindingHolder {
        view?.setBackgroundResource(resid!!)
        return this
    }

    /**
     * 传入文本控件id和设置的文本颜色
     */
    fun setTextViewColor(view: TextView?, context: Context, colorId: Int): BaseViewBindingHolder {
        view?.setTextColor(context.resources.getColor(colorId))
        return this
    }

    fun setTextViewSize(context: Context, view: TextView?, size: Int): BaseViewBindingHolder {
        view?.textSize = context.resources.getDimension(size)
        return this
    }

    /**
     * 传入控件id和透明度，设置控件背景透明度并且不改变文本透明度
     */
    fun setViewBackgroundAlpha(view: View?, value: Int): BaseViewBindingHolder {
        view?.background?.mutate()?.alpha = value
        return this
    }

    /**
     * 设置某个View的margin
     *
     * @param view   需要设置的view
     * @param left   左边距
     * @param right  右边距
     * @param top    上边距
     * @param bottom 下边距
     * @return
     */
    fun setViewMargin(
        view: View?,
        left: Int,
        right: Int,
        top: Int,
        bottom: Int
    ): ViewGroup.LayoutParams? {
        if (view == null) {
            return null
        }
        val params = view.layoutParams
        var marginParams: ViewGroup.MarginLayoutParams? = null
        //获取view的margin设置参数
        marginParams = if (params is ViewGroup.MarginLayoutParams) {
            params
        } else {
            //不存在时创建一个新的参数
            ViewGroup.MarginLayoutParams(params)
        }
        //设置margin
        marginParams.setMargins(left, top, right, bottom)
        view.layoutParams = marginParams
        return marginParams
    }

    /**
     * 设置图片
     */
    fun setImageResource(view: ImageView?, drawable: Drawable?): BaseViewBindingHolder {
        view?.setImageDrawable(drawable)
        return this
    }

    /**
     * 设置图片
     */
    fun setImageResource(view: ImageView?, resID: Int): BaseViewBindingHolder {
        view?.setImageResource(resID)
        return this
    }

    /**
     * 传入图片控件id和资源id，设置图片
     */
//    fun setImageResource(view: ImageView?, url: String?): BaseViewBindingHolder {
//        if (view != null) {
//            if (TextUtils.isEmpty(url)) {
//                BitmapUtils.loadResoureBitmapToImage(
//                    mRootView.context,
//                    R.drawable.icon_default,
//                    view
//                )
//            } else {
//                BitmapUtils.loadBitmapToImage(mRootView.context, url, view)
//            }
//        }
//        return this
//    }

    fun setViewClick(view: View?, listener: View.OnClickListener?): BaseViewBindingHolder {
        view?.setOnClickListener(listener)
        return this
    }

    fun setImageViewClick(view: View?, listener: View.OnClickListener?): BaseViewBindingHolder {
        view?.setOnClickListener(listener)
        return this
    }

    fun setViewVisibly(view: View?, visibility: Int): BaseViewBindingHolder {
        if (view != null) {
            view.visibility = visibility
        }
        return this
    }

    fun setLayoutParms(view: View?, parmes: LinearLayout.LayoutParams?): BaseViewBindingHolder {
        if (view != null) {
            view.layoutParams = parmes
        }
        return this
    }

    fun setViewLongClick(view: View?, listener: View.OnLongClickListener?): BaseViewBindingHolder {
        view?.setOnLongClickListener(listener)
        return this
    }

    fun setOnLongClickListener(listener: View.OnLongClickListener?) {
        mRootView.setOnLongClickListener(listener)
    }

    fun OnClickListener(listener: View.OnClickListener?) {
        mRootView.setOnClickListener(listener)
    }
}