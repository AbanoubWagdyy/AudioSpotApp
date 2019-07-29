package com.audiospotapplication.UI.books

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import com.audiospot.DataLayer.Model.Book
import com.audiospotapplication.BaseFragment
import com.audiospotapplication.DataLayer.Model.BookListResponse
import com.audiospotapplication.DataLayer.Retrofit.GlobalKeys
import com.audiospotapplication.R
import com.audiospotapplication.UI.bookDetails.BookDetailsActivity
import com.audiospotapplication.UI.books.adapter.BooksAdapter
import com.audiospotapplication.UI.books.Interface.onBookItemClickListener
import com.audiospotapplication.utils.DialogUtils
import com.example.jean.jcplayer.JcPlayerManager
import com.example.jean.jcplayer.JcPlayerManagerListener
import com.example.jean.jcplayer.general.JcStatus
import com.example.jean.jcplayer.model.JcAudio
import com.github.zawadz88.materialpopupmenu.popupMenu
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_books.*

class BooksFragment(var ivArrow: ImageView) : BaseFragment(), BooksContract.View, onBookItemClickListener,
    JcPlayerManagerListener {
    override fun onPreparedAudio(status: JcStatus) {

    }

    override fun onCompletedAudio() {

    }

    override fun onPaused(status: JcStatus) {

    }

    override fun onContinueAudio(status: JcStatus) {

    }

    override fun onPlaying(status: JcStatus) {

    }

    override fun onTimeChanged(status: JcStatus) {

    }

    override fun onStopped(status: JcStatus) {

    }

    override fun onJcpError(throwable: Throwable) {

    }

    override fun showBookDetailsScreen() {
        val intent = Intent(activity!!, BookDetailsActivity::class.java)
        startActivity(intent)
    }

    override fun dismissLoadingDialog() {
        DialogUtils.dismissProgressDialog()
    }

    override fun showLoadingDialog() {
        DialogUtils.showProgressDialog(activity!!, "Loading ...")
    }

    override fun onItemClicked(book: Book) {
        mPresenter.saveBook(book)
    }

    override fun onPlayClicked(book: Book) {
        var currentAudio = jcPlayerManager.currentAudio
        if (jcPlayerManager.isPlaying()) {
            if (currentAudio?.path.equals(book.sample)) {
                jcPlayerManager.pauseAudio()
                adapter = BooksAdapter(listMyBooks, this)
                recyclerBooks.adapter = adapter
                return
            } else {
                playAudio(book)
            }
        } else {
            if (currentAudio != null && currentAudio?.path.equals(book.sample)) {
                jcPlayerManager.continueAudio()
                adapter = BooksAdapter(
                    listMyBooks, this,
                    jcPlayerManager.currentAudio
                )
                recyclerBooks.adapter = adapter
            } else {
                playAudio(book)
            }
        }
    }

    private fun playAudio(book: Book) {

        if (book == null || book.sample == null || book.sample.equals("")) {
            Snackbar.make(
                activity!!.findViewById(android.R.id.content), "Audio is not available right now ," +
                        "please check again later", Snackbar.LENGTH_LONG
            ).show()

            adapter = BooksAdapter(listMyBooks, this)
            recyclerBooks.adapter = adapter
            return
        }

        jcPlayerManager.kill()

        var audio = JcAudio.createFromURL(
            book.id, book.title,
            book.sample, null
        )
        var playlist = ArrayList<JcAudio>()
        playlist.add(audio)

        jcPlayerManager.playlist = playlist
        jcPlayerManager.jcPlayerManagerListener = this

        jcPlayerManager.playAudio(audio)
        jcPlayerManager.createNewNotification(R.mipmap.ic_launcher)

        adapter = BooksAdapter(listMyBooks, this, jcPlayerManager.currentAudio)
        recyclerBooks.adapter = adapter
    }

    override fun setBooksList(result: BookListResponse?) {
        this.listMyBooks = result!!.data
        if (result != null) {
            recyclerBooks.layoutManager = LinearLayoutManager(context)
            recyclerBooks.setHasFixedSize(true)
            recyclerBooks.isNestedScrollingEnabled = false
            adapter = if (jcPlayerManager.isPlaying()) {
                BooksAdapter(
                    listMyBooks, this,
                    jcPlayerManager.currentAudio
                )
            } else {
                BooksAdapter(listMyBooks, this)
            }
            recyclerBooks.adapter = adapter
        }
    }

    override fun showErrorMessage(message: String) {
        Snackbar.make(activity!!.findViewById(android.R.id.content), message, Snackbar.LENGTH_SHORT).show()
    }

    override fun getAppContext(): Context? {
        return activity!!.applicationContext
    }

    private lateinit var adapter: BooksAdapter

    lateinit var mPresenter: BooksContract.Presenter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_books, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ivArrow.setOnClickListener {
            onSingleSectionClicked(ivArrow)
        }
        mPresenter = BooksPresenter(this)
        mPresenter.start()
    }

    fun onSingleSectionClicked(view: View) {

        val popupMenu = popupMenu {
            section {
                item {
                    labelRes = R.string.a_z
                    callback = {
                        //optional
                        mPresenter.handleSortBookItemClicked(GlobalKeys.SortType.A_Z)
                    }
                }
                item {
                    labelRes = R.string.newest_arrival
                    callback = {
                        //optional
                        mPresenter.handleSortBookItemClicked(GlobalKeys.SortType.NEWEST_ARRIVAL)
                    }
                }
                item {
                    labelRes = R.string.best_seller
                    callback = {
                        //optional
                        mPresenter.handleSortBookItemClicked(GlobalKeys.SortType.BEST_SELLER)
                    }
                }
                item {
                    labelRes = R.string.length
                    callback = {
                        //optional
                        mPresenter.handleSortBookItemClicked(GlobalKeys.SortType.LENGTH)
                    }
                }
                item {
                    labelRes = R.string.average_review
                    callback = {
                        //optional
                        mPresenter.handleSortBookItemClicked(GlobalKeys.SortType.AVERAGE_REVIEW)
                    }
                }
            }
        }

        popupMenu.show(activity!!, view)
    }

    override fun onStop() {
        mPresenter.handleOnActivityStopped()
        super.onStop()
    }

    companion object {
        @JvmStatic
        fun newInstance(ivArrow: ImageView) =
            BooksFragment(ivArrow)
    }

    private val jcPlayerManager: JcPlayerManager by lazy {
        JcPlayerManager.getInstance(activity!!.applicationContext).get()!!
    }

    private lateinit var listMyBooks: List<Book>
}