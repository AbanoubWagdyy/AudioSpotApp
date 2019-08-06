package com.audiospotapplication.UI.payment

import android.os.Bundle
import com.audiospot.DataLayer.Model.BookDetailsResponse
import com.audiospotapplication.DataLayer.DataRepository
import com.audiospotapplication.DataLayer.Model.BookListResponse
import com.audiospotapplication.DataLayer.Model.FawryCustomParams
import com.audiospotapplication.DataLayer.Model.PromoCodeResponse
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
                        item.setDescription(book.title)
                        item.qty = "1"
                        item.sku = book.id.toString()
                        items.add(item)
                    }
                    mView.setPayableItems(items, mRepositorySource.getCurrentLanguage().equals("en"))
                }

                override fun onFailure(call: Call<BookListResponse>?, t: Throwable?) {
                    mView.dismissLoading()
                }
            })
        } else {
            var bookId = -1
            if (extras.containsKey("BOOKID")) {
                bookId = extras.getString("BOOKID")!!.toInt()
            }

            if (bookId == -1) {

                var promoCode = ""
                var promoCodeResponse: PromoCodeResponse? = null
                promoCode = if (extras.getString("promoCode") != null)
                    extras.getString("promoCode")!!
                else {
                    ""
                }

                if (!promoCode.equals("")) {
                    promoCodeResponse = mRepositorySource.getPromoCodeResponse()
                }

                fawryCustomParams = FawryCustomParams("", "", promoCode)

                mView.setFawryCustomParams(fawryCustomParams)

                mRepositorySource.getMyCart(object : RetrofitCallbacks.BookListCallback {
                    override fun onSuccess(result: BookListResponse?) {
                        mView.dismissLoading()
                        for (book in result?.data!!) {
                            val item = Item()
                            item.setPrice(book.price.toString())
                            item.setDescription(book.title)
                            item.qty = "1"
                            item.sku = book.id.toString()
                            items.add(item)
                        }

                        val sentItems = ArrayList<PayableItem>()
                        if (promoCodeResponse != null) {
                            items.forEach {
                                val item = Item()
                                val price_before = promoCodeResponse.data.promoCode.percentage* it.fawryItemPrice.toDouble() / 100
                                val price = it.fawryItemPrice.toDouble() - price_before
                                item.setPrice(price.toString())
                                item.setDescription(it.fawryItemDescription)
                                item.qty = "1"
                                item.sku = it.fawryItemSKU
                                sentItems.add(item)
                            }
                        } else {
                            sentItems.addAll(items)
                        }
                        mView.setPayableItems(sentItems, mRepositorySource.getCurrentLanguage().equals("en"))
                    }

                    override fun onFailure(call: Call<BookListResponse>?, t: Throwable?) {
                        mView.dismissLoading()
                    }
                })
            } else {
                var voucher = ""
                voucher = if (extras.getString("VOUCHER") != null)
                    extras.getString("VOUCHER")!!
                else {
                    "1"
                }
                var emails = ""
                emails = if (extras.getString("SENDTO") != null)
                    extras.getString("SENDTO")!!
                else {
                    ""
                }

                var promoCode = ""
                promoCode = if (extras.getString("promoCode") != null)
                    extras.getString("promoCode")!!
                else {
                    ""
                }

                fawryCustomParams = FawryCustomParams(voucher, emails, promoCode)

                mView.setFawryCustomParams(fawryCustomParams)

                mRepositorySource.getBookDetailsWithId(bookId, object : RetrofitCallbacks.BookDetailsResponseCallback {
                    override fun onSuccess(result: BookDetailsResponse?) {
                        mView.dismissLoading()
                        val items = ArrayList<PayableItem>()
                        val item = Item()
                        item.setPrice(result?.data?.price.toString())
                        item.setDescription(result?.data?.title)
                        item.qty = voucher
                        item.sku = result?.data?.id.toString()
                        items.add(item)
                        mView.setPayableItems(items, mRepositorySource.getCurrentLanguage().equals("en"))
                    }

                    override fun onFailure(call: Call<BookDetailsResponse>?, t: Throwable?) {
                        mView.dismissLoading()
                    }
                })
            }
        }
    }

    lateinit var mRepositorySource: RepositorySource
    lateinit var fawryCustomParams: FawryCustomParams
}