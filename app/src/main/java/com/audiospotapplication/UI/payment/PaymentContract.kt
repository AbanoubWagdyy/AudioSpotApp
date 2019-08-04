package com.audiospotapplication.UI.payment

import android.content.Context
import com.audiospotapplication.BaseView
import com.emeint.android.fawryplugin.Plugininterfacing.PayableItem
import java.util.ArrayList

interface PaymentContract {

    interface Presenter {
        fun start()
        fun resetRepo()
    }

    interface View : BaseView {
        fun getAppContext(): Context?
        fun showLoadingDialog()
        fun dismissLoading()
        fun setPayableItems(
            items: ArrayList<PayableItem>,
            isEnglish: Boolean
        )
    }
}