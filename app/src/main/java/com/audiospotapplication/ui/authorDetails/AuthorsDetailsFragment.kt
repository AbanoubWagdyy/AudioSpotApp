package com.audiospotapplication.ui.authorDetails

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
import com.audiospotapplication.data.model.BookListResponse

import com.audiospotapplication.R
import com.audiospotapplication.ui.bookDetails.BookDetailsActivity
import com.audiospotapplication.ui.books.Interface.onBookItemClickListener
import com.audiospotapplication.ui.books.adapter.BooksAdapter
import com.audiospotapplication.utils.DialogUtils
import com.audiospotapplication.utils.ImageUtils
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_authors_details.*

class AuthorsDetailsFragment : BaseFragment(), AuthorDetailsContract.View, onBookItemClickListener {

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

    override fun setAuthorBio(bio: String) {
        tvAbout.text = bio
    }

    override fun setAuthorName(name: String) {
        tvAuthor.text = "$name #"
    }

    override fun setAuthorImage(photo: String) {
        ImageUtils.setImageFromUrlIntoImageViewUsingGlide(
            photo, requireActivity().applicationContext,
            ivAuthor, false
        )
    }

    override fun setBookList(result: BookListResponse?) {
        result?.data?.let {
            this.listMyBooks = it
            recyclerBooks.layoutManager = LinearLayoutManager(context)
            recyclerBooks.setHasFixedSize(true)
            recyclerBooks.isNestedScrollingEnabled = false
            adapter = BooksAdapter(
                listMyBooks!!, this,
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
        Snackbar.make(
            requireActivity().findViewById(android.R.id.content),
            message,
            Snackbar.LENGTH_SHORT
        )
            .show()
    }


    override fun getAppContext(): Context? {
        return requireActivity().applicationContext
    }

    override fun showLoading() {
        DialogUtils.showProgressDialog(requireActivity(), "Loading ....")
    }

    override fun dismissLoading() {
        DialogUtils.dismissProgressDialog()
    }


    lateinit var mPresenter: AuthorDetailsContract.Presenter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_authors_details, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mPresenter = AuthorDetailsPresenter(this)
        mPresenter.start()
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            AuthorsDetailsFragment()
    }

    private var listMyBooks: List<Book>? = null
    private var adapter: BooksAdapter? = null
}
