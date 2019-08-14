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
    }

    interface View : BaseView {
        fun getAppContext(): Context?
        fun showLoadingDialog()
        fun dismissLoading()
        fun setPayableItems(
            items: ArrayList<PayableItem>,
            isEnglish: Boolean
        )

        fun setFawryCustomParams(fawryCustomParams: FawryCustomParams)
    }
}