package com.audiospotapplication.UI.books

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.audiospot.DataLayer.Model.Book
import com.audiospotapplication.UI.BaseFragment
import com.audiospotapplication.DataLayer.Model.BookListResponse
import com.audiospotapplication.DataLayer.Retrofit.GlobalKeys
import com.audiospotapplication.R
import com.audiospotapplication.UI.bookDetails.BookDetailsActivity
import com.audiospotapplication.UI.books.adapter.BooksAdapter
import com.audiospotapplication.UI.books.Interface.onBookItemClickListener
import com.audiospotapplication.utils.DialogUtils
import com.github.zawadz88.materialpopupmenu.popupMenu
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_books.*

class BooksFragment(var ivArrow: ImageView) : BaseFragment(), BooksContract.View,
    onBookItemClickListener {

    override fun showBookDetailsScreen() {
        val intent = Intent(requireActivity(), BookDetailsActivity::class.java)
        startActivity(intent)
    }

    override fun dismissLoadingDialog() {
        DialogUtils.dismissProgressDialog()
    }

    override fun showLoadingDialog() {
        DialogUtils.showProgressDialog(requireActivity(), "Loading ...")
    }

    override fun onItemClicked(book: Book) {
        mPresenter.saveBook(book)
    }

    override fun onPlayClicked(book: Book) {
        if (playBookSample(book) == R.drawable.ic_pause) {
            handlePlayPause()
        }
    }

    override fun setBooksList(result: BookListResponse?) {
        result?.let {
            this.listMyBooks = it.data
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
        }
    }

    override fun showErrorMessage(message: String) {
        Snackbar.make(requireActivity().findViewById(android.R.id.content), message, Snackbar.LENGTH_SHORT)
            .show()
    }

    override fun getAppContext(): Context? {
        return requireActivity().applicationContext
    }

    private var adapter: BooksAdapter? = null

    lateinit var mPresenter: BooksContract.Presenter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
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

        popupMenu.show(requireActivity(), view)
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

    private lateinit var listMyBooks: List<Book>
}