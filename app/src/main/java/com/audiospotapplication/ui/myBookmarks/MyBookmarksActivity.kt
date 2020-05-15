package com.audiospotapplication.ui.myBookmarks

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import com.audiospotapplication.ui.BaseActivity

class MyBookmarksActivity : BaseActivity() {

    override fun getHeaderTitle(): String {
        return "My Bookmarks"
    }

    override fun getArrowHeaderVisibility(): Int {
        return View.GONE
    }

    override fun getFragment(ivArrow: ImageView) = MyBookmarksFragment.newInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
}
