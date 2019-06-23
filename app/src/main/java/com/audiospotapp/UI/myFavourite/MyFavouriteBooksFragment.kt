package com.audiospotapp.UI.myFavourite

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

import com.audiospotapp.R
import com.audiospotapp.UI.bookDetails.BookDetailsActivity
import com.audiospotapp.UI.books.Interface.onBookItemClickListener
import com.audiospotapp.UI.books.adapter.BooksAdapter
import com.audiospotapp.UI.onItemPlayClickListener
import com.audiospotapp.utils.BookMediaDataConversion
import com.audiospotapp.utils.DialogUtils
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_my_favourite_books.*

class MyFavouriteBooksFragment : Fragment(), myFavouriteBooksContract.View, onBookItemClickListener,
    onBookItemClickListener.onCartBookDeleteClickListener {

    override fun showMessage(message: String) {
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
        recyclerBooks.layoutManager = LinearLayoutManager(context)
        recyclerBooks.setHasFixedSize(true)
        recyclerBooks.isNestedScrollingEnabled = false
        adapter = BooksAdapter(listMyBooks, this,this)
        recyclerBooks.adapter = adapter
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
            throw ClassCastException(activity.toString() + " must implement MyInterface ")
        }
    }

    fun onEditCartClicked() {
        adapter.showDeleteIcon()
    }

    private lateinit var adapter: BooksAdapter
    private lateinit var mPlayCallback: onItemPlayClickListener
    lateinit var mPresenter: myFavouriteBooksContract.Presenter
}