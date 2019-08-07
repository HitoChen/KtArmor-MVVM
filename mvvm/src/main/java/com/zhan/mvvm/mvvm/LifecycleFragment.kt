package com.zhan.mvvm.mvvm

import androidx.annotation.StringRes
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.zhan.mvvm.base.BaseFragment
import com.zhan.mvvm.bean.SharedData
import com.zhan.mvvm.bean.SharedType
import com.zhan.mvvm.ext.Toasts.toast
import com.zhan.mvvm.utils.Clzz

/**
 * @author  hyzhan
 * @date    2019/5/22
 * @desc    TODO
 */
abstract class LifecycleFragment<VM : BaseViewModel<*>> : BaseFragment() {

    lateinit var viewModel: VM

    override fun initData() {
        super.initData()

        viewModel = ViewModelProviders.of(this).get(Clzz.getClass(this))
        viewModel.sharedData.observe(this, observer)
        dataObserver()
    }

    open fun dataObserver() {}

    open fun showSuccess() {}

    open fun showError(msg: String) {
        toast(msg)
    }

    open fun showLoading() {}

    open fun showTips(@StringRes strRes: Int) {
        toast(getString(strRes))
    }

    open fun showEmptyView() {}

    // 分发状态
    private val observer by lazy {
        Observer<SharedData> { sharedData ->
            sharedData?.run {
                when (type) {
                    SharedType.SUCCESS -> showSuccess()
                    SharedType.ERROR -> showError(msg)
                    SharedType.LOADING -> showLoading()
                    SharedType.TIPS -> showTips(strRes)
                    SharedType.EMPTY -> showEmptyView()
                }
            }
        }
    }
}