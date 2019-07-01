package com.audiospotapplication.UI.giftSelection


import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.audiospot.DataLayer.Model.Book

import com.audiospotapplication.R
import com.audiospotapplication.UI.cart.CartActivity
import com.audiospotapplication.UI.giveAgift.GiveGiftActivity
import com.audiospotapplication.UI.payment.PaymentActivity
import com.audiospotapplication.utils.DialogUtils
import com.audiospotapplication.utils.ImageUtils
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_gift_selection.*

class GiftSelectionFragment : Fragment(), GiftSelectionContract.View {

    override fun showPayment() {
        val intent = Intent(activity!!, PaymentActivity::class.java)
        startActivity(intent)
        activity!!.finish()
    }

    override fun showCartScreen() {
        val intent = Intent(activity!!, CartActivity::class.java)
        startActivity(intent)
    }

    override fun showGiftSCreen() {
        val intent = Intent(activity!!, GiveGiftActivity::class.java)
        startActivity(intent)
    }

    override fun getAppContext(): Context? {
        return activity!!.applicationContext
    }

    override fun showLoadingDialog() {
        DialogUtils.showProgressDialog(activity!!, "Loading ...")
    }

    override fun dismissLoading() {
        DialogUtils.dismissProgressDialog()
    }

    override fun bindResponse(bookDetailsData: Book) {
        ratingBar.rating = bookDetailsData!!.rate.toFloat()
        tvBookTitle.text = bookDetailsData!!.title
        var reviews = bookDetailsData!!.reviews.toString()
        tvNumberOfReviews.text = "($reviews Reviews)"
        tvAuthor.text = bookDetailsData!!.author
        if (bookDetailsData.narators.isNotEmpty()) {
            val builder = StringBuilder()
            for (narator in bookDetailsData.narators) {
                builder.append(narator.name).append(",")
            }
            tvNarrator.text = builder.toString().substring(0, builder.toString().length - 1)
        }
        ImageUtils.setImageFromUrlIntoImageViewUsingPicasso(
            bookDetailsData.cover,
            activity!!.applicationContext,
            ivBook
        )
    }

    override fun showMessage(message: String) {
        if (activity != null)
            Snackbar.make(
                activity!!.findViewById(android.R.id.content),
                message,
                Snackbar.LENGTH_SHORT
            ).show()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_gift_selection, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        relativeAdd.setOnClickListener {
            var quantityStrAsInt = quantity.text.toString().toInt()
            quantityStrAsInt += 1
            quantity.text = quantityStrAsInt.toString()
        }

        relativeMinus.setOnClickListener {
            var quantityStrAsInt = quantity.text.toString().toInt()
            if (quantityStrAsInt > 1) {
                quantityStrAsInt -= 1
                quantity.text = quantityStrAsInt.toString()
            }
        }

        btnSubmit.setOnClickListener {
            if (radioSendToSpecificAccount.isChecked) {
                giftSelection = GiftSelection.AUDIOSPOT_ACCOUNT
            } else {
                giftSelection = GiftSelection.VOUCHER
            }

            mPresenter.handleSubmitClicked(giftSelection, quantity.text.toString().toInt())
        }

        mPresenter = GiftSelectionPresenter(this)
        mPresenter.start()
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            GiftSelectionFragment()
    }

    lateinit var mPresenter: GiftSelectionContract.Presenter
    var giftSelection: GiftSelection = GiftSelection.AUDIOSPOT_ACCOUNT
}
