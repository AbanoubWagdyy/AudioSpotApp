package com.audiospotapp.UI.categories

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.audiospotapp.UI.BaseActivity

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

    override fun getActiveTab(): ActiveTab {
        return ActiveTab.LIBRARY
    }
}