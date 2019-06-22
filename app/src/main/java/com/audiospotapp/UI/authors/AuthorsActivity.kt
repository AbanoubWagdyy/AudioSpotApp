package com.audiospotapp.UI.authors

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.audiospotapp.UI.BaseActivity

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

    override fun getActiveTab(): ActiveTab {
        return ActiveTab.LIBRARY
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
}
