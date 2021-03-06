package com.audiospotapplication.ui.books

import com.audiospot.DataLayer.Model.Book
import com.audiospotapplication.data.DataRepository
import com.audiospotapplication.data.model.BookListResponse
import com.audiospotapplication.data.retrofit.RetrofitCallbacks
import com.audiospotapplication.data.retrofit.RetrofitResponseHandler
import com.audiospotapplication.data.RepositorySource
import retrofit2.Call

class BooksPresenter(val mView: BooksContract.View) : BooksContract.Presenter {
    override fun saveBook(book: Book) {
        mRepositorySource.saveBook(book)
        mView.showBookDetailsScreen()
    }

    var book: String = ""
    var sort: String = ""
    var author: Int = 0
    var publisher: Int = 0
    var narrator: Int = 0
    var category: Int = 0

    override fun handleOnActivityStopped() {
        mRepositorySource.clearBookFilters()
    }

    override fun handleSortBookItemClicked(sortType: String) {
        mView.showLoadingDialog()
        sort = sortType
        getBooks()
    }

    lateinit var mRepositorySource: RepositorySource

    override fun start() {
        mRepositorySource = DataRepository.getInstance(mView.getAppContext()!!)
        mView.showLoadingDialog()

        if (mRepositorySource.getCurrentCategoryItem() != null) {
            category = mRepositorySource.getCurrentCategoryItem()!!.id
        }
        getBooks()
    }

    private fun getBooks() {
        mRepositorySource.getBooks(book,
            sort,
            author,
            publisher,
            narrator,
            category,
            object : RetrofitCallbacks.BookListCallback {

                override fun onSuccess(result: BookListResponse?) {
                    mView.dismissLoadingDialog()
                    val status = RetrofitResponseHandler.validateAuthResponseStatus(result)
                    if (status == RetrofitResponseHandler.Companion.Status.VALID) {
                        mView.setBooksList(result)
                    } else if (status == RetrofitResponseHandler.Companion.Status.UNAUTHORIZED) {
                        mView!!.showLoginPage()
                    } else {
                        mView!!.showErrorMessage(result!!.message)
                    }
                }

                override fun onFailure(call: Call<BookListResponse>?, t: Throwable?) {
                    mView.dismissLoadingDialog()
                    mView!!.showErrorMessage("Server Error ,please try again later")
                }
            })
    }
}