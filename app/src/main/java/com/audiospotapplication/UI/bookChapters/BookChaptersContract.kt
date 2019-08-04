package com.audiospotapplication.UI.bookChapters

import android.content.Context
import com.audiospotapplication.BaseView
import com.audiospotapplication.DataLayer.Model.Bookmark
import com.audiospotapplication.DataLayer.Model.ChaptersData
import com.example.jean.jcplayer.model.JcAudio

interface BookChaptersContract {

    interface Presenter {
        fun start()
        fun handleBookmarkClicked(timeString: String, id: Int, title: String)
        fun isBookMine(): Boolean
        fun handleDownloadPressed(currentSong: JcAudio?)
        fun validateChapterDownloaded(mediaData: ChaptersData): String
        fun resetRepo()
    }

    interface View : BaseView {
        fun getAppContext(): Context?
        fun showLoadingDialog()
        fun dismissLoading()
        fun setChapters(data: List<ChaptersData>)
        fun setBookName(title: String)
        fun setBookImage(cover: String)
        fun showAddBookmarkScreen()
        fun setBookmark(bookmark: Bookmark)
        fun onChapterClicked(chapterData: ChaptersData)
        fun showDownloadComplete(message: String, currentPath: String)
        fun showMessage(s: String)
        fun showDownloadingDialog()
        fun updateProgress(progress: Int)
        fun dismissDownloadingDialog()
    }
}