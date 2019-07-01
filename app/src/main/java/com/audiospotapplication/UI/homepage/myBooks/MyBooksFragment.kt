package com.audiospotapplication.UI.homepage.myBooks

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.audiospot.DataLayer.Model.Book

import com.audiospotapplication.R
import com.audiospotapplication.UI.bookDetails.BookDetailsActivity
import com.audiospotapplication.UI.books.Interface.onBookItemClickListener
import com.audiospotapplication.UI.books.adapter.BooksAdapter
import com.audiospotapplication.utils.BookDataConversion
import com.audiospotapplication.utils.DialogUtils
import com.example.jean.jcplayer.JcPlayerManager
import com.example.jean.jcplayer.JcPlayerManagerListener
import com.example.jean.jcplayer.general.JcStatus
import com.example.jean.jcplayer.model.JcAudio
import com.google.android.material.snackbar.Snackbar
import dm.audiostreamer.MediaMetaData
import kotlinx.android.synthetic.main.fragment_my_books.*

class MyBooksFragment : Fragment(), MyBooksContract.View, onBookItemClickListener, JcPlayerManagerListener {

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

    override fun showEmptyBooksScreen(strEmptyListBooks: String) {
        relativeEmptyBooks.visibility = View.VISIBLE
        emptyBooks.text = strEmptyListBooks
    }

    override fun showBookDetailsScreen() {
        val intent = Intent(activity!!, BookDetailsActivity::class.java)
        startActivity(intent)
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
                recyclerMyBooks.adapter = adapter
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
                recyclerMyBooks.adapter = adapter
            } else {
                playAudio(book)
            }
        }
    }

    private fun playAudio(book: Book) {

        if (book == null || book.sample == null || book.sample.equals("")) {
            if (activity != null)
                Snackbar.make(
                    activity!!.findViewById(android.R.id.content), "Audio is not available right now ," +
                            "please check again later", Snackbar.LENGTH_LONG
                ).show()

            adapter = BooksAdapter(listMyBooks, this)
            recyclerMyBooks.adapter = adapter
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
        recyclerMyBooks.adapter = adapter
    }

    override fun getAppContext(): Context? {
        return activity!!.applicationContext
    }

    override fun showErrorMessage() {
        if (activity != null)
            Snackbar.make(
                activity!!.findViewById(android.R.id.content),
                activity!!.applicationContext.getString(R.string.try_again),
                Snackbar.LENGTH_SHORT
            ).show()
    }

    override fun showLoading() {
        DialogUtils.showProgressDialog(activity!!, "Loading ...")
    }

    override fun dismissLoading() {
        DialogUtils.dismissProgressDialog()
    }

    override fun setBookList(listMyBooks: List<Book>) {
        this.listMyBooks = listMyBooks
        recyclerMyBooks.layoutManager = LinearLayoutManager(context)
        recyclerMyBooks.setHasFixedSize(true)
        recyclerMyBooks.isNestedScrollingEnabled = false
        adapter = if (jcPlayerManager.isPlaying()) {
            BooksAdapter(
                listMyBooks, this,
                jcPlayerManager.currentAudio
            )
        } else {
            BooksAdapter(listMyBooks, this)
        }
        recyclerMyBooks.adapter = adapter
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_my_books, container, false)
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            MyBooksFragment()
    }

    override fun onAttach(activity: Activity) {
        super.onAttach(activity)
        try {
            mPlayCallback = activity as onItemPlayClickListener
            mPresenter = MyBooksPresenter(this)
            mPresenter.start()
        } catch (e: ClassCastException) {
            throw ClassCastException(activity.toString() + " must implement MyInterface ")
        }
    }

    interface onItemPlayClickListener {
        fun OnItemPlayed(mediaData: MediaMetaData)
    }

    private lateinit var adapter: BooksAdapter
    lateinit var mPresenter: MyBooksContract.Presenter
    private lateinit var mPlayCallback: onItemPlayClickListener
    private val jcPlayerManager: JcPlayerManager by lazy {
        JcPlayerManager.getInstance(activity!!.applicationContext).get()!!
    }

    private lateinit var listMyBooks: List<Book>
}