package com.audiospotapplication.UI.payment

import android.content.Context
import com.audiospotapplication.BaseView
import com.audiospotapplication.DataLayer.Model.FawryCustomParams
import com.audiospotapplication.DataLayer.Retrofit.RetrofitCallbacks
import com.emeint.android.fawryplugin.Plugininterfacing.PayableItem
import java.util.*

interface PaymentContract {

    interface Presenter {
        fun start()
        fun resetRepo()
        fun createOrder(
            fawryCustomParams: FawryCustomParams?,
            uuid: UUID,
            createOrderResponseCallback: RetrofitCallbacks.CreateOrderResponseCallback
        )

        fun createPaypalOrder(
            createOrderResponseCallback: RetrofitCallbacks.PaypalArgumentCallback
        )

        fun getMerchantRefNumber() : String

        fun getPayItems(): MutableList<PayableItem>

        fun isEnglish(): Boolean
        fun getPaypalItemsWholeAmount(dollarPrice: Double): String
    }

    interface View : BaseView {
        fun getAppContext(): Context?
        fun showLoadingDialog()
        fun dismissLoading()

        fun setFawryCustomParams(fawryCustomParams: FawryCustomParams)
    }
}