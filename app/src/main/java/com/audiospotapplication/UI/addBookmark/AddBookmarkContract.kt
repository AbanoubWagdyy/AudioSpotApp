package com.audiospotapplication.UI.addBookmark

import android.content.Context

interface AddBookmarkContract {

    interface Presenter {
        fun start()
        fun addBookmark(toString: String)

    }

    interface View {
        fun getAppContext(): Context?
        fun showLoading()
        fun dismissLoading()
        fun setBookName(bookName: String)
        fun setBookImage(bookName: String)
        fun setChapterName(chapterName: String)
        fun setBookmarkTime(bookmarkTime: String)
        fun showMessage(message: String)
        fun finalizeView()
    }
}