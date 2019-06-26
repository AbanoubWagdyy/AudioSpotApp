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
import com.audiospot.DataLayer.Model.Book

import com.audiospotapplication.R
import com.audiospotapplication.UI.bookDetails.BookDetailsActivity
import com.audiospotapplication.UI.books.Interface.onBookItemClickListener
import com.audiospotapplication.UI.books.adapter.BooksAdapter
import com.audiospotapplication.utils.BookMediaDataConversion
import com.audiospotapplication.utils.DialogUtils
import com.google.android.material.snackbar.Snackbar
import dm.audiostreamer.MediaMetaData
import kotlinx.android.synthetic.main.fragment_my_books.*

class MyBooksFragment : Fragment(), MyBooksContract.View, onBookItemClickListener {

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
        var mediaData = BookMediaDataConversion.convertBookToMediaMetaData(book)
        mPlayCallback.OnItemPlayed(mediaData)
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
        recyclerMyBooks.layoutManager = LinearLayoutManager(context)
        recyclerMyBooks.setHasFixedSize(true)
        recyclerMyBooks.isNestedScrollingEnabled = false
        recyclerMyBooks.adapter = BooksAdapter(listMyBooks, this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_my_books, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mPresenter = MyBooksPresenter(this)
        mPresenter.start()
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
        } catch (e: ClassCastException) {
            throw ClassCastException(activity.toString() + " must implement MyInterface ")
        }
    }

    interface onItemPlayClickListener {
        fun OnItemPlayed(mediaData: MediaMetaData)
    }

    lateinit var mPresenter: MyBooksContract.Presenter
    private lateinit var mPlayCallback: onItemPlayClickListener
}
