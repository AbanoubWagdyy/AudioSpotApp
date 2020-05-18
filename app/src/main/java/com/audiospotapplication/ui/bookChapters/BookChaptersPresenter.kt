package com.audiospotapplication.ui.bookChapters

import android.os.Handler
import com.audiospotapplication.data.DataRepository
import com.audiospotapplication.data.model.BookmarkBody
import com.audiospotapplication.data.model.ChaptersData
import com.audiospotapplication.data.model.Paragraph
import com.downloader.OnDownloadListener
import com.downloader.PRDownloader
import com.downloader.PRDownloaderConfig
import com.facebook.FacebookSdk.getApplicationContext
import com.snatik.storage.Storage
import com.audiospotapplication.data.RepositorySource
import java.io.File


class BookChaptersPresenter(val mView: BookChaptersContract.View) : BookChaptersContract.Presenter {

    override fun setCurrentChapterTitle(title: String) {
        currentChapterT = title
    }

    override fun getChapters(): List<ChaptersData>? {
        return mRepoSource.getCurrentBookChapters()
    }

    override fun setCurrentChapterID(id: Int) {
        this.currentChapterId = id
    }

    override fun getCurrentChapterParagraphs(): List<Paragraph>? {
        return currentParagraphs
    }

    override fun setCurrentChapterParagraphs(paragraphs: List<Paragraph>) {
        this.currentParagraphs = paragraphs
    }

    override fun getBookAuthor(): String {
        return mRepoSource.getSavedBook()?.author!!
    }

    override fun getBookReleasedDate(): String {
        return mRepoSource.getSavedBook()?.release_date!!
    }

    override fun getBookIcon(): String {
        return mRepoSource.getSavedBook()?.cover!!
    }

    override fun getBookByID(mediaId: String?): ChaptersData? {
        val items = chapters?.filter {
            it.id == mediaId?.toInt()
        }
        if (items.isNullOrEmpty())
            return null

        return items[0]
    }

    override fun getSavedBookId(): String {
        return mRepoSource.getSavedBook()?.id.toString()
    }

    override fun resetRepo() {
        mRepoSource.clear()
    }

    override fun validateChapterDownloaded(mediaData: ChaptersData): String {
        val storage = Storage(mView.getAppContext())
        val path = storage.internalFilesDirectory

        val newDir = path + File.separator + "AudioSpotDownloadsCache"

        val fileNameStr = mediaData.id.toString() + "-" + mediaData.title

        return if (storage.isFileExist("$newDir/$fileNameStr")) {
            currentPath = "$newDir/$fileNameStr"
            currentPath
        } else {
            ""
        }
    }

    override fun handleDownloadPressed() {

        storage = Storage(mView.getAppContext())
        if (chapters != null) {
            val chapter = chapters!!.filter {
                it.id == currentChapterId
            }[0]

            val path = storage.internalFilesDirectory

            val newDir = path + File.separator + "AudioSpotDownloadsCache"
            val fileNameStr = chapter.id.toString() + "-" + chapter.title

            currentPath = "$newDir/$fileNameStr"

            if (storage.isFileExist(currentPath)) {
                mView.showMessage("Already Downloaded")
                return
            }

            mView.getAppContext()?.let {
                mView.showDownloadingDialog()
                PRDownloader.download(chapter.sound_file, newDir, fileNameStr)
                    .build()
                    .setOnStartOrResumeListener { }
                    .setOnPauseListener { }
                    .setOnCancelListener {
                        storage.deleteFile(currentPath)
                        mView.dismissDownloadingDialog()
                        mView.refreshAdapter()
                    }
                    .setOnProgressListener {
                        mView.updateProgress((it.currentBytes * 100 / it.totalBytes).toInt())
                    }
                    .start(object : OnDownloadListener {

                        override fun onDownloadComplete() {
                            mView.dismissDownloadingDialog()
                            mView.showMessage("Download Complete")
                            mView.refreshAdapter()
                        }

                        override fun onError(error: com.downloader.Error?) {
                            storage.deleteFile(currentPath)
                            mView.dismissDownloadingDialog()
                            mView.refreshAdapter()
                        }
                    })
            }
        }
    }

    override fun handleBookmarkClicked(timeString: String) {
        val savedbook = mRepoSource.getSavedBook()

        val bookmarkBody = BookmarkBody()
        bookmarkBody.bookmarkTime = timeString
        bookmarkBody.chapter_id = currentChapterId
        if (savedbook != null) {
            bookmarkBody.bookId = savedbook.id
        }
        if (savedbook != null) {
            bookmarkBody.url = savedbook.cover
        }
        bookmarkBody.chapter_name = currentChapterT
        if (savedbook != null) {
            bookmarkBody.bookName = savedbook.title
        }

        mRepoSource.setBookmarkData(bookmarkBody)

        mView.showAddBookmarkScreen()
    }

    override fun isBookMine(): Boolean {
        return mRepoSource.isBookMine()
    }

    override fun start() {

        val config = PRDownloaderConfig.newBuilder()
            .setDatabaseEnabled(true)
            .setReadTimeout(30000)
            .setConnectTimeout(30000)
            .build()

        PRDownloader.initialize(getApplicationContext(), config)

        mRepoSource = DataRepository.getInstance(mView.getAppContext()!!)

        val book = mRepoSource.getSavedBook()

        val bookmark = mRepoSource.getBookmark()

        val isToPlayFirstChapter = mRepoSource.getIsPlayFirstChapter()

        chapters = mRepoSource.getCurrentBookChapters()

        if (chapters != null) {
            if (book != null) {
                mView.setBookName(book.title)
                mView.setBookImage(book.cover)
                mView.setChapters(chapters!!, book.id)
            }

            Handler().postDelayed({
                if (isToPlayFirstChapter) {
                    mView.playChapter(chapters!![0], 0)
                    mRepoSource.setIsPlayFirstChapter(false)
                }

                if (bookmark != null) {
                    val chapterData = chapters!!.filter {
                        it.id == bookmark.chapter_id
                    }[0]
                    val index = chapters!!.indexOfFirst {
                        it.id == bookmark.chapter_id
                    }
                    mView.setBookmark(bookmark)
                    mView.playChapter(chapterData, index)
                }
            }, 1500)
        } else {
            mView.showHomepageScreen()
        }
    }

    private lateinit var storage: Storage
    private var currentChapterId: Int = 0
    private var currentChapterT: String = ""
    private var currentParagraphs: List<Paragraph>? = null
    private var chapters: List<ChaptersData>? = null
    private lateinit var currentPath: String
    private lateinit var mRepoSource: RepositorySource
}