package com.audiospotapplication.ui.bookDetails

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
import com.audiospotapplication.ui.BaseFragment
import com.audiospotapplication.data.model.Review
import com.audiospotapplication.ui.BaseActivity
import com.audiospotapplication.ui.bookChapters.BookChaptersActivity
import com.audiospotapplication.ui.bookDetails.adapter.ReviewListAdapter
import com.audiospotapplication.ui.bookReviews.BookReviewsActivity
import com.audiospotapplication.ui.giftSelection.GiftSelectionActivity
import com.audiospotapplication.ui.login.LoginActivity
import com.audiospotapplication.ui.rateBook.RateBookActivity

import com.audiospotapplication.utils.DialogUtils
import com.audiospotapplication.utils.ImageUtils
import com.google.android.material.snackbar.Snackbar
import android.graphics.Paint


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
        val intent = Intent(requireActivity(), RateBookActivity::class.java)
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
        val intent = Intent(requireActivity(), BookReviewsActivity::class.java)
        startActivity(intent)
    }

    override fun viewBookChaptersScreen() {
        val intent = Intent(requireActivity(), BookChaptersActivity::class.java)
        startActivity(intent)
    }

    override fun showGiveGiftScreen() {
        val intent = Intent(requireActivity(), GiftSelectionActivity::class.java)
        startActivity(intent)
    }

    override fun showLoginMessage(message: String) {
        if (activity != null)
            Snackbar.make(
                requireActivity().findViewById(android.R.id.content),
                message,
                Snackbar.LENGTH_LONG
            ).show()
        Handler().postDelayed({
            val mainIntent = Intent(requireActivity(), LoginActivity::class.java)
            requireActivity().startActivity(mainIntent)
        }, 500)
    }

    override fun showMessage(message: String) {
        if (activity != null)
            Snackbar.make(
                requireActivity().findViewById(android.R.id.content),
                message,
                Snackbar.LENGTH_LONG
            ).show()
    }

    override fun setBookReviews(reviews: List<Review>) {
        recyclerReviews?.let {
            if (reviews.isNotEmpty()) {
                it.layoutManager = LinearLayoutManager(context)
                it.setHasFixedSize(true)
                it.isNestedScrollingEnabled = false
                it.adapter = ReviewListAdapter(reviews, ReviewListAdapter.Display.SEMI)
            }
        }
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

        getPlayingObserver().observe(viewLifecycleOwner, Observer {
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

            if (it.data.price_after_sale != 0) {
                tvPriceLabel.setPaintFlags(tvPriceLabel.getPaintFlags() or Paint.STRIKE_THRU_TEXT_FLAG)
                tvPrice.setPaintFlags(tvPriceLabel.getPaintFlags() or Paint.STRIKE_THRU_TEXT_FLAG)
                tvPrice.text = it.data.price.toString() + " EGP."
                tvPriceAfterSale.text = it.data.price_after_sale.toString() + " EGP."
            } else {
                tvPrice.text = it.data.price.toString() + " EGP."
                linearPriceAfterSale.visibility = View.GONE
            }

            ImageUtils.setImageFromUrlIntoImageViewUsingGlide(
                it.data.cover,
                requireActivity().applicationContext,
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