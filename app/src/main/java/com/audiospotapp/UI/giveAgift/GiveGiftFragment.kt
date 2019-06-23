package com.audiospotapp.UI.giveAgift


import android.content.Context
import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.audiospot.DataLayer.Model.Book

import com.audiospotapp.R
import com.audiospotapp.utils.DialogUtils
import com.audiospotapp.utils.ImageUtils
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_give_agift.*

class GiveGiftFragment : Fragment(), GiveGiftContract.View {

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
        tvNumberOfReviews.text = result!!.reviews.toString()
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
            mPresenter.giveGift(email.text.toString())
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            GiveGiftFragment()
    }

    lateinit var mPresenter: GiveGiftContract.Presenter
}
