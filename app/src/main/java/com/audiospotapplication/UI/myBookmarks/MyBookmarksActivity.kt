package com.audiospotapplication.UI.myBookmarks

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import com.audiospotapplication.UI.BaseActivity

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
