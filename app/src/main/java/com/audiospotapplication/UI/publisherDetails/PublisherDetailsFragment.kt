package com.audiospotapplication.UI.publisherDetails

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import com.example.jean.jcplayer.JcPlayerManager
import com.example.jean.jcplayer.JcPlayerManagerListener
import com.example.jean.jcplayer.general.JcStatus
import com.example.jean.jcplayer.model.JcAudio
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_publisher_details.*
import kotlinx.android.synthetic.main.fragment_publisher_details.recyclerBooks
import kotlinx.android.synthetic.main.fragment_publisher_details.tvAbout

class PublisherDetailsFragment : BaseFragment(), PublisherDetailsContract.View, onBookItemClickListener,
    JcPlayerManagerListener {
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
        ImageUtils.setImageFromUrlIntoImageViewUsingGlide(photo, activity!!.applicationContext, ivPublisher, false)
    }

    override fun setBookList(result: BookListResponse?) {
        result?.let {
            this.listMyBooks = it.data
            recyclerBooks.layoutManager = LinearLayoutManager(context)
            recyclerBooks.setHasFixedSize(true)
            recyclerBooks.isNestedScrollingEnabled = false
            recyclerBooks.adapter = BooksAdapter(it.data, this,
                getPlaylistIdObserver().value!!, isPlaying)
        }

    }

    override fun showErrorMessage(message: String) {
        if (activity != null)
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

    private val jcPlayerManager: JcPlayerManager by lazy {
        JcPlayerManager.getInstance(activity!!.applicationContext).get()!!
    }

    private lateinit var listMyBooks: List<Book>
    private lateinit var adapter: BooksAdapter
}
