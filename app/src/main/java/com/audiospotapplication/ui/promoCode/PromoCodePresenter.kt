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
        this.promoCode = promoCode
        mRepositorySource.addPromoCode(
            promoCode,
            object : RetrofitCallbacks.PromoCodeResponseCallback {
                override fun onSuccess(result: PromoCodeResponse?) {
                    mView.dismissLoading()
                    mView.showMessage(result!!.message)

                    when (RetrofitResponseHandler.validateAuthResponseStatus(result)) {
                        RetrofitResponseHandler.Companion.Status.VALID -> {
                            mView.setSubTotal()
                            mView.setDiscount(result.data.promoCode.percentage.toString())
                            mView.setTotal()
                        }
                        RetrofitResponseHandler.Companion.Status.UNAUTHORIZED -> {
                            mView.showLoginPage()
                        }
                        else -> {
                            mView.showLoginPage()
                        }
                    }
                }

                override fun onFailure(call: Call<PromoCodeResponse>?, t: Throwable?) {
                    mView.dismissLoading()
                    mView.showMessage("Please try again later")
                }
            })
    }

    override fun handleProceedToPaymentClicked() {
        mView.showPayment(promoCode)
    }

    override fun start() {
        mRepositorySource = DataRepository.getInstance(mView.getAppContext()!!)
    }

    private var promoCode: String = ""
    lateinit var mRepositorySource: RepositorySource
}