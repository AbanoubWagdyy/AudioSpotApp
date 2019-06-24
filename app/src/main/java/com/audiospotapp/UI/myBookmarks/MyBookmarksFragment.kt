package com.audiospotapp.UI.myBookmarks


import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.audiospotapp.DataLayer.Model.Bookmark

import com.audiospotapp.R
import com.audiospotapp.UI.bookChapters.BookChaptersActivity
import com.audiospotapp.UI.myBookmarks.Interface.OnBookmarkClickListener
import com.audiospotapp.UI.myBookmarks.adapter.MyBookmarksAdapter
import com.audiospotapp.utils.DialogUtils
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_my_bookmarks.*

class MyBookmarksFragment : Fragment(), MyBookmarksContract.View, OnBookmarkClickListener {

    override fun showBookChaptersScreen() {
        val intent = Intent(activity!!, BookChaptersActivity::class.java)
        startActivity(intent)
    }

    override fun onBookmarkClicked(bookmark: Bookmark) {
        mPresenter.saveBookmark(bookmark)
    }

    override fun setBookmarks(data: List<Bookmark>) {
        recyclerBookmarks.layoutManager = LinearLayoutManager(context)
        recyclerBookmarks.setHasFixedSize(true)
        recyclerBookmarks.isNestedScrollingEnabled = false
        recyclerBookmarks.adapter = MyBookmarksAdapter(data, this)
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

    override fun showMessage(message: String) {
        Snackbar.make(activity!!.findViewById(android.R.id.content), message, Snackbar.LENGTH_SHORT).show()
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