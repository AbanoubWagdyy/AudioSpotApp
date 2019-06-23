package com.audiospotapp.UI.bookChapters

import com.audiospotapp.DataLayer.DataRepository
import com.audiospotapp.DataLayer.Model.BookmarkBody
import com.audiospotapp.DataLayer.Model.ChaptersResponse
import com.audiospotapp.DataLayer.Retrofit.RetrofitCallbacks
import com.audiospotapp.DataLayer.Retrofit.RetrofitResponseHandler
import com.visionvalley.letuno.DataLayer.RepositorySource
import retrofit2.Call

class BookChaptersPresenter(val mView: BookChaptersContract.View) : BookChaptersContract.Presenter {

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
    }

    lateinit var mRepoSource: RepositorySource
}