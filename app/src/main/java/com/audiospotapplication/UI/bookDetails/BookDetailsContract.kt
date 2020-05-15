package com.audiospotapplication.UI.bookDetails

import android.content.Context
import com.audiospot.DataLayer.Model.Book
import com.audiospot.DataLayer.Model.BookDetailsResponse
import com.audiospotapplication.BaseView
import com.audiospotapplication.DataLayer.Model.Review

interface BookDetailsContract {

    interface Presenter {
        fun start()
        fun addToCart()
        fun addToFavorites()
        fun handleGiveGiftClicked()
        fun handleViewBookChaptersClicked()
        fun handleSeeAllReviewsClicked()
        fun handlePlayClicked()
        fun isBookMine(): Boolean
        fun getCurrentSampleBookPlaylistId(): String
    }

    interface View : BaseView {
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
        fun playSong(mediaMetaData: Book?)
        fun setCartNumber(size: Int?)
    }
}