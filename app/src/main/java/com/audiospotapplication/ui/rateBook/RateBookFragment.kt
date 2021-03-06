package com.audiospotapplication.ui.rateBook

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.audiospot.DataLayer.Model.Book
import com.audiospotapplication.ui.BaseFragment

import com.audiospotapplication.R
import com.audiospotapplication.utils.DialogUtils
import com.audiospotapplication.utils.ImageUtils
import com.google.android.material.snackbar.Snackbar
import com.willy.ratingbar.BaseRatingBar
import kotlinx.android.synthetic.main.fragment_rate_book.*


class RateBookFragment : BaseFragment(), RateBookContract.View {

    override fun showHompageScreen() {
        requireActivity().finish()
    }

    override fun showMessage(message: String) {
        if (activity != null)
            Snackbar.make(requireActivity().findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG).show()
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

    override fun getAppContext(): Context? {
        return requireActivity().applicationContext
    }

    override fun showLoadingDialog() {
        DialogUtils.showProgressDialog(requireActivity(), "Loading ...")
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

        ratingBarUser.setOnRatingChangeListener(object : BaseRatingBar.OnRatingChangeListener {
            override fun onRatingChange(ratingBar: BaseRatingBar?, rating: Float, fromUser: Boolean) {
                this@RateBookFragment.rating = ratingBar!!.rating
            }
        })

        btnSubmit.setOnClickListener {
            mPresenter.rateBook(rating, message.text.toString())
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            RateBookFragment()
    }

    lateinit var mPresenter: RateBookContract.Presenter
    var rating: Float = 5.0f
}
