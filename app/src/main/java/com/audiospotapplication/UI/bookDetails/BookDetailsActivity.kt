package com.audiospotapplication.UI.bookDetails

import android.view.View
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.audiospotapplication.UI.BaseActivity

class BookDetailsActivity : BaseActivity() {

    override fun getHeaderTitle(): String =
        "Books"

    override fun getArrowHeaderVisibility(): Int =
        View.GONE

    override fun getFragment(ivArrow: ImageView): Fragment =
        BookDetailsFragment.newInstance()
}