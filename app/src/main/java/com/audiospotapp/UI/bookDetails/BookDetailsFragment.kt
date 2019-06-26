package com.audiospotapp.UI.bookDetails


import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.audiospotapp.R
import kotlinx.android.synthetic.main.fragment_book_details.*
import android.text.style.UnderlineSpan
import android.text.SpannableString
import androidx.recyclerview.widget.LinearLayoutManager
import com.audiospot.DataLayer.Model.BookDetailsResponse
import com.audiospotapp.DataLayer.Model.Review
import com.audiospotapp.UI.bookChapters.BookChaptersActivity
import com.audiospotapp.UI.bookDetails.adapter.ReviewListAdapter
import com.audiospotapp.UI.bookReviews.BookReviewsActivity
import com.audiospotapp.UI.giftSelection.GiftSelectionActivity
import com.audiospotapp.UI.giveAgift.GiveGiftActivity
import com.audiospotapp.UI.login.LoginActivity
import com.audiospotapp.UI.rateBook.RateBookActivity
import com.audiospotapp.UI.splash.SplashActivity
import com.audiospotapp.utils.DialogUtils
import com.audiospotapp.utils.ImageUtils
import com.audiospotapp.utils.TimeUtils
import com.google.android.material.snackbar.Snackbar
import dm.audiostreamer.AudioStreamingManager
import dm.audiostreamer.MediaMetaData

class BookDetailsFragment : Fragment(), BookDetailsContract.View {

    override fun playSong(mediaMetaData: MediaMetaData) {
        streamingManager = AudioStreamingManager.getInstance(activity!!.applicationContext)

        streamingManager!!.isPlayMultiple = false

        var list = ArrayList<MediaMetaData>()
        list.add(mediaMetaData)

        streamingManager!!.setMediaList(list)

        streamingManager!!.setShowPlayerNotification(true)
        streamingManager!!.setPendingIntentAct(getNotificationPendingIntent())

        if (streamingManager != null) {
            streamingManager!!.onPlay(mediaMetaData)
        }
    }

    private fun getNotificationPendingIntent(): PendingIntent {
        val intent = Intent(activity!!.applicationContext, SplashActivity::class.java)
        intent.setAction("openplayer")
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        return PendingIntent.getActivity(activity!!.applicationContext, 0, intent, 0)
    }

    override fun viewRateBookScreen() {
        val intent = Intent(activity!!, RateBookActivity::class.java)
        startActivity(intent)
    }

    override fun hideAddFavoritesButton() {
        relativeAddToFavourites.visibility = View.GONE
    }

    override fun setAdToCartText(s: String) {
        tvAddToCart.text = s
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
        Snackbar.make(activity!!.findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG).show()
        Handler().postDelayed({
            val mainIntent = Intent(activity!!, LoginActivity::class.java)
            activity!!.startActivity(mainIntent)
        }, 500)
    }

    override fun showMessage(message: String) {
        Snackbar.make(activity!!.findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG).show()
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

    private lateinit var streamingManager: AudioStreamingManager
    lateinit var mPresenter: BookDetailsContract.Presenter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_book_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var content = SpannableString("See All Reviews")
        content.setSpan(UnderlineSpan(), 0, content.length, 0)
        tvSeeAllReviews.text = content
        content = SpannableString("View Book Chapters")
        content.setSpan(UnderlineSpan(), 0, content.length, 0)
        tvViewBookChapters.text = content

        tvSeeAllReviews.setOnClickListener {
            mPresenter.handleSeeAllReviewsClicked()
        }

        tvViewBookChapters.setOnClickListener {
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
    }

    override fun bindResponse(result: BookDetailsResponse?) {

        ratingBar.rating = result!!.data.rate.toFloat()

        ratingBar.setOnRatingChangeListener(null)
        ratingBar.setOnTouchListener { p0, p1 -> true }

        ratingBar.setOnRatingChangeListener { ratingBar, rating, fromUser ->
            ratingBar.rating = result.data.rate.toFloat()
        }

        tvBookTitle.text = result!!.data.title
        tvNumberOfReviews.text = "(" + result!!.data.reviews.toString() + " reviews" + ")"
        tvAuthor.text = result!!.data.author
        if (result.data.narators.isNotEmpty()) {
            val builder = StringBuilder()
            for (narator in result.data.narators) {
                builder.append(narator.name).append(",")
            }
            tvNarrator.text = builder.toString().substring(0, builder.toString().length - 1)
        }
        tvCategory.text = result!!.data.category
        tvPublisher.text = result!!.data.publisher
        tvAbout.text = result!!.data.about_book

        tvLength.text = result!!.data.total_time_trt + " Hours"

        tvPrice.text = result!!.data.price.toString() + " EGP."

        ImageUtils.setImageFromUrlIntoImageViewUsingPicasso(result.data.cover, activity!!.applicationContext, ivBook)
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            BookDetailsFragment()
    }
}