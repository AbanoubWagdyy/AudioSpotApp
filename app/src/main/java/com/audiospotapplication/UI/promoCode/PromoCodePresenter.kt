package com.audiospotapplication.UI.promoCode

import com.audiospotapplication.DataLayer.DataRepository
import com.audiospotapplication.DataLayer.Model.PromoCodeResponse
import com.audiospotapplication.DataLayer.Retrofit.RetrofitCallbacks
import com.audiospotapplication.DataLayer.Retrofit.RetrofitResponseHandler
import com.visionvalley.letuno.DataLayer.RepositorySource
import retrofit2.Call

class PromoCodePresenter(val mView: PromoCodeContract.View) : PromoCodeContract.Presenter {
    override fun applyPromoCode(promoCode: String) {
        mView.showLoadingDialog()
        mRepositorySource.addPromoCode(promoCode, object : RetrofitCallbacks.PromoCodeResponseCallback {
            override fun onSuccess(result: PromoCodeResponse?) {
                mView.dismissLoading()
                val status = RetrofitResponseHandler.validateAuthResponseStatus(result)
                if (status == RetrofitResponseHandler.Companion.Status.VALID) {
                    mView.setSubTotal(result!!.data.sub_total.toString())
                    mView.setDiscount(result!!.data.discount.toString())
                    mView.setTotal(result!!.data.total.toString())
                }
                mView.showMessage(result!!.message)
            }

            override fun onFailure(call: Call<PromoCodeResponse>?, t: Throwable?) {
                mView.dismissLoading()
                mView.showMessage("Please try again later")
            }
        })
    }

    override fun start() {
        mRepositorySource = DataRepository.getInstance(mView.getAppContext())
    }

    lateinit var mRepositorySource: RepositorySource
}