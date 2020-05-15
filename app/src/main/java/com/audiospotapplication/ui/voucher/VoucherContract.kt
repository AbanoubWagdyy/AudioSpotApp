package com.audiospotapplication.ui.voucher

import android.content.Context
import com.audiospotapplication.ui.BaseView

interface VoucherContract {

    interface Presenter {
        fun start()
        fun applyVoucher(voucher: String)
    }

    interface View : BaseView {
        fun getAppContext(): Context?
        fun showLoadingDialog()
        fun dismissLoading()
        fun showMessage(message: String)
        fun showCongratulationScreen()
    }
}