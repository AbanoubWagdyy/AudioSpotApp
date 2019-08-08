package com.audiospotapplication.UI.bookChapters

import android.os.Bundle
import android.util.Log
import com.audiospotapplication.DataLayer.DataRepository
import com.audiospotapplication.DataLayer.Model.BookmarkBody
import com.audiospotapplication.DataLayer.Model.ChaptersData
import com.audiospotapplication.DataLayer.Model.ChaptersResponse
import com.audiospotapplication.DataLayer.Retrofit.RetrofitCallbacks
import com.audiospotapplication.DataLayer.Retrofit.RetrofitResponseHandler
import com.example.jean.jcplayer.JcPlayerManager
import com.example.jean.jcplayer.general.JcStatus
import com.example.jean.jcplayer.model.JcAudio
import com.snatik.storage.Storage
import com.tonyodev.fetch2.*
import com.tonyodev.fetch2core.DownloadBlock
import com.visionvalley.letuno.DataLayer.RepositorySource
import retrofit2.Call
import java.io.File


class BookChaptersPresenter(val mView: BookChaptersContract.View) : BookChaptersContract.Presenter, FetchListener {
    override fun resetRepo() {
        mRepoSource.clear()
    }

    override fun validateChapterDownloaded(data: ChaptersData): String {
        val storage = Storage(mView.getAppContext())
        val path = storage.internalCacheDirectory

        val newDir = path + File.separator + "AudioSpotDownloadsCache"

        var fileNameStr = data.sound_file.split("/")

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
        mView.dismissDownloadingDialog()
        mView.showDownloadComplete("Item Downloaded !.", currentPath)
    }

    override fun onDeleted(download: Download) {
        Log.d("onDeleted", "onDeleted")
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

    override fun onStarted(download: Download, downloadBlocks: List<DownloadBlock>, totalBlocks: Int) {
    }

    override fun onWaitingNetwork(download: Download) {

    }

    override fun handleDownloadPressed(currentSong: JcAudio?) {

        mView.showDownloadingDialog()
        val storage = Storage(mView.getAppContext())
        val path = storage.internalCacheDirectory

        val newDir = path + File.separator + "AudioSpotDownloadsCache"
        var fileNameStr = currentSong!!.path.split("/")
        currentPath = newDir + "/" + fileNameStr[fileNameStr.size - 1]

        if (storage.isFileExist(currentPath)) {
            mView.dismissLoading()
            mView.showMessage("Already Downloaded")
            return
        }
        val request = Request(currentSong!!.path, currentPath)
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
        if (savedbook != null) {
            bookmarkBody.bookId = savedbook.id
        }
        if (savedbook != null) {
            bookmarkBody.url = savedbook.cover
        }
        bookmarkBody.chapter_name = title
        if (savedbook != null) {
            bookmarkBody.bookName = savedbook.title
        }

        mRepoSource.setBookmarkData(bookmarkBody)
        mView.showAddBookmarkScreen()
    }

    override fun start(extras: Bundle?) {

        mRepoSource = DataRepository.getInstance(mView.getAppContext()!!)

//        if (extras != null) {
//            val audioId = extras.getInt("currentAudio")
//            if (!mRepoSource.isBookMine(audioId)) {
//                mView.showHomepageScreen()
//                return
//            }
//        }

        var currentAudioId = 0
        if (JcPlayerManager.getInstance(mView.getAppContext()!!).get()!!.currentAudio != null) {
            currentAudioId = JcPlayerManager.getInstance(mView.getAppContext()!!).get()!!.currentAudio!!.id
        }

        var currentAudioStatus: JcStatus? = null

        if (JcPlayerManager.getInstance(mView.getAppContext()!!).get()!!.currentStatus != null) {
            currentAudioStatus = JcPlayerManager.getInstance(mView.getAppContext()!!).get()!!.currentStatus!!
        }

        var book = mRepoSource.getSavedBook()
        var bookmark = mRepoSource.getBookmark()
        var isToPlayFirstChapter = mRepoSource.getIsPlayFirstChapter()

        if (book != null) {
            mView.setBookName(book.title)
            mView.setBookImage(book.cover)
        }

        mView.showLoadingDialog()
        mRepoSource.getBookChapters(object : RetrofitCallbacks.ChaptersResponseCallback {
            override fun onSuccess(result: ChaptersResponse?) {
                mView.dismissLoading()
                val status = RetrofitResponseHandler.validateResponseStatus(result)
                if (status == RetrofitResponseHandler.Companion.Status.VALID) {
                    mView.setChapters(result!!.data)
                    if (extras == null) {
                        if (isToPlayFirstChapter) {
                            mView.onChapterClicked(result.data[0])
                            mRepoSource.setIsPlayFirstChapter(false)
                        } else if (status == RetrofitResponseHandler.Companion.Status.UNAUTHORIZED) {
                            mView!!.showLoginPage()
                        }

                        if (bookmark != null) {
                            var chapterData = result!!.data.filter {
                                it.id == bookmark!!.chapter_id
                            }[0]
                            mView.setBookmark(bookmark)
                            mView.onChapterClicked(chapterData)
                        } else {
                            mView.playAllChapters(result)
                        }
                    } else {
                        var chapterData = result!!.data.filter {
                            it.id == currentAudioId
                        }[0]

                        mView.onChapterClicked(chapterData, currentAudioStatus)
                    }
                } else {
                    mView.showHomepageScreen()
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