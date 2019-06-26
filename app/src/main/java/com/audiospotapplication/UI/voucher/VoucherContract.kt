package com.audiospotapplication.UI.voucher

import android.content.Context

interface VoucherContract {

    interface Presenter {
        fun start()
        fun applyVoucher(voucher: String)
    }

    interface View {
        fun getAppContext(): Context?
        fun showLoadingDialog()
        fun dismissLoading()
        fun showMessage(message: String)
        fun showCongratulationScreen()
    }
}