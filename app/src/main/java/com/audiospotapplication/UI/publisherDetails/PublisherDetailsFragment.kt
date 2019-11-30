package com.audiospotapplication.UI.publisherDetails

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.audiospot.DataLayer.Model.Book
import com.audiospotapplication.BaseFragment
import com.audiospotapplication.DataLayer.Model.BookListResponse

import com.audiospotapplication.R
import com.audiospotapplication.UI.bookDetails.BookDetailsActivity
import com.audiospotapplication.UI.books.Interface.onBookItemClickListener
import com.audiospotapplication.UI.books.adapter.BooksAdapter
import com.audiospotapplication.utils.DialogUtils
import com.audiospotapplication.utils.ImageUtils
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_publisher_details.*
import kotlinx.android.synthetic.main.fragment_publisher_details.recyclerBooks
import kotlinx.android.synthetic.main.fragment_publisher_details.tvAbout

class PublisherDetailsFragment : BaseFragment(), PublisherDetailsContract.View,
    onBookItemClickListener {

    override fun showBookDetailsScreen() {
        val intent = Intent(activity!!, BookDetailsActivity::class.java)
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
        tvPublisher.text = "$name #"
    }

    override fun setPublisherImage(photo: String) {
        ImageUtils.setImageFromUrlIntoImageViewUsingGlide(
            photo,
            activity!!.applicationContext,
            ivPublisher,
            false
        )
    }

    override fun setBookList(result: BookListResponse?) {
        result?.data?.let {
            if (it.isEmpty()) {
                recyclerBooks.visibility = View.GONE
            } else {

                recyclerBooks.layoutManager = LinearLayoutManager(context)
                recyclerBooks.setHasFixedSize(true)
                recyclerBooks.isNestedScrollingEnabled = false

                adapter = BooksAdapter(
                    it, this,
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
    }

    override fun showErrorMessage(message: String) {
        if (activity != null)
            Snackbar.make(
                activity!!.findViewById(android.R.id.content),
                message,
                Snackbar.LENGTH_LONG
            ).show()
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

    private var adapter: BooksAdapter? = null
}
