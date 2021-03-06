package com.audiospotapplication.ui.rateBook

import android.view.View
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.audiospotapplication.ui.BaseActivity

class RateBookActivity : BaseActivity() {

    override fun getHeaderTitle(): String {
        return "Rate a book"
    }

    override fun getArrowHeaderVisibility(): Int {
        return View.GONE
    }

    override fun getFragment(ivArrow: ImageView): Fragment = RateBookFragment.newInstance()
}