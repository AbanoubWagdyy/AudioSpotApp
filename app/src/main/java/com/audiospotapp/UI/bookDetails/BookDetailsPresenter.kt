package com.audiospotapp.UI.bookDetails

import com.audiospot.DataLayer.Model.Book
import com.audiospot.DataLayer.Model.BookDetailsResponse
import com.audiospotapp.DataLayer.DataRepository
import com.audiospotapp.DataLayer.Model.Response
import com.audiospotapp.DataLayer.Model.Review
import com.audiospotapp.DataLayer.Model.ReviewListResponse
import com.audiospotapp.DataLayer.Retrofit.RetrofitCallbacks
import com.audiospotapp.DataLayer.Retrofit.RetrofitResponseHandler
import com.visionvalley.letuno.DataLayer.RepositorySource
import retrofit2.Call

class BookDetailsPresenter(val mView: BookDetailsContract.View) : BookDetailsContract.Presenter {

    override fun handleSeeAllReviewsClicked() {
        var authResponse = mRepositorySource.getAuthResponse()
        if (authResponse == null) {
            mView.showLoginMessage("You have to be Logged In First !.")
        } else {
            if (reviews != null && reviews.isNotEmpty()) {
                mView.viewAllReviewsScreen()
            } else {
                mView.showMessage("No Reviews detected !.")
            }
        }
    }

    override fun handleViewBookChaptersClicked() {
        var authResponse = mRepositorySource.getAuthResponse()
        if (authResponse == null) {
            mView.showLoginMessage("You have to be Logged In First !.")
        } else {
            if (mRepositorySource.isBookMine()) {
                mRepositorySource.saveBook(bookDetails)
                mView.viewBookChaptersScreen()
            } else {
                mView.showMessage("You Should own this book to view all chapters !.")
            }
        }
    }

    override fun handleGiveGiftClicked() {
        var authResponse = mRepositorySource.getAuthResponse()
        if (authResponse == null) {
            mView.showLoginMessage("You have to be Logged In First !.")
        } else {
            if (mRepositorySource.isBookMine()) {
                mRepositorySource.saveBook(bookDetails)
                mView.showGiveGiftScreen()
            } else {
                mView.showMessage("You Should own this book to give this book as a gift !.")
            }
        }
    }

    override fun addToCart() {
        if(mRepositorySource.isBookMine()){
            mView.viewRateBookScreen()
        }else{
            mView.showLoadingDialog()
            mRepositorySource.addBookToCart(object : RetrofitCallbacks.ResponseCallback {

                override fun onAuthFailure() {
                    mView.dismissLoading()
                    mView.showLoginMessage("You have to be Logged In First !.")
                }

                override fun onSuccess(result: Response?) {
                    mView.dismissLoading()
                    mView!!.showMessage(result!!.message)
                }

                override fun onFailure(call: Call<Response>?, t: Throwable?) {
                    mView.dismissLoading()
                    mView!!.showMessage("Please try again later")
                }
            })
        }
    }

    override fun addToFavorites() {
        mView.showLoadingDialog()
        mRepositorySource.addBookToFavorites(object : RetrofitCallbacks.ResponseCallback {
            override fun onAuthFailure() {
                mView.dismissLoading()
                mView.showLoginMessage("You have to be Logged In First !.")
            }

            override fun onSuccess(result: Response?) {
                mView.dismissLoading()
                mView.showMessage(result!!.message)
            }

            override fun onFailure(call: Call<Response>?, t: Throwable?) {
                mView.dismissLoading()
                mView!!.showMessage("Please try again later")
            }
        })
    }

    lateinit var mRepositorySource: RepositorySource
    lateinit var bookDetails: Book

    override fun start() {
        mRepositorySource = DataRepository.getInstance(mView.getAppContext())
        mView.showLoadingDialog()

        if (mRepositorySource.isBookMine()) {
            mView.hideAddFavoritesButton()
            mView.setAdToCartText("Review the book")
        }

        mRepositorySource.getBookDetails(object : RetrofitCallbacks.BookDetailsResponseCallback {
            override fun onSuccess(result1: BookDetailsResponse?) {
                mView.dismissLoading()
                if (result1 != null) {
                    var status = RetrofitResponseHandler.validateAuthResponseStatus(result1)
                    if (status == RetrofitResponseHandler.Companion.Status.VALID) {
                        bookDetails = result1.data
                        mView.bindResponse(result1)
                    } else {
                        mView.showMessage(result1.message)
                    }
                } else {
                    mView.dismissLoading()
                    mView.showMessage("Please try again later")
                }
            }

            override fun onFailure(call: Call<BookDetailsResponse>?, t: Throwable?) {
                mView.dismissLoading()
                mView.showMessage("Please try again later")
            }
        })

        mRepositorySource.getBookReviews(object : RetrofitCallbacks.ReviewListResponseCallback {
            override fun onSuccess(result: ReviewListResponse?) {
                if (result != null) {
                    val status = RetrofitResponseHandler.validateAuthResponseStatus(result)
                    if (status == RetrofitResponseHandler.Companion.Status.VALID) {
                        reviews = result!!.data
                        mView.setBookReviews(result!!.data)
                    } else {
                        mView.showMessage(result.message)
                    }
                } else {
                }
            }

            override fun onFailure(call: Call<ReviewListResponse>?, t: Throwable?) {
                mView.showMessage("Please try again later")
            }
        })
    }

    lateinit var reviews: List<Review>
}