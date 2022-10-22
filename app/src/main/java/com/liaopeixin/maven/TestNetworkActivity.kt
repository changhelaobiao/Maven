package com.liaopeixin.maven

import android.util.Log
import androidx.lifecycle.Observer
import com.liaopeixin.lib_mvvm.BaseViewBindingActivity
import com.liaopeixin.maven.databinding.ActivityTestNetworkLayoutBinding

/**
 * author : toby
 * e-mail : 16620129640@163.com
 * time : 2022/10/22
 * desc :
 */
class TestNetworkActivity :
    BaseViewBindingActivity<TestNetworkViewModel, ActivityTestNetworkLayoutBinding>() {

    override fun initData() {
        super.initData()
    }

    override fun initView() {
        super.initView()
        binding?.apply {
            btn1.setOnClickListener {
                viewModel?.getData()
            }
        }
    }

    override fun initViewObservable() {
        super.initViewObservable()
        viewModel?.mDataList?.observe(this, Observer {
            Log.d("------>", "------->${it.size}")
        })
    }

}