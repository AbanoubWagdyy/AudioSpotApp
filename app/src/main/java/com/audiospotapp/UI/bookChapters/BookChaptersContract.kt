package com.audiospotapp.UI.bookChapters

import android.content.Context
import com.audiospotapp.DataLayer.Model.Bookmark
import com.audiospotapp.DataLayer.Model.ChaptersData

interface BookChaptersContract {

    interface Presenter {
        fun start()
        fun handleBookmarkClicked(timeString: String, id: Int, title: String)
    }

    interface View {
        fun getAppContext(): Context?
        fun showLoadingDialog()
        fun dismissLoading()
        fun setChapters(data: List<ChaptersData>)
        fun setBookName(title: String)
        fun setBookImage(cover: String)
        fun showAddBookmarkScreen()
        fun setBookmark(bookmark: Bookmark)
        fun onChapterClicked(chapterData: ChaptersData)
    }
}