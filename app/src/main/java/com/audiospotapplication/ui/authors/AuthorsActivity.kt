package com.audiospotapplication.ui.authors

import android.view.View
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.audiospotapplication.ui.BaseActivity

class AuthorsActivity : BaseActivity() {

    override fun getHeaderTitle(): String {
        return "Authors"
    }

    override fun getArrowHeaderVisibility(): Int {
        return View.GONE
    }

    override fun getFragment(ivArrow: ImageView): Fragment {
        return AuthorsFragment.newInstance(ivArrow)
    }
}
