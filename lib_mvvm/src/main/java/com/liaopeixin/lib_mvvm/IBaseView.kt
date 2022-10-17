package com.liaopeixin.lib_mvvm

/**
 *
 * author：fengjunming on 2022/4/25
 * email：fengjunming@ysbang.cn
 * company:ysbang
 * remark：
 * View层：初始化作用
 */
interface IBaseView {
 /**
  * 初始化数据
  */
 fun initData()

 /**
  * 初始化View
  */
 fun initView()

 /**
  * 初始化响应事件
  */
 fun initEvent()

 /**
  * 初始化界面观察者的监听
  */
 fun initViewObservable()

 /**
  * 获取数据
  */
 fun loadData()
}