package com.audiospotapplication.ui.giveAgift

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.audiospot.DataLayer.Model.Book
import com.audiospotapplication.ui.BaseFragment

import com.audiospotapplication.R
import com.audiospotapplication.ui.payment.PaymentActivity
import com.audiospotapplication.utils.DialogUtils
import com.audiospotapplication.utils.ImageUtils
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_give_agift.*

class GiveGiftFragment : BaseFragment(), GiveGiftContract.View {

    override fun showPayment(emails: String, voucher: String, id: Int) {
        val intent = Intent(requireActivity(), PaymentActivity::class.java)
        intent.putExtra("BOOKID", id.toString())
        intent.putExtra("VOUCHER", voucher)
        intent.putExtra("SENDTO", emails)
        startActivity(intent)
        requireActivity().finish()
    }

    override fun showMessage(message: String) {
        if (activity != null)
            Snackbar.make(requireActivity().findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG).show()
    }

    override fun finalizeView() {
        Handler().postDelayed({
            requireActivity().finish()
        }, 1000)
    }

    override fun showInvalidEmailMessage(message: String) {
        if (activity != null)
            Snackbar.make(requireActivity().findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG).show()
    }

    override fun getAppContext(): Context? {
        return requireActivity().applicationContext
    }

    override fun showLoadingDialog() {
        DialogUtils.showProgressDialog(requireActivity(), "Loading ....")
    }

    override fun dismissLoading() {
        DialogUtils.dismissProgressDialog()
    }

    override fun bindResponse(result: Book?, quantity: Int) {
        ratingBar.rating = result!!.rate.toFloat()
        tvBookTitle.text = result!!.title
        var numberOfReviews = result.reviews
        tvNumberOfReviews.text = "($numberOfReviews Reviews)"
        tvAuthor.text = result!!.author
        if (result.narators.isNotEmpty()) {
            val builder = StringBuilder()
            for (narator in result.narators) {
                builder.append(narator.name).append(",")
            }
            tvNarrator.text = builder.toString().substring(0, builder.toString().length - 1)
        }
        if (quantity == 1) {
            email2.visibility = View.GONE
            email3.visibility = View.GONE
            email4.visibility = View.GONE
            email5.visibility = View.GONE
        } else if (quantity == 2) {
            email3.visibility = View.GONE
            email4.visibility = View.GONE
            email5.visibility = View.GONE
        } else if (quantity == 3) {
            email4.visibility = View.GONE
            email5.visibility = View.GONE
        } else if (quantity == 4) {
            email5.visibility = View.GONE
        }

        ImageUtils.setImageFromUrlIntoImageViewUsingGlide(result.cover, requireActivity().applicationContext, ivBook)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_give_agift, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mPresenter = GiveGiftPresenter(this)
        mPresenter.start()
        ivPlay.setOnClickListener {
        }
        relativeGiveAGift.setOnClickListener {
            mPresenter.giveGift(
                email1.text.toString(),
                email2.text.toString(),
                email3.text.toString(),
                email4.text.toString(),
                email5.text.toString()
            )
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            GiveGiftFragment()
    }

    lateinit var mPresenter: GiveGiftContract.Presenter
}