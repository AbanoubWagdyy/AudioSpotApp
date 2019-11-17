package com.audiospotapplication.UI.bookDetails

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.audiospotapplication.R
import kotlinx.android.synthetic.main.fragment_book_details.*
import android.text.style.UnderlineSpan
import android.text.SpannableString
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.audiospot.DataLayer.Model.Book
import com.audiospot.DataLayer.Model.BookDetailsResponse
import com.audiospotapplication.BaseFragment
import com.audiospotapplication.DataLayer.Model.Review
import com.audiospotapplication.UI.BaseActivity
import com.audiospotapplication.UI.bookChapters.BookChaptersActivity
import com.audiospotapplication.UI.bookDetails.adapter.ReviewListAdapter
import com.audiospotapplication.UI.bookReviews.BookReviewsActivity
import com.audiospotapplication.UI.giftSelection.GiftSelectionActivity
import com.audiospotapplication.UI.login.LoginActivity
import com.audiospotapplication.UI.rateBook.RateBookActivity

import com.audiospotapplication.utils.DialogUtils
import com.audiospotapplication.utils.ImageUtils
import com.google.android.material.snackbar.Snackbar

class BookDetailsFragment : BaseFragment(), BookDetailsContract.View {

    override fun setCartNumber(size: Int?) {
        (activity as BaseActivity).setCartNumber(size)
    }

    override fun playSong(sampledBook: Book?) {
        sampledBook?.let {
            if (playBookSample(sampledBook) == R.drawable.ic_pause) {
                handlePlayPause()
            }
        }
    }

    override fun viewRateBookScreen() {
        val intent = Intent(activity!!, RateBookActivity::class.java)
        startActivity(intent)
    }

    override fun hideAddFavoritesButton() {
        relativeAddToFavourites.visibility = View.GONE
    }

    override fun setAddToCartText(s: String) {
        tvAddToCart.text = s
        ivAddToCart.visibility = View.GONE
    }

    override fun viewAllReviewsScreen() {
        val intent = Intent(activity!!, BookReviewsActivity::class.java)
        startActivity(intent)
    }

    override fun viewBookChaptersScreen() {
        val intent = Intent(activity!!, BookChaptersActivity::class.java)
        startActivity(intent)
    }

    override fun showGiveGiftScreen() {
        val intent = Intent(activity!!, GiftSelectionActivity::class.java)
        startActivity(intent)
    }

    override fun showLoginMessage(message: String) {
        if (activity != null)
            Snackbar.make(
                activity!!.findViewById(android.R.id.content),
                message,
                Snackbar.LENGTH_LONG
            ).show()
        Handler().postDelayed({
            val mainIntent = Intent(activity!!, LoginActivity::class.java)
            activity!!.startActivity(mainIntent)
        }, 500)
    }

    override fun showMessage(message: String) {
        if (activity != null)
            Snackbar.make(
                activity!!.findViewById(android.R.id.content),
                message,
                Snackbar.LENGTH_LONG
            ).show()
    }

    override fun setBookReviews(reviews: List<Review>) {
        if (reviews.isNotEmpty()) {
            recyclerReviews.layoutManager = LinearLayoutManager(context)
            recyclerReviews.setHasFixedSize(true)
            recyclerReviews.isNestedScrollingEnabled = false
            recyclerReviews.adapter = ReviewListAdapter(reviews, ReviewListAdapter.Display.SEMI)
        }
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

    lateinit var mPresenter: BookDetailsContract.Presenter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_book_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val content = SpannableString("See All Reviews")
        content.setSpan(UnderlineSpan(), 0, content.length, 0)
        tvSeeAllReviews.text = content

        tvSeeAllReviews.setOnClickListener {
            mPresenter.handleSeeAllReviewsClicked()
        }

        relativeViewBookChapters.setOnClickListener {
            mPresenter.handleViewBookChaptersClicked()
        }

        relativeAddToCart.setOnClickListener {
            mPresenter.addToCart()
        }

        relativeAddToFavourites.setOnClickListener {
            mPresenter.addToFavorites()
        }

        relativeGiveAGift.setOnClickListener {
            mPresenter.handleGiveGiftClicked()
        }

        ivPlay.setOnClickListener {
            mPresenter.handlePlayClicked()
        }

        mPresenter = BookDetailsPresenter(this)
        mPresenter.start()

        getPlayingObserver().observe(this, Observer {
            val id =
                mPresenter.getCurrentSampleBookPlaylistId()
            if (it && id.equals(getPlaylistIdObserver().value)) {
                ivPlay.setImageResource(R.mipmap.homepage_play)
            } else {
                ivPlay.setImageResource(R.mipmap.play)
            }
        })
    }

    override fun bindResponse(result: BookDetailsResponse?) {

        result?.let {
            ratingBar.rating = it.data.rate.toFloat()

            ratingBar.setOnRatingChangeListener(null)
            ratingBar.setOnTouchListener { p0, p1 -> true }

            ratingBar.setOnRatingChangeListener { ratingBar, rating, fromUser ->
                ratingBar.rating = it.data.rate.toFloat()
            }

            tvBookTitle.text = it.data.title
            tvNumberOfReviews.text = "(" + it.data.reviews.toString() + " reviews" + ")"
            tvAuthor.text = it.data.author
            if (it.data.narators.isNotEmpty()) {
                val builder = StringBuilder()
                for (narator in it.data.narators) {
                    builder.append(narator.name).append(",")
                }
                tvNarrator.text = builder.toString().substring(0, builder.toString().length - 1)
            }

            tvCategory.text = it.data.category
            tvPublisher.text = it.data.publisher
            tvAbout.text = it.data.about_book

            tvLength.text = it.data.total_time_trt + " Hours."

            tvPrice.text = it.data.price.toString() + " EGP."

            ImageUtils.setImageFromUrlIntoImageViewUsingGlide(
                it.data.cover,
                activity!!.applicationContext,
                ivBook
            )
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            BookDetailsFragment()
    }
}