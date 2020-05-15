package com.audiospotapplication.ui.bookDetails

import com.audiospot.DataLayer.Model.Book
import com.audiospot.DataLayer.Model.BookDetailsResponse
import com.audiospotapplication.data.DataRepository
import com.audiospotapplication.data.model.*
import com.audiospotapplication.data.retrofit.RetrofitCallbacks
import com.audiospotapplication.data.retrofit.RetrofitResponseHandler
import com.audiospotapplication.R
import com.visionvalley.letuno.DataLayer.RepositorySource
import retrofit2.Call

class BookDetailsPresenter(val mView: BookDetailsContract.View?) : BookDetailsContract.Presenter {

    override fun getCurrentSampleBookPlaylistId(): String {
        return currentSamplePlaylistId
    }

    override fun isBookMine(): Boolean {
        return mRepositorySource.isBookMine()
    }

    override fun handlePlayClicked() {
        if (isBookMine()) {
            mRepositorySource.setIsPlayFirstChapter(true)
        }

        mView?.playSong(mRepositorySource.getSavedBook())
    }

    override fun handleSeeAllReviewsClicked() {
        val authResponse = mRepositorySource.getAuthResponse()
        if (authResponse == null) {
            mView?.showLoginMessage("You have to be Logged In First !.")
        } else {
            if (reviews != null && reviews.isNotEmpty()) {
                mView?.viewAllReviewsScreen()
            } else {
                mView?.showMessage("No Reviews detected !.")
            }
        }
    }

    override fun handleViewBookChaptersClicked() {
        mView?.showLoadingDialog()
        mRepositorySource.getBookChapters(object : RetrofitCallbacks.ChaptersResponseCallback {
            override fun onSuccess(result: ChaptersResponse?) {
                mView?.dismissLoading()
                val status = RetrofitResponseHandler.validateResponseStatus(result)
                if (status == RetrofitResponseHandler.Companion.Status.VALID) {
                    result?.let {
                        mRepositorySource.setMediaItems(it.data)
                        mView?.viewBookChaptersScreen()
                    }
                }
            }

            override fun onFailure(call: Call<ChaptersResponse>?, t: Throwable?) {
                mView?.dismissLoading()
            }
        })
    }

    override fun handleGiveGiftClicked() {
        mView?.showGiveGiftScreen()
    }

    override fun addToCart() {
        if (mRepositorySource.isBookMine()) {
            mView?.viewRateBookScreen()
        } else {
            mView?.showLoadingDialog()
            mRepositorySource.addBookToCart(object : RetrofitCallbacks.ResponseCallback {

                override fun onAuthFailure() {
                    mView?.dismissLoading()
                    mView?.showLoginMessage("You have to be Logged In First !.")
                }

                override fun onSuccess(result: Response?) {
                    val status = RetrofitResponseHandler.validateAuthResponseStatus(result)
                    if (status == RetrofitResponseHandler.Companion.Status.VALID) {
                        mRepositorySource.getMyCart(object : RetrofitCallbacks.BookListCallback {
                            override fun onSuccess(result: BookListResponse?) {
                                mView?.dismissLoading()
                                val status =
                                    RetrofitResponseHandler.validateAuthResponseStatus(result)
                                if (status == RetrofitResponseHandler.Companion.Status.VALID) {
                                    mView?.setCartNumber(result?.data?.size)
                                } else if (status == RetrofitResponseHandler.Companion.Status.UNAUTHORIZED) {
                                    mView?.showLoginPage()
                                } else {
                                    mView?.showMessage(result!!.message)
                                }
                            }

                            override fun onFailure(call: Call<BookListResponse>?, t: Throwable?) {
                                mView?.dismissLoading()
                            }
                        })
                    } else if (status == RetrofitResponseHandler.Companion.Status.UNAUTHORIZED) {
                        mView?.dismissLoading()
                        mView?.showLoginPage()
                    } else {
                        mView?.dismissLoading()
                        mView?.showMessage(result!!.message)
                    }
                }

                override fun onFailure(call: Call<Response>?, t: Throwable?) {
                    mView?.dismissLoading()
                    mView?.showMessage("Please try again later")
                }
            })
        }
    }

    override fun addToFavorites() {
        mView?.showLoadingDialog()
        mRepositorySource.addBookToFavorites(object : RetrofitCallbacks.ResponseCallback {
            override fun onAuthFailure() {
                mView?.dismissLoading()
                mView?.showLoginMessage("You have to be Logged In First !.")
            }

            override fun onSuccess(result: Response?) {
                mView?.dismissLoading()
                mView?.showMessage(result!!.message)
            }

            override fun onFailure(call: Call<Response>?, t: Throwable?) {
                mView?.dismissLoading()
                mView?.showMessage("Please try again later")
            }
        })
    }

    override fun start() {
        mRepositorySource = DataRepository.getInstance(mView?.getAppContext()!!)
        mView?.showLoadingDialog()

        if (mRepositorySource.isBookMine()) {
            mView?.hideAddFavoritesButton()
            mView?.setAddToCartText(mView?.getAppContext()!!.getString(R.string.rate_book))
        }

        currentSamplePlaylistId =
            mRepositorySource.getSavedBook()?.id.toString() + mRepositorySource.getSavedBook()?.id.toString()

        mRepositorySource.getBookDetails(object : RetrofitCallbacks.BookDetailsResponseCallback {
            override fun onSuccess(result1: BookDetailsResponse?) {
                mView?.dismissLoading()
                if (result1 != null) {
                    var status = RetrofitResponseHandler.validateAuthResponseStatus(result1)
                    when (status) {
                        RetrofitResponseHandler.Companion.Status.VALID -> {
                            bookDetails = result1.data
                            mView?.bindResponse(result1)
                        }
                        RetrofitResponseHandler.Companion.Status.UNAUTHORIZED -> mView?.showLoginPage()
                        else -> mView?.showMessage(result1.message)
                    }
                } else {
                    mView?.dismissLoading()
                    mView?.showMessage("Please try again later")
                }
            }

            override fun onFailure(call: Call<BookDetailsResponse>?, t: Throwable?) {
                mView?.dismissLoading()
                mView?.showMessage("Please try again later")
            }
        })

        mRepositorySource.getBookReviews(object : RetrofitCallbacks.ReviewListResponseCallback {
            override fun onSuccess(result: ReviewListResponse?) {
                if (result != null) {
                    val status = RetrofitResponseHandler.validateAuthResponseStatus(result)
                    when (status) {
                        RetrofitResponseHandler.Companion.Status.VALID -> {
                            reviews = result!!.data
                            mView?.setBookReviews(result!!.data)
                        }
                        RetrofitResponseHandler.Companion.Status.UNAUTHORIZED -> mView?.showLoginPage()
                        else -> mView?.showMessage(result.message)
                    }
                }
            }

            override fun onFailure(call: Call<ReviewListResponse>?, t: Throwable?) {
                mView?.showMessage("Please try again later")
            }
        })
    }

    private lateinit var currentSamplePlaylistId: String
    lateinit var reviews: List<Review>
    lateinit var mRepositorySource: RepositorySource
    lateinit var bookDetails: Book
}