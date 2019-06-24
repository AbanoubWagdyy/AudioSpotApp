package com.audiospotapp.UI.rateBook

import android.content.Context
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.audiospot.DataLayer.Model.Book

import com.audiospotapp.R
import com.audiospotapp.utils.DialogUtils
import com.audiospotapp.utils.ImageUtils
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_rate_book.*


class RateBookFragment : Fragment(), RateBookContract.View {

    override fun showMessage(message: String) {
        Snackbar.make(activity!!.findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG).show()
    }

    override fun bindResponse(bookDetailsData: Book) {
        ratingBar.rating = bookDetailsData!!.rate.toFloat()
        tvBookTitle.text = bookDetailsData!!.title
        tvNumberOfReviews.text = bookDetailsData!!.reviews.toString()
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

    override fun getAppContext(): Context? {
        return activity!!.applicationContext
    }

    override fun showLoadingDialog() {
        DialogUtils.showProgressDialog(activity!!, "Loading ...")
    }

    override fun dismissLoading() {
        DialogUtils.dismissProgressDialog()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_rate_book, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mPresenter = RateBookPresenter(this)

        mPresenter.start()

        btnSubmit.setOnClickListener {
            mPresenter.rateBook(ratingBarUser.rating, message.text.toString())
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            RateBookFragment()
    }

    lateinit var mPresenter: RateBookContract.Presenter
}
