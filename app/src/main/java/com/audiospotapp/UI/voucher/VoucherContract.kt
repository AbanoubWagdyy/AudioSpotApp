package com.audiospotapp.UI.voucher

import android.content.Context
import com.audiospot.DataLayer.Model.Book

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