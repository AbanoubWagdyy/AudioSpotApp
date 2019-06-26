package com.audiospotapplication.UI.giveAgift

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.audiospot.DataLayer.Model.Book

import com.audiospotapplication.R
import com.audiospotapplication.UI.cart.CartActivity
import com.audiospotapplication.utils.DialogUtils
import com.audiospotapplication.utils.ImageUtils
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_give_agift.*

class GiveGiftFragment : Fragment(), GiveGiftContract.View {

    override fun showCartScreen() {
        val intent = Intent(activity!!, CartActivity::class.java)
        startActivity(intent)
    }

    override fun showMessage(message: String) {
        Snackbar.make(activity!!.findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG).show()
    }

    override fun finalizeView() {
        Handler().postDelayed({
            activity!!.finish()
        }, 1000)
    }

    override fun showInvalidEmailMessage(message: String) {
        Snackbar.make(activity!!.findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG).show()
    }

    override fun getAppContext(): Context? {
        return activity!!.applicationContext
    }

    override fun showLoadingDialog() {
        DialogUtils.showProgressDialog(activity!!, "Loading ....")
    }

    override fun dismissLoading() {
        DialogUtils.dismissProgressDialog()
    }

    override fun bindResponse(result: Book?) {
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
        ImageUtils.setImageFromUrlIntoImageViewUsingPicasso(result.cover, activity!!.applicationContext, ivBook)
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