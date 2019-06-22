package com.audiospotapp.UI.bookDetails

import android.view.View
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.audiospotapp.UI.BaseActivity

class BookDetailsActivity : BaseActivity() {

    override fun getHeaderTitle(): String =
        "Books"

    override fun getArrowHeaderVisibility(): Int =
        View.GONE

    override fun getFragment(ivArrow: ImageView): Fragment =
        BookDetailsFragment.newInstance()

    override fun getActiveTab(): ActiveTab =
        ActiveTab.LIBRARY
}