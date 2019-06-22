package com.audiospotapp.UI.homepage.myBooks

import com.audiospot.DataLayer.Model.Book
import com.audiospotapp.DataLayer.DataRepository

import com.visionvalley.letuno.DataLayer.RepositorySource

class MyBooksPresenter(val mView: MyBooksContract.View) : MyBooksContract.Presenter {

    override fun saveBook(book: Book) {
        mRepositorySource.saveBook(book)
        mView.showBookDetailsScreen()
    }

    override fun start() {
        mRepositorySource = DataRepository.getInstance(mView.getAppContext())
        var listMyBooks = mRepositorySource.getMyBooks()
        if (listMyBooks != null && listMyBooks.isNotEmpty()) {
            mView.setBookList(listMyBooks)
        }
    }

    lateinit var mRepositorySource: RepositorySource
}