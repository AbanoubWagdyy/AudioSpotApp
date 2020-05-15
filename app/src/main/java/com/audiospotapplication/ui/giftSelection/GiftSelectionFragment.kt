package com.audiospotapplication.ui.giftSelection


import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.audiospot.DataLayer.Model.Book
import com.audiospotapplication.ui.BaseFragment

import com.audiospotapplication.R
import com.audiospotapplication.ui.giveAgift.GiveGiftActivity
import com.audiospotapplication.ui.payment.PaymentActivity
import com.audiospotapplication.utils.DialogUtils
import com.audiospotapplication.utils.ImageUtils
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_gift_selection.*

class GiftSelectionFragment : BaseFragment(), GiftSelectionContract.View {

    override fun showPayment(id: Int, quantity: Int) {
        val intent = Intent(requireActivity(), PaymentActivity::class.java)
        intent.putExtra("BOOKID", id.toString())
        intent.putExtra("VOUCHER", quantity.toString())
        startActivity(intent)
        requireActivity().finish()
    }

    override fun showGiftSCreen() {
        val intent = Intent(requireActivity(), GiveGiftActivity::class.java)
        startActivity(intent)
    }

    override fun getAppContext(): Context? {
        return requireActivity().applicationContext
    }

    override fun showLoadingDialog() {
        DialogUtils.showProgressDialog(requireActivity(), "Loading ...")
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
        ImageUtils.setImageFromUrlIntoImageViewUsingGlide(
            bookDetailsData.cover,
            requireActivity().applicationContext,
            ivBook
        )
    }

    override fun showMessage(message: String) {
        if (activity != null)
            Snackbar.make(
                requireActivity().findViewById(android.R.id.content),
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
            if (quantityStrAsInt < 5) {
                quantityStrAsInt += 1
                quantity.text = quantityStrAsInt.toString()
            }
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
