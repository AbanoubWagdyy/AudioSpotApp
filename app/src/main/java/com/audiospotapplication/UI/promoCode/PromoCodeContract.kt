package com.audiospotapplication.UI.promoCode

import android.content.Context

interface PromoCodeContract {

    interface Presenter {
        fun start()
        fun applyPromoCode(promoCode: String)
    }

    interface View {
        fun getAppContext(): Context?
        fun showLoadingDialog()
        fun dismissLoading()
        fun setSubTotal(toString: String)
        fun setDiscount(toString: String)
        fun setTotal(toString: String)
        fun showMessage(message: String)
    }
}