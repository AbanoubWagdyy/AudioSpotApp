package com.audiospotapplication.ui.categories

import android.view.View
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.audiospotapplication.ui.BaseActivity

class CategoriesActivity : BaseActivity() {

    override fun getHeaderTitle(): String {
        return "Categories";
    }

    override fun getArrowHeaderVisibility(): Int {
        return View.GONE
    }

    override fun getFragment(ivArrow: ImageView): Fragment {
        return CategoriesFragment.newInstance(ivArrow)
    }
}