package com.audiospotapp.UI.bookReviews

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.audiospotapp.R
import com.audiospotapp.UI.BaseActivity

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

    override fun getActiveTab(): ActiveTab {
        return ActiveTab.LIBRARY
    }
}