package com.audiospotapp.UI.authorDetails

import android.view.View
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.audiospotapp.UI.ActiveTab
import com.audiospotapp.UI.BaseActivity

class AuthorDetailsActivity : BaseActivity() {
    override fun getHeaderTitle(): String {
        return "Authors"
    }

    override fun getArrowHeaderVisibility(): Int {
        return View.GONE
    }

    override fun getFragment(ivArrow: ImageView): Fragment =AuthorsDetailsFragment.newInstance()
}
