package com.audiospotapplication.UI.myFavourite

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.audiospot.DataLayer.Model.Book

import com.audiospotapplication.R
import com.audiospotapplication.UI.bookDetails.BookDetailsActivity
import com.audiospotapplication.UI.books.Interface.onBookItemClickListener
import com.audiospotapplication.UI.books.adapter.BooksAdapter
import com.audiospotapplication.UI.onItemPlayClickListener
import com.audiospotapplication.utils.BookDataConversion
import com.audiospotapplication.utils.DialogUtils
import com.example.jean.jcplayer.JcPlayerManager
import com.example.jean.jcplayer.JcPlayerManagerListener
import com.example.jean.jcplayer.general.JcStatus
import com.example.jean.jcplayer.model.JcAudio
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_my_favourite_books.*

class MyFavouriteBooksFragment : Fragment(), myFavouriteBooksContract.View, onBookItemClickListener,
    onBookItemClickListener.onCartBookDeleteClickListener, JcPlayerManagerListener {

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

    override fun showMessage(message: String) {
        if (activity != null)
            Snackbar.make(
                activity!!.findViewById(android.R.id.content),
                message,
                Snackbar.LENGTH_SHORT
            ).show()
    }

    override fun onItemDeleted(book: Book) {
        mPresenter.removeFromFavorites(book)
    }

    override fun onItemClicked(book: Book) {
        mPresenter.saveBook(book)
    }

    override fun onPlayClicked(book: Book) {
        var currentAudio = jcPlayerManager.currentAudio
        if (jcPlayerManager.isPlaying()) {
            if (currentAudio?.path.equals(book.sample)) {
                jcPlayerManager.pauseAudio()
                adapter = BooksAdapter(listMyBooks, this, this)
                recyclerBooks.adapter = adapter
                return
            } else {
                playAudio(book)
            }
        } else {
            if (currentAudio != null && currentAudio?.path.equals(book.sample)) {
                jcPlayerManager.continueAudio()
                adapter = BooksAdapter(
                    listMyBooks, this, this,
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

            adapter = BooksAdapter(listMyBooks, this, this)
            recyclerBooks.adapter = adapter
            return
        }

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
        adapter = BooksAdapter(
            listMyBooks, this, this,
            jcPlayerManager.currentAudio
        )
        recyclerBooks.adapter = adapter
    }

    override fun getAppContext(): Context? {
        return activity!!.applicationContext
    }

    override fun showErrorMessage() {
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
        if (listMyBooks.isNotEmpty()) {
            recyclerBooks.layoutManager = LinearLayoutManager(context)
            recyclerBooks.setHasFixedSize(true)
            recyclerBooks.isNestedScrollingEnabled = false
            adapter = if (jcPlayerManager.isPlaying()) {
                BooksAdapter(
                    listMyBooks, this, this,
                    jcPlayerManager.currentAudio
                )
            } else {
                BooksAdapter(listMyBooks, this, this)
            }
        } else {
            relativeEmptyBooks.visibility = View.VISIBLE
            emptyBooks.text = "You have no favorite books yet"
        }
    }

    override fun showBookDetailsScreen() {
        val intent = Intent(activity!!, BookDetailsActivity::class.java)
        startActivity(intent)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_my_favourite_books, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mPresenter = myFavoriteBooksPresenter(this)
        mPresenter.start()
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            MyFavouriteBooksFragment()
    }

    override fun onAttach(activity: Activity) {
        super.onAttach(activity)
        try {
            mPlayCallback = activity as onItemPlayClickListener
        } catch (e: ClassCastException) {
            throw ClassCastException("$activity must implement MyInterface ")
        }
    }

    fun onEditCartClicked() {
        adapter.showDeleteIcon()
    }

    private lateinit var listMyBooks: List<Book>
    private lateinit var adapter: BooksAdapter
    private lateinit var mPlayCallback: onItemPlayClickListener
    lateinit var mPresenter: myFavouriteBooksContract.Presenter
    private val jcPlayerManager: JcPlayerManager by lazy {
        JcPlayerManager.getInstance(activity!!.applicationContext).get()!!
    }
}