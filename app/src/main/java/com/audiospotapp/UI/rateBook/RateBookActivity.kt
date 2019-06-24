package com.audiospotapp.UI.rateBook

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.audiospotapp.R
import com.audiospotapp.UI.BaseActivity

class RateBookActivity : BaseActivity() {

    override fun getHeaderTitle(): String {
        return "Books"
    }

    override fun getArrowHeaderVisibility(): Int {
        return View.GONE
    }

    override fun getFragment(ivArrow: ImageView): Fragment = RateBookFragment.newInstance()

    override fun getActiveTab(): ActiveTab {
        return ActiveTab.LIBRARY
    }
}