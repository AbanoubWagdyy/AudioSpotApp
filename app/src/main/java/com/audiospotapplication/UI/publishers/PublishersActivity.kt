package com.audiospotapplication.UI.publishers

import android.view.View
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.audiospotapplication.UI.BaseActivity

class PublishersActivity : BaseActivity() {

    override fun getHeaderTitle(): String {
        return "Publishers"
    }

    override fun getArrowHeaderVisibility(): Int {
        return View.GONE
    }

    override fun getFragment(ivArrow: ImageView): Fragment {
        return PublishersFragment.newInstance(ivArrow)
    }
}