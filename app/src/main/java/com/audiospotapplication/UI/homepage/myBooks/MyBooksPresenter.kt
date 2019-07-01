package com.audiospotapplication.UI.homepage.myBooks

import android.os.Handler
import com.audiospot.DataLayer.Model.Book
import com.audiospotapplication.DataLayer.DataRepository

import com.visionvalley.letuno.DataLayer.RepositorySource

class MyBooksPresenter(val mView: MyBooksContract.View) : MyBooksContract.Presenter {

    override fun saveBook(book: Book) {
        mRepositorySource.saveBook(book)
        mView.showBookDetailsScreen()
    }

    override fun start() {

        Handler().postDelayed({
            mRepositorySource = DataRepository.getInstance(mView.getAppContext()!!)
            var listMyBooks = mRepositorySource.getMyBooks()
            if (listMyBooks != null && listMyBooks.isNotEmpty()) {
                mView.setBookList(listMyBooks)
            } else {
                if (mRepositorySource.getAuthResponse() != null) {
                    mView.showEmptyBooksScreen("You have no books yet")
                } else {
                    mView.showEmptyBooksScreen("You have to le be logged in")
                }
            }
        }, 100)
    }

    lateinit var mRepositorySource: RepositorySource
}