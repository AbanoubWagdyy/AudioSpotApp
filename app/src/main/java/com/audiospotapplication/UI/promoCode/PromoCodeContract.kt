package com.audiospotapplication.UI.promoCode

import android.content.Context
import com.audiospotapplication.UI.BaseView

interface PromoCodeContract {

    interface Presenter {
        fun start()
        fun applyPromoCode(promoCode: String)
    }

    interface View : BaseView {
        fun getAppContext(): Context?
        fun showLoadingDialog()
        fun dismissLoading()
        fun setSubTotal(toString: String)
        fun setDiscount(toString: String)
        fun setTotal(toString: String)
        fun showMessage(message: String)
        fun showPayment(promoCode: String)
    }
}