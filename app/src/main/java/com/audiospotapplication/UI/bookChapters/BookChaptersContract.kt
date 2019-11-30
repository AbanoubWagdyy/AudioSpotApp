package com.audiospotapplication.UI.bookChapters

import android.content.Context
import com.audiospotapplication.BaseView
import com.audiospotapplication.DataLayer.Model.Bookmark
import com.audiospotapplication.DataLayer.Model.ChaptersData
import com.audiospotapplication.DataLayer.Model.Paragraph

interface BookChaptersContract {

    interface Presenter {

        fun start()
        fun handleBookmarkClicked(timeString: String)
        fun handleDownloadPressed()
        fun isBookMine(): Boolean
        fun validateChapterDownloaded(mediaData: ChaptersData): String
        fun resetRepo()
        fun getSavedBookId(): String
        fun getBookByID(mediaId: String?): ChaptersData?
        fun getBookAuthor(): String
        fun getBookReleasedDate(): String
        fun getBookIcon(): String
        fun setCurrentChapterParagraphs(paragraphs: List<Paragraph>)
        fun getCurrentChapterParagraphs(): List<Paragraph>?
        fun setCurrentChapterID(id: Int)
        fun setCurrentChapterTitle(title: String)
        fun getChapters(): List<ChaptersData>
    }

    interface View : BaseView {
        fun getAppContext(): Context?
        fun showLoadingDialog()
        fun dismissLoading()
        fun setChapters(
            data: List<ChaptersData>,
            id: Int
        )
        fun setBookName(title: String)
        fun setBookImage(cover: String)
        fun showAddBookmarkScreen()
        fun setBookmark(bookmark: Bookmark)
        fun showDownloadComplete(message: String, currentPath: String)
        fun showMessage(s: String)
        fun showDownloadingDialog()
        fun updateProgress(progress: Int)
        fun dismissDownloadingDialog()
        fun showHomepageScreen()
        fun playChapter(chaptersData: ChaptersData, index: Int)
    }
}