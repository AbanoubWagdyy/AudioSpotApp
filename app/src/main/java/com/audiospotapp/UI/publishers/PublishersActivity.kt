package com.audiospotapp.UI.publishers

import android.view.View
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.audiospotapp.UI.BaseActivity
import com.audiospotapp.UI.categories.CategoriesFragment

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

    override fun getActiveTab(): ActiveTab {
        return ActiveTab.LIBRARY
    }
}