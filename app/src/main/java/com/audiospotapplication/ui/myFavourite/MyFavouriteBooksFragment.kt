package com.audiospotapplication.ui.myFavourite

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.audiospot.DataLayer.Model.Book
import com.audiospotapplication.ui.BaseFragment

import com.audiospotapplication.R
import com.audiospotapplication.ui.bookDetails.BookDetailsActivity
import com.audiospotapplication.ui.books.Interface.onBookItemClickListener
import com.audiospotapplication.ui.books.adapter.BooksAdapter
import com.audiospotapplication.utils.DialogUtils
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_my_favourite_books.*

class MyFavouriteBooksFragment : BaseFragment(), myFavouriteBooksContract.View,
    onBookItemClickListener,
    onBookItemClickListener.onCartBookDeleteClickListener {

    override fun showMessage(message: String) {
        if (activity != null)
            Snackbar.make(
                requireActivity().findViewById(android.R.id.content),
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
        if (playBookSample(book) == R.drawable.ic_pause) {
            handlePlayPause()
        }
    }

    override fun getAppContext(): Context? {
        return requireActivity().applicationContext
    }

    override fun showErrorMessage() {
        Snackbar.make(
            requireActivity().findViewById(android.R.id.content),
            requireActivity().applicationContext.getString(R.string.try_again),
            Snackbar.LENGTH_SHORT
        ).show()
    }

    override fun showLoading() {
        DialogUtils.showProgressDialog(requireActivity(), "Loading ...")
    }

    override fun dismissLoading() {
        DialogUtils.dismissProgressDialog()
    }

    override fun setBookList(listMyBooks: List<Book>) {
        this.listMyBooks = listMyBooks
        if (listMyBooks.isNotEmpty()) {
            this.listMyBooks = listMyBooks
            recyclerBooks.layoutManager = LinearLayoutManager(context)
            recyclerBooks.setHasFixedSize(true)
            recyclerBooks.isNestedScrollingEnabled = false

            adapter = BooksAdapter(
                listMyBooks, this,
                getPlaylistIdObserver().value!!, isPlaying
            )
            recyclerBooks.adapter = adapter

            getPlaylistIdObserver().observe(this, Observer {
                if (adapter != null && !it.equals(""))
                    adapter!!.updatePlaylistId(it, isPlaying)
            })

            getPlayingObserver().observe(this, Observer {
                if (adapter != null)
                    adapter!!.updatePlaylistId(getPlaylistIdObserver().value, it)
            })
        } else {
            relativeEmptyBooks.visibility = View.VISIBLE
            emptyBooks.text = "You have no favorite books yet"
        }
    }

    override fun showBookDetailsScreen() {
        val intent = Intent(requireActivity(), BookDetailsActivity::class.java)
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

    fun onEditCartClicked() {
        adapter?.showDelete()
    }

    private lateinit var listMyBooks: List<Book>

    private var adapter: BooksAdapter? = null

    lateinit var mPresenter: myFavouriteBooksContract.Presenter
}