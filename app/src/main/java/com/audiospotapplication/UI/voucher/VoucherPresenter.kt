package com.audiospotapplication.UI.voucher

import com.audiospot.DataLayer.Model.BookDetailsResponse
import com.audiospotapplication.DataLayer.DataRepository
import com.audiospotapplication.DataLayer.Retrofit.RetrofitCallbacks
import com.audiospotapplication.DataLayer.Retrofit.RetrofitResponseHandler
import com.visionvalley.letuno.DataLayer.RepositorySource
import retrofit2.Call

class VoucherPresenter(val mView: VoucherContract.View) : VoucherContract.Presenter {

    override fun applyVoucher(voucher: String) {
        if (voucher.equals("")) {
            mView.showMessage("Please Enter Valid Voucher")
        } else {
            mView.showLoadingDialog()
            mRepositorySource.receiveBook(voucher, object : RetrofitCallbacks.BookDetailsResponseCallback {
                override fun onSuccess(result: BookDetailsResponse?) {
                    mView.dismissLoading()
                    val status = RetrofitResponseHandler.validateAuthResponseStatus(result!!)
                    if (status == RetrofitResponseHandler.Companion.Status.VALID) {
                        mRepositorySource.saveVoucherBook(result.data)
                        mView.showCongratulationScreen()
                    } else if (status == RetrofitResponseHandler.Companion.Status.UNAUTHORIZED) {
                        mView!!.showLoginPage()
                    } else {
                        mView.showMessage(result.message)
                    }
                }

                override fun onFailure(call: Call<BookDetailsResponse>?, t: Throwable?) {
                    mView.dismissLoading()
                    mView.showMessage("Please try again")
                }
            })
        }
    }

    override fun start() {
        mRepositorySource = DataRepository.getInstance(mView.getAppContext()!!)
    }

    lateinit var mRepositorySource: RepositorySource
}