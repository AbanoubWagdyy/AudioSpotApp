package com.audiospotapplication.UI.bookDetails


import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.audiospotapplication.R
import kotlinx.android.synthetic.main.fragment_book_details.*
import android.text.style.UnderlineSpan
import android.text.SpannableString
import androidx.recyclerview.widget.LinearLayoutManager
import com.audiospot.DataLayer.Model.Book
import com.audiospot.DataLayer.Model.BookDetailsResponse
import com.audiospotapplication.DataLayer.Model.Review
import com.audiospotapplication.UI.bookChapters.BookChaptersActivity
import com.audiospotapplication.UI.bookDetails.adapter.ReviewListAdapter
import com.audiospotapplication.UI.bookReviews.BookReviewsActivity
import com.audiospotapplication.UI.giftSelection.GiftSelectionActivity
import com.audiospotapplication.UI.login.LoginActivity
import com.audiospotapplication.UI.rateBook.RateBookActivity
import com.audiospotapplication.UI.splash.SplashActivity
import com.audiospotapplication.utils.BookDataConversion
import com.audiospotapplication.utils.DialogUtils
import com.audiospotapplication.utils.ImageUtils
import com.example.jean.jcplayer.JcPlayerManager
import com.example.jean.jcplayer.JcPlayerManagerListener
import com.example.jean.jcplayer.general.JcStatus
import com.example.jean.jcplayer.model.JcAudio
import com.google.android.material.snackbar.Snackbar
import dm.audiostreamer.AudioStreamingManager
import dm.audiostreamer.MediaMetaData

class BookDetailsFragment : Fragment(), BookDetailsContract.View, JcPlayerManagerListener {
    override fun onPreparedAudio(status: JcStatus) {

    }

    override fun onCompletedAudio() {

    }

    override fun onPaused(status: JcStatus) {

    }

    override fun onContinueAudio(status: JcStatus) {

    }

    override fun onPlaying(status: JcStatus) {

    }

    override fun onTimeChanged(status: JcStatus) {

    }

    override fun onStopped(status: JcStatus) {

    }

    override fun onJcpError(throwable: Throwable) {

    }

    override fun validatePlayResouce(result1: BookDetailsResponse) {
        if (jcPlayerManager.isPlaying()) {
            var currentAudio = jcPlayerManager.currentAudio

            if (result1.data.sample.equals(currentAudio!!.path)) {
                ivPlay.setBackgroundResource(R.mipmap.homepage_play)
                ivPlay.setTag(R.mipmap.homepage_play)
            } else {
                ivPlay.setBackgroundResource(R.mipmap.play)
                ivPlay.setTag(R.mipmap.play)
            }
        }
    }

    override fun playSong(mediaData: MediaMetaData) {
        if (mediaData == null || mediaData.mediaUrl == null || mediaData.mediaUrl.equals("")) {
            Snackbar.make(
                activity!!.findViewById(android.R.id.content), "Audio is not available right now ," +
                        "please check again later", Snackbar.LENGTH_LONG
            ).show()
            return
        }
        jcPlayerManager.kill()

        var audio = JcAudio.createFromURL(
            mediaData.mediaId.toInt(), mediaData.mediaTitle,
            mediaData.mediaUrl, null
        )

        var playlist = ArrayList<JcAudio>()
        playlist.add(audio)

        jcPlayerManager.playlist = playlist
        jcPlayerManager.jcPlayerManagerListener = this

        jcPlayerManager.playAudio(audio)
        jcPlayerManager.createNewNotification(R.mipmap.ic_launcher)

    }

    override fun viewRateBookScreen() {
        val intent = Intent(activity!!, RateBookActivity::class.java)
        startActivity(intent)
    }

    override fun hideAddFavoritesButton() {
        relativeAddToFavourites.visibility = View.GONE
    }

    override fun setAddToCartText(addToCartStr: String) {
        tvAddToCart.text = addToCartStr
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
            var currentAudio = jcPlayerManager.currentAudio
            if (jcPlayerManager.isPlaying()) {
                if (currentAudio?.path.equals(mPresenter.getSavedBook()!!.sample)) {
                    jcPlayerManager.pauseAudio()
                    ivPlay.setBackgroundResource(R.mipmap.play)
                    ivPlay.setTag(R.mipmap.play)
                } else {
                    mPresenter.handlePlayClicked()
                }
            } else {
                if (currentAudio?.path.equals(mPresenter.getSavedBook()!!.sample)) {
                    jcPlayerManager.continueAudio()
                    ivPlay.setBackgroundResource(R.mipmap.homepage_play)
                    ivPlay.setTag(R.mipmap.homepage_play)
                } else {
                    mPresenter.handlePlayClicked()
                }
            }
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

        tvLength.text = result!!.data.total_time_trt + " Hours."

        tvPrice.text = result!!.data.price.toString() + " EGP."

        ImageUtils.setImageFromUrlIntoImageViewUsingPicasso(result.data.cover, activity!!.applicationContext, ivBook)
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            BookDetailsFragment()
    }

    private val jcPlayerManager: JcPlayerManager by lazy {
        JcPlayerManager.getInstance(activity!!.applicationContext).get()!!
    }
}