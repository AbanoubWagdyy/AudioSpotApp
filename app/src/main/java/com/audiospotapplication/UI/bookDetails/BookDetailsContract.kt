package com.audiospotapplication.UI.bookDetails

import android.content.Context
import com.audiospot.DataLayer.Model.Book
import com.audiospot.DataLayer.Model.BookDetailsResponse
import com.audiospotapplication.DataLayer.Model.Review
import dm.audiostreamer.MediaMetaData

interface BookDetailsContract {

    interface Presenter {
        fun start()
        fun addToCart()
        fun addToFavorites()
        fun handleGiveGiftClicked()
        fun handleViewBookChaptersClicked()
        fun handleSeeAllReviewsClicked()
        fun handlePlayClicked()
        fun getSavedBook(): Book?
    }

    interface View {
        fun getAppContext(): Context?
        fun showLoadingDialog()
        fun dismissLoading()
        fun bindResponse(result: BookDetailsResponse?)
        fun setBookReviews(reviews: List<Review>)
        fun showMessage(message: String)
        fun showLoginMessage(message: String)
        fun showGiveGiftScreen()
        fun viewBookChaptersScreen()
        fun viewAllReviewsScreen()
        fun hideAddFavoritesButton()
        fun setAddToCartText(s: String)
        fun viewRateBookScreen()
        fun playSong(mediaMetaData: MediaMetaData)
        fun validatePlayResouce(result1: BookDetailsResponse)
    }
}