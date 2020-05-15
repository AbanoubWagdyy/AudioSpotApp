package com.audiospotapplication.ui.bookChapters

import android.os.Handler
import android.util.Log
import com.audiospotapplication.data.DataRepository
import com.audiospotapplication.data.model.BookmarkBody
import com.audiospotapplication.data.model.ChaptersData
import com.audiospotapplication.data.model.Paragraph
import com.snatik.storage.Storage
import com.tonyodev.fetch2.*
import com.tonyodev.fetch2core.DownloadBlock
import com.visionvalley.letuno.DataLayer.RepositorySource
import java.io.File

class BookChaptersPresenter(val mView: BookChaptersContract.View) : BookChaptersContract.Presenter,
    FetchListener {

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

    override fun validateChapterDownloaded(data: ChaptersData): String {
        val storage = Storage(mView.getAppContext())
        val path = storage.internalCacheDirectory

        val newDir = path + File.separator + "AudioSpotDownloadsCache"

        val fileNameStr = data.id

        if (storage.isFileExist("$newDir/$fileNameStr")) {
            currentPath = "$newDir/$fileNameStr"
            return currentPath
        } else {
            return ""
        }
    }

    override fun onAdded(download: Download) {
        Log.d("onAdded", "onAdded")
    }

    override fun onCancelled(download: Download) {
        mView.dismissLoading()
        mView.showDownloadComplete("Error in Item Downloading !.", currentPath)
        val storage = Storage(mView.getAppContext())
        val path = storage.internalCacheDirectory
        val newDir = path + File.separator + "AudioSpotDownloadsCache"
        currentPath = newDir + "/" + download.url
        storage.deleteFile(currentPath)
    }

    override fun onCompleted(download: Download) {
        mView.dismissDownloadingDialog()
        mView.showDownloadComplete("Item Downloaded !.", currentPath)
    }

    override fun onDeleted(download: Download) {
        Log.d("onDeleted", "onDeleted")
    }

    override fun onDownloadBlockUpdated(
        download: Download,
        downloadBlock: DownloadBlock,
        totalBlocks: Int
    ) {
        Log.d("Blocked", "Blocked")
    }

    override fun onError(download: Download, error: Error, throwable: Throwable?) {
        mView.dismissLoading()

        mView.showDownloadComplete("Error in Item Downloading !.", currentPath)
        val storage = Storage(mView.getAppContext())
        val path = storage.internalCacheDirectory

        val newDir = path + File.separator + "AudioSpotDownloadsCache"
        currentPath = newDir + "/" + download.url

        storage.deleteFile(currentPath)
    }

    override fun onPaused(download: Download) {
        Log.d("onPaused", "onPaused")
    }

    override fun onProgress(
        download: Download,
        etaInMilliSeconds: Long,
        downloadedBytesPerSecond: Long
    ) {
        mView.updateProgress(download.progress)
    }

    override fun onQueued(download: Download, waitingOnNetwork: Boolean) {
        Log.d("onQueued", "onQueued")
    }

    override fun onRemoved(download: Download) {
        Log.d("onRemoved", "onRemoved")
    }

    override fun onResumed(download: Download) {
        Log.d("onResumed", "onResumed")
    }

    override fun onStarted(
        download: Download,
        downloadBlocks: List<DownloadBlock>,
        totalBlocks: Int
    ) {
    }

    override fun onWaitingNetwork(download: Download) {
    }

    override fun handleDownloadPressed() {

        val storage = Storage(mView.getAppContext())
        val path = storage.internalCacheDirectory

        if (chapters != null) {
            val chapter = chapters!!.filter {
                it.id == currentChapterId
            }[0]

            val newDir = path + File.separator + "AudioSpotDownloadsCache"
            val fileNameStr = chapter.id

            currentPath = "$newDir/$fileNameStr"

            if (storage.isFileExist(currentPath)) {
                mView.dismissLoading()
                mView.showMessage("Already Downloaded")
                return
            }

            mView.showDownloadingDialog()

            val request = Request(chapter.sound_file, currentPath)
            request.priority = Priority.HIGH
            request.networkType = NetworkType.ALL
            fetch.enqueue(request)
            fetch.addListener(this)
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

            val fetchConfiguration = FetchConfiguration.Builder(mView.getAppContext()!!)
                .setDownloadConcurrentLimit(1)
                .build()

            fetch = Fetch.Impl.getInstance(fetchConfiguration)

        } else {
            mView.showHomepageScreen()
        }
    }

    private var currentChapterId: Int = 0
    private var currentChapterT: String = ""
    private var currentParagraphs: List<Paragraph>? = null
    private var chapters: List<ChaptersData>? = null
    private lateinit var currentPath: String
    lateinit var mRepoSource: RepositorySource
    lateinit var fetch: Fetch
}