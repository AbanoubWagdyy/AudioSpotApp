package com.audiospotapp.UI.voucher

import com.audiospot.DataLayer.Model.BookDetailsResponse
import com.audiospotapp.DataLayer.DataRepository
import com.audiospotapp.DataLayer.Model.PromoCodeResponse
import com.audiospotapp.DataLayer.Retrofit.RetrofitCallbacks
import com.audiospotapp.DataLayer.Retrofit.RetrofitResponseHandler
import com.visionvalley.letuno.DataLayer.RepositorySource
import retrofit2.Call

class VoucherPresenter(val mView: VoucherContract.View) : VoucherContract.Presenter {

    override fun applyVoucher(voucher: String) {
        mRepositorySource.receiveBook(voucher, object : RetrofitCallbacks.BookDetailsResponseCallback {
            override fun onSuccess(result: BookDetailsResponse?) {
                val status = RetrofitResponseHandler.validateAuthResponseStatus(result!!)
                if (status == RetrofitResponseHandler.Companion.Status.VALID) {
                    mRepositorySource.saveVoucherBook(result.data)
                    mView.showCongratulationScreen()
                }else{
                    mView.showMessage(result.message)
                }
            }

            override fun onFailure(call: Call<BookDetailsResponse>?, t: Throwable?) {
                mView.showMessage("Please Check your internet connection")
            }

        })
    }

    override fun start() {
        mRepositorySource = DataRepository.getInstance(mView.getAppContext())
    }

    lateinit var mRepositorySource: RepositorySource
}