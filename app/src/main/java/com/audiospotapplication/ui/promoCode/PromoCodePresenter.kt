package com.audiospotapplication.ui.promoCode

import com.audiospotapplication.data.DataRepository
import com.audiospotapplication.data.model.PromoCodeResponse
import com.audiospotapplication.data.retrofit.RetrofitCallbacks
import com.audiospotapplication.data.retrofit.RetrofitResponseHandler
import com.audiospotapplication.data.RepositorySource
import retrofit2.Call

class PromoCodePresenter(val mView: PromoCodeContract.View) : PromoCodeContract.Presenter {
    override fun applyPromoCode(promoCode: String) {
        mView.showLoadingDialog()
        mRepositorySource.addPromoCode(promoCode, object : RetrofitCallbacks.PromoCodeResponseCallback {
            override fun onSuccess(result: PromoCodeResponse?) {
                mView.dismissLoading()
                mView.showMessage(result!!.message)

                val status = RetrofitResponseHandler.validateAuthResponseStatus(result)
                if (status == RetrofitResponseHandler.Companion.Status.VALID) {
//                    mView.setSubTotal(result!!.data.sub_total.toString())
//                    mView.setDiscount(result!!.data.discount.toString())
//                    mView.setTotal(result!!.data.total.toString())
                    mView.showPayment(promoCode)
                } else if (status == RetrofitResponseHandler.Companion.Status.UNAUTHORIZED) {
                    mView!!.showLoginPage()
                } else {
                    mView.showLoginPage()
                }
            }

            override fun onFailure(call: Call<PromoCodeResponse>?, t: Throwable?) {
                mView.dismissLoading()
                mView.showMessage("Please try again later")
            }
        })
    }

    override fun start() {
        mRepositorySource = DataRepository.getInstance(mView.getAppContext()!!)
    }

    lateinit var mRepositorySource: RepositorySource
}