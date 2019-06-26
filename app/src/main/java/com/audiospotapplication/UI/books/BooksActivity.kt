package com.audiospotapplication.UI.books

import android.view.View
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.audiospotapplication.UI.BaseActivity

class BooksActivity : BaseActivity() {

    override fun getHeaderTitle(): String {
        return "Books"
    }

    override fun getArrowHeaderVisibility(): Int {
        return View.VISIBLE
    }

    override fun getFragment(ivArrow: ImageView): Fragment {
       return BooksFragment.newInstance(ivArrow)
    }
}
