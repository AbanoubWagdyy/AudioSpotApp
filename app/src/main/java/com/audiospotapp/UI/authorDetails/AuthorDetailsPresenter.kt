package com.audiospotapp.UI.authorDetails

import com.audiospot.DataLayer.Model.Book
import com.audiospotapp.DataLayer.DataRepository
import com.audiospotapp.DataLayer.Model.AuthorsData
import com.audiospotapp.DataLayer.Model.AuthorsResponse
import com.audiospotapp.DataLayer.Model.BookListResponse
import com.audiospotapp.DataLayer.Retrofit.RetrofitCallbacks
import com.audiospotapp.DataLayer.Retrofit.RetrofitResponseHandler
import com.visionvalley.letuno.DataLayer.RepositorySource
import retrofit2.Call

class AuthorDetailsPresenter(val mView: AuthorDetailsContract.View) : AuthorDetailsContract.Presenter {

    override fun saveBook(book: Book) {
        mRepositorySource.saveBook(book)
        mView.showBookDetailsScreen()
    }

    lateinit var mRepositorySource: RepositorySource

    override fun start() {
        mView.showLoading()
        mRepositorySource = DataRepository.getInstance(mView.getAppContext())
        var authorItem = mRepositorySource.getAuthorItem()
        mView.setAuthorName(authorItem.name)
        mView.setAuthorImage(authorItem.photo)
        mView.setAuthorBio(authorItem.bio)
        mRepositorySource = DataRepository.getInstance(mView.getAppContext())
        mRepositorySource.getBooks(
            "", "", authorItem.id, 0, 0,
            0,
            object : RetrofitCallbacks.BookListCallback {
                override fun onSuccess(result: BookListResponse?) {
                    mView.dismissLoading()
                    val status = RetrofitResponseHandler.validateAuthResponseStatus(result)
                    if (status == RetrofitResponseHandler.Companion.Status.VALID
                    ) {
                        mView.setBookList(result)
                    } else {
                        mView!!.showErrorMessage(result!!.message)
                    }
                }

                override fun onFailure(call: Call<BookListResponse>?, t: Throwable?) {
                    mView.dismissLoading()
                    mView.showErrorMessage("Please check your internet connection")
                }
            }
        )
    }
}