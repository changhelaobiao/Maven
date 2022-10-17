package com.liaopeixin.maven

import android.util.Log
import com.liaopeixin.lib_mvvm.BaseViewBindingActivity
import com.liaopeixin.maven.databinding.ActivityTestMvvmBinding

/**
 * author : toby
 * e-mail : 16620129640@163.com
 * time : 2022/10/16
 * desc :
 */
class TestMvvmActivity : BaseViewBindingActivity<TestViewModel, ActivityTestMvvmBinding>() {

    override fun initData() {
        super.initData()
        Log.d("------>", "-----initData---->");
    }

    override fun initView() {
        super.initView()
        Log.d("------>", "-----initView---->");
    }

    override fun initEvent() {
        super.initEvent()
        Log.d("------>", "-----initEvent---->");
    }


}