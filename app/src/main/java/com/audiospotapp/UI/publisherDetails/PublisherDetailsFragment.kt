package com.audiospotapp.UI.publisherDetails

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.audiospot.DataLayer.Model.Book
import com.audiospotapp.DataLayer.Model.BookListResponse

import com.audiospotapp.R
import com.audiospotapp.UI.bookDetails.BookDetailsActivity
import com.audiospotapp.UI.books.Interface.onBookItemClickListener
import com.audiospotapp.UI.books.adapter.BooksAdapter
import com.audiospotapp.utils.DialogUtils
import com.audiospotapp.utils.ImageUtils
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_publisher_details.*
import kotlinx.android.synthetic.main.fragment_publisher_details.recyclerBooks
import kotlinx.android.synthetic.main.fragment_publisher_details.tvAbout

class PublisherDetailsFragment : Fragment(), PublisherDetailsContract.View, onBookItemClickListener {

    override fun showBookDetailsScreen() {
        val intent = Intent(activity!!, BookDetailsActivity::class.java)
        startActivity(intent)
    }

    override fun onItemClicked(book: Book) {
        mPresenter.saveBook(book)
    }

    override fun onPlayClicked(book: Book) {
    }

    override fun getAppContext(): Context? {
        return activity!!.applicationContext
    }

    override fun showLoading() {
        DialogUtils.showProgressDialog(activity!!, "Loading ...")
    }

    override fun dismissLoading() {
        DialogUtils.dismissProgressDialog()
    }

    override fun setPublisherBio(bio: String) {
        tvAbout.text = bio
    }

    override fun setPublisherName(name: String) {
        tvPublisher.text = name
    }

    override fun setPublisherImage(photo: String) {
        ImageUtils.setImageFromUrlIntoImageViewUsingPicasso(photo, activity!!.applicationContext, ivPublisher, false)
    }

    override fun setBookList(result: BookListResponse?) {
        if (result != null) {
            recyclerBooks.layoutManager = LinearLayoutManager(context)
            recyclerBooks.setHasFixedSize(true)
            recyclerBooks.isNestedScrollingEnabled = false
            recyclerBooks.adapter = BooksAdapter(result!!.data, this)
        }
    }

    override fun showErrorMessage(message: String) {
        Snackbar.make(activity!!.findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG).show()
    }

    lateinit var mPresenter: PublisherDetailsContract.Presenter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_publisher_details, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mPresenter = PublisherDetailsPresenter(this)
        mPresenter.start()
    }

    companion object {

        @JvmStatic
        fun newInstance() =
            PublisherDetailsFragment()
    }
}
