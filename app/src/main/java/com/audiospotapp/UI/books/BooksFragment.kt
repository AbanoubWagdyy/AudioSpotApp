package com.audiospotapp.UI.books

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
import com.audiospotapp.DataLayer.Model.BookListResponse
import com.audiospotapp.DataLayer.Retrofit.GlobalKeys
import com.audiospotapp.R
import com.audiospotapp.UI.bookDetails.BookDetailsActivity
import com.audiospotapp.UI.books.adapter.BooksAdapter
import com.audiospotapp.UI.books.Interface.onBookItemClickListener
import com.audiospotapp.utils.DialogUtils
import com.github.zawadz88.materialpopupmenu.popupMenu
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_books.*
import kotlinx.android.synthetic.main.fragment_publishers.*

class BooksFragment(var ivArrow: ImageView) : Fragment(), BooksContract.View, onBookItemClickListener {

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

    }

    override fun setBooksList(result: BookListResponse?) {
        if (result != null) {
            recyclerBooks.layoutManager = LinearLayoutManager(context)
            recyclerBooks.setHasFixedSize(true)
            recyclerBooks.isNestedScrollingEnabled = false
            recyclerBooks.adapter = BooksAdapter(result!!.data, this)
        }
    }

    override fun showErrorMessage(message: String) {
        Snackbar.make(activity!!.findViewById(android.R.id.content), message, Snackbar.LENGTH_SHORT).show()
    }

    override fun getAppContext(): Context? {
        return activity!!.applicationContext
    }

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
}