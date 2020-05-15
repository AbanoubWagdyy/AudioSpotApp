package com.audiospotapplication.ui.homepage.myBooks

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
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_my_books.*

class MyBooksFragment : BaseFragment(), MyBooksContract.View, onBookItemClickListener {

    override fun showEmptyBooksScreen(strEmptyListBooks: String) {
        emptyBooks.visibility = View.VISIBLE
        emptyBooks.text = strEmptyListBooks
    }

    override fun showBookDetailsScreen() {
        val intent = Intent(requireActivity(), BookDetailsActivity::class.java)
        startActivity(intent)
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
        if (activity != null)
            Snackbar.make(
                requireActivity().findViewById(android.R.id.content),
                requireActivity().applicationContext.getString(R.string.try_again),
                Snackbar.LENGTH_SHORT
            ).show()
    }

    override fun showLoading() {
        if (swipeToRefresh != null)
            swipeToRefresh.isRefreshing = true
    }

    override fun dismissLoading() {
        if (swipeToRefresh != null)
            swipeToRefresh.isRefreshing = false
    }

    override fun setBookList(listMyBooks: List<Book>) {
        if (swipeToRefresh != null) {
            swipeToRefresh.isRefreshing = false
            this.listMyBooks = listMyBooks
            recyclerMyBooks.layoutManager = LinearLayoutManager(context)
            recyclerMyBooks.setHasFixedSize(true)
            recyclerMyBooks.isNestedScrollingEnabled = false

            adapter = BooksAdapter(
                listMyBooks, this,
                getPlaylistIdObserver().value!!, isPlaying
            )
            recyclerMyBooks.adapter = adapter

            getPlaylistIdObserver().observe(this, Observer {
                if (adapter != null && !it.equals(""))
                    adapter!!.updatePlaylistId(it, isPlaying)
            })

            getPlayingObserver().observe(this, Observer {
                if (adapter != null)
                    adapter!!.updatePlaylistId(getPlaylistIdObserver().value, it)
            })
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_my_books, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        swipeToRefresh.setOnRefreshListener {
            mPresenter.start()
        }

        mPresenter = MyBooksPresenter(this)
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            MyBooksFragment()
    }

    override fun onResume() {
        super.onResume()
        mPresenter.start()
    }

    private var adapter: BooksAdapter? = null
    lateinit var mPresenter: MyBooksContract.Presenter

    private lateinit var listMyBooks: List<Book>
}