package com.audiospotapp.UI.publisherDetails

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.audiospotapp.UI.BaseActivity

class PublisherDetailsActivity : BaseActivity() {
    override fun getHeaderTitle(): String {
        return "Publishers";
    }

    override fun getArrowHeaderVisibility(): Int {
        return View.GONE
    }

    override fun getFragment(ivArrow: ImageView): Fragment {
        return PublisherDetailsFragment.newInstance()
    }

    override fun getActiveTab(): ActiveTab {
        return ActiveTab.LIBRARY
    }
}
