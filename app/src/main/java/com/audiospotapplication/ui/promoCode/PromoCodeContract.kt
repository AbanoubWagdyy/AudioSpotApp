package com.audiospotapplication.ui.promoCode

import android.content.Context
import com.audiospotapplication.ui.BaseView

interface PromoCodeContract {

    interface Presenter {
        fun start()
        fun applyPromoCode(promoCode: String)
        fun handleProceedToPaymentClicked()
    }

    interface View : BaseView {
        fun getAppContext(): Context?
        fun showLoadingDialog()
        fun dismissLoading()
        fun setSubTotal()
        fun setDiscount(toString: String)
        fun setTotal()
        fun showMessage(message: String)
        fun showPayment(promoCode: String)
    }
}