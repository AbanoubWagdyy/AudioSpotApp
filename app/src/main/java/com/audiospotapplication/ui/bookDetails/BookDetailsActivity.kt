package com.audiospotapplication.ui.bookDetails

import android.view.View
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.audiospotapplication.R
import com.audiospotapplication.ui.BaseActivity

class BookDetailsActivity : BaseActivity() {

    override fun getHeaderTitle(): String =
        getString(R.string.books)

    override fun getArrowHeaderVisibility(): Int =
        View.GONE

    override fun getFragment(ivArrow: ImageView): Fragment =
        BookDetailsFragment.newInstance()
}