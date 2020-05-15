package com.audiospotapplication.ui.myBookmarks


import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.audiospotapplication.ui.BaseFragment
import com.audiospotapplication.data.model.Bookmark

import com.audiospotapplication.R
import com.audiospotapplication.ui.bookChapters.BookChaptersActivity
import com.audiospotapplication.ui.myBookmarks.Interface.OnBookmarkClickListener
import com.audiospotapplication.ui.myBookmarks.adapter.MyBookmarksAdapter
import com.audiospotapplication.utils.DialogUtils
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_my_bookmarks.*

class MyBookmarksFragment : BaseFragment(), MyBookmarksContract.View, OnBookmarkClickListener {

    override fun showBookChaptersScreen() {
        val intent = Intent(requireActivity(), BookChaptersActivity::class.java)
        startActivity(intent)
    }

    override fun onBookmarkClicked(bookmark: Bookmark) {
        mPresenter.saveBookmark(bookmark)
    }

    override fun setBookmarks(data: List<Bookmark>) {
        if (data.isNotEmpty()) {
            recyclerBookmarks.layoutManager = LinearLayoutManager(context)
            recyclerBookmarks.setHasFixedSize(true)
            recyclerBookmarks.isNestedScrollingEnabled = false
            recyclerBookmarks.adapter = MyBookmarksAdapter(data, this)
        } else {
            relativeEmptyBookmarks.visibility = View.VISIBLE
            emptyBookmarks.text = "You have no bookmarks yet !."
        }
    }

    override fun getAppContext(): Context? {
        return requireActivity().applicationContext
    }

    override fun showLoading() {
        DialogUtils.showProgressDialog(requireActivity(), "Loading ...")
    }

    override fun dismissLoading() {
        DialogUtils.dismissProgressDialog()
    }

    override fun showMessage(message: String) {
        Snackbar.make(requireActivity().findViewById(android.R.id.content), message, Snackbar.LENGTH_SHORT).show()
    }

    override fun finalizeView() {

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_my_bookmarks, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mPresenter = MyBookmarksPresenter(this)
        mPresenter.start()
    }

    lateinit var mPresenter: MyBookmarksContract.Presenter

    companion object {
        @JvmStatic
        fun newInstance() =
            MyBookmarksFragment()
    }
}