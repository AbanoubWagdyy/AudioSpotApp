package com.audiospotapp.UI.myBookmarks

import android.content.Context
import com.audiospot.DataLayer.Model.Book
import com.audiospotapp.DataLayer.Model.AuthorsData
import com.audiospotapp.DataLayer.Model.AuthorsResponse
import com.audiospotapp.DataLayer.Model.BookListResponse
import com.audiospotapp.DataLayer.Model.Bookmark

interface MyBookmarksContract {

    interface Presenter {
        fun start()
        fun saveBookmark(bookmark: Bookmark)
    }

    interface View {
        fun getAppContext(): Context?
        fun showLoading()
        fun dismissLoading()
        fun showMessage(message: String)
        fun finalizeView()
        fun setBookmarks(data: List<Bookmark>)
        fun showBookChaptersScreen()
    }
}