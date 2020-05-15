package com.audiospotapplication.ui.giveAgift

import com.audiospot.DataLayer.Model.Book
import com.audiospotapplication.data.DataRepository
import com.audiospotapplication.data.model.Response
import com.audiospotapplication.data.retrofit.RetrofitCallbacks
import com.audiospotapplication.data.retrofit.RetrofitResponseHandler
import com.visionvalley.letuno.DataLayer.RepositorySource
import retrofit2.Call
import java.lang.StringBuilder

class GiveGiftPresenter(val mView: GiveGiftContract.View) : GiveGiftContract.Presenter {

    override fun giveGift(email1: String, email2: String, email3: String, email4: String, email5: String) {

        if (email1.equals("") && email2.equals("") && email3.equals("") && email4.equals("") && email5.equals("")) {
            return
        }

        mView.showLoadingDialog()

        var listEmails = ArrayList<String>()

        listEmails.add(email1)
        listEmails.add(email2)
        listEmails.add(email3)
        listEmails.add(email4)
        listEmails.add(email5)

        var notEmptyEmails = listEmails.filter {
            it.isNotEmpty() && it.isNotBlank()
        }

        var builder = StringBuilder()
        for (str in notEmptyEmails) {
            builder.append(str)
            builder.append(",")
        }

        var str = builder.toString().substring(0, builder.toString().length - 1)
        var voucher = mRepositorySource.getVoucher().toString()

        mRepositorySource.sendAsGift(
            email1,
            email2,
            email3,
            email4,
            email5,
            object : RetrofitCallbacks.ResponseCallback {
                override fun onSuccess(result: Response?) {
                    mView.showMessage(result!!.message)
                    mView.dismissLoading()
                    val status = RetrofitResponseHandler.validateAuthResponseStatus(result)
                    if (status == RetrofitResponseHandler.Companion.Status.VALID) {
                        mView.showPayment(str, voucher, mRepositorySource.getSavedBook()!!.id)
                    } else if (status == RetrofitResponseHandler.Companion.Status.UNAUTHORIZED) {
                        mView!!.showLoginPage()
                    } else {
                        mView.showMessage(result!!.message)
                    }
                }

                override fun onFailure(call: Call<Response>?, t: Throwable?) {
                    mView.dismissLoading()
                    mView.showMessage("Please try again later")
                }

                override fun onAuthFailure() {

                }

            })
    }

    override fun start() {
        mRepositorySource = mView.getAppContext()?.let { DataRepository.getInstance(it) }!!
        var bookDetailsData = mRepositorySource.getSavedBook()
        mView.bindResponse(bookDetailsData!!, (mRepositorySource as DataRepository).quantity)
    }

    lateinit var mRepositorySource: RepositorySource
    lateinit var bookDetails: Book
}