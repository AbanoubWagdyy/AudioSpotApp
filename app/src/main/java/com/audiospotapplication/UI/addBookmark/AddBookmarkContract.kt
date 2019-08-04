package com.audiospotapplication.UI.addBookmark

import android.content.Context
import com.audiospotapplication.BaseView

interface AddBookmarkContract {

    interface Presenter {
        fun start()
        fun addBookmark(toString: String)
        fun resetRepo()
    }

    interface View :BaseView{
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