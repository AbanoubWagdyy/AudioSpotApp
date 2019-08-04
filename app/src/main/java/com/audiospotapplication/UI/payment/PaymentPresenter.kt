package com.audiospotapplication.UI.payment

import android.os.Bundle
import com.audiospot.DataLayer.Model.BookDetailsResponse
import com.audiospotapplication.DataLayer.DataRepository
import com.audiospotapplication.DataLayer.Model.BookListResponse
import com.audiospotapplication.DataLayer.Retrofit.RetrofitCallbacks
import com.emeint.android.fawryplugin.Plugininterfacing.PayableItem
import com.visionvalley.letuno.DataLayer.RepositorySource
import retrofit2.Call
import java.util.ArrayList

class PaymentPresenter(val mView: PaymentContract.View, val extras: Bundle?) : PaymentContract.Presenter {

    override fun resetRepo() {
        mRepositorySource.clear()
    }

    override fun start() {
        val items = ArrayList<PayableItem>()
        mView.showLoadingDialog()
        mRepositorySource = DataRepository.getInstance(mView.getAppContext()!!)

        if (extras == null) {
            mRepositorySource.getMyCart(object : RetrofitCallbacks.BookListCallback {
                override fun onSuccess(result: BookListResponse?) {
                    mView.dismissLoading()
                    for (book in result?.data!!) {
                        val item = Item()
                        item.setPrice(book.price.toString())
                        item.setDescription(book.about_book)
                        item.qty = "1"
                        item.sku = "1"
                        items.add(item)
                    }
                    mView.setPayableItems(items, mRepositorySource.getCurrentLanguage().equals("en"))
                }

                override fun onFailure(call: Call<BookListResponse>?, t: Throwable?) {
                    mView.dismissLoading()
                }

            })
        } else {
            var bookId = extras.getInt("BOOKID")

            mRepositorySource.getBookDetailsWithId(bookId, object : RetrofitCallbacks.BookDetailsResponseCallback {
                override fun onSuccess(result: BookDetailsResponse?) {
                    mView.dismissLoading()
                    val items = ArrayList<PayableItem>()
                    val item = Item()
                    item.setPrice(result?.data?.price.toString())
                    item.setDescription(result?.data?.about_book)
                    item.qty = "1"
                    item.sku = "1"
                    items.add(item)
                    mView.setPayableItems(items, mRepositorySource.getCurrentLanguage().equals("en"))
                }

                override fun onFailure(call: Call<BookDetailsResponse>?, t: Throwable?) {
                    mView.dismissLoading()
                }
            })
        }
    }

    lateinit var mRepositorySource: RepositorySource
}