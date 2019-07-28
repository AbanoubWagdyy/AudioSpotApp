package com.audiospotapplication.UI.payment

import android.content.Context
import com.emeint.android.fawryplugin.Plugininterfacing.PayableItem
import java.util.ArrayList

interface PaymentContract {

    interface Presenter {
        fun start()
    }

    interface View {
        fun getAppContext(): Context?
        fun showLoadingDialog()
        fun dismissLoading()
        fun setPayableItems(
            items: ArrayList<PayableItem>,
            isEnglish: Boolean
        )
    }
}