package com.audiospotapplication.ui.myBookmarks

import android.content.Context
import com.audiospotapplication.ui.BaseView
import com.audiospotapplication.data.model.Bookmark

interface MyBookmarksContract {

    interface Presenter {
        fun start()
        fun saveBookmark(bookmark: Bookmark)
    }

    interface View : BaseView {
        fun getAppContext(): Context?
        fun showLoading()
        fun dismissLoading()
        fun showMessage(message: String)
        fun finalizeView()
        fun setBookmarks(data: List<Bookmark>)
        fun showBookChaptersScreen()
    }
}