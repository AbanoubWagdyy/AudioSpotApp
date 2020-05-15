package com.audiospotapplication.UI.voucher

import android.content.Context
import com.audiospotapplication.UI.BaseView

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