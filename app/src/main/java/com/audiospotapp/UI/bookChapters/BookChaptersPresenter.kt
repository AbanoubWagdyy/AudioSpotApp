package com.audiospotapp.UI.bookChapters

import android.util.Log
import com.audiospotapp.DataLayer.DataRepository
import com.audiospotapp.DataLayer.Model.BookmarkBody
import com.audiospotapp.DataLayer.Model.ChaptersResponse
import com.audiospotapp.DataLayer.Retrofit.RetrofitCallbacks
import com.audiospotapp.DataLayer.Retrofit.RetrofitResponseHandler
import com.snatik.storage.Storage
import com.tonyodev.fetch2.*
import com.tonyodev.fetch2core.DownloadBlock
import com.visionvalley.letuno.DataLayer.RepositorySource
import dm.audiostreamer.MediaMetaData
import retrofit2.Call
import java.io.File


class BookChaptersPresenter(val mView: BookChaptersContract.View) : BookChaptersContract.Presenter, FetchListener {

    override fun validateChapterDownloaded(mediaData: MediaMetaData): String {
        val storage = Storage(mView.getAppContext())
        val path = storage.internalCacheDirectory

        val newDir = path + File.separator + "AudioSpotDownloadsCache"

        var fileNameStr = mediaData!!.mediaUrl.split("/")

        if (storage.isFileExist(newDir + "/" + fileNameStr[fileNameStr.size - 1])) {
            currentPath = newDir + "/" + fileNameStr[fileNameStr.size - 1]
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
        mView.dismissLoading()
        mView.showDownloadComplete("Item Downloaded !.", currentPath)
    }

    override fun onDeleted(download: Download) {
        Log.d("onDEleted", "onDeleted")
    }

    override fun onDownloadBlockUpdated(download: Download, downloadBlock: DownloadBlock, totalBlocks: Int) {
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

    override fun onProgress(download: Download, etaInMilliSeconds: Long, downloadedBytesPerSecond: Long) {
        Log.d("onProgress", "onProgress")
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

    override fun onStarted(download: Download, downloadBlocks: List<DownloadBlock>, totalBlocks: Int) {
        mView.showLoadingDialog()
    }

    override fun onWaitingNetwork(download: Download) {

    }

    override fun handleDownloadPressed(currentSong: MediaMetaData?) {

        val storage = Storage(mView.getAppContext())
        val path = storage.internalCacheDirectory

        val newDir = path + File.separator + "AudioSpotDownloadsCache"
        var fileNameStr = currentSong!!.mediaUrl.split("/")
        currentPath = newDir + "/" + fileNameStr[fileNameStr.size - 1]

        if (storage.isFileExist(currentPath)) {
            mView.showMessage("Item Already Downloaded")
            return
        }
        val request = Request(currentSong!!.mediaUrl, currentPath)
        request.priority = Priority.HIGH
        request.networkType = NetworkType.ALL
        fetch.enqueue(request)
        fetch.addListener(this)
    }

    override fun isBookMine(): Boolean {
        return mRepoSource.isBookMine()
    }

    override fun handleBookmarkClicked(timeString: String, id: Int, title: String) {
        var savedbook = mRepoSource.getSavedBook()

        var bookmarkBody = BookmarkBody()
        bookmarkBody.bookmarkTime = timeString
        bookmarkBody.chapter_id = id
        bookmarkBody.bookId = savedbook.id
        bookmarkBody.url = savedbook.cover
        bookmarkBody.chapter_name = title
        bookmarkBody.bookName = savedbook.title

        mRepoSource.setBookmarkData(bookmarkBody)
        mView.showAddBookmarkScreen()
    }

    override fun start() {

        mRepoSource = DataRepository.getInstance(mView.getAppContext())
        var book = mRepoSource.getSavedBook()
        var bookmark = mRepoSource.getBookmark()

        mView.setBookName(book.title)
        mView.setBookImage(book.cover)
        mView.showLoadingDialog()
        mRepoSource.getBookChapters(object : RetrofitCallbacks.ChaptersResponseCallback {
            override fun onSuccess(result: ChaptersResponse?) {
                mView.dismissLoading()
                val status = RetrofitResponseHandler.validateResponseStatus(result)
                if (status == RetrofitResponseHandler.Companion.Status.VALID) {
                    mView.setChapters(result!!.data)

                    if (bookmark != null) {
//                        mView.setBookmark(bookmark)
//                        mRepoSource.saveBookmark(null)

                        var chapterData = result!!.data.filter {
                            it.id == bookmark!!.chapter_id
                        }[0]

                        mView.setBookmark(bookmark)

                        mView.onChapterClicked(chapterData)
                    }
                }
            }

            override fun onFailure(call: Call<ChaptersResponse>?, t: Throwable?) {
                mView.dismissLoading()
            }
        })


        var fetchConfiguration = FetchConfiguration.Builder(mView.getAppContext()!!)
            .setDownloadConcurrentLimit(1)
            .build()

        fetch = Fetch.Impl.getInstance(fetchConfiguration);
    }

    private lateinit var currentPath: String
    lateinit var mRepoSource: RepositorySource
    lateinit var fetch: Fetch
}