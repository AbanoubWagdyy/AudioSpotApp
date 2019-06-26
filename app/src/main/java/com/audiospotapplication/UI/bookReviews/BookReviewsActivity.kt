package com.audiospotapplication.UI.bookReviews

import android.view.View
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.audiospotapplication.UI.BaseActivity

class BookReviewsActivity : BaseActivity() {
    override fun getHeaderTitle(): String {
        return "Books"
    }

    override fun getArrowHeaderVisibility(): Int {
        return View.GONE
    }

    override fun getFragment(ivArrow: ImageView): Fragment {
        return BookReviewsFragment.newInstance()
    }
}