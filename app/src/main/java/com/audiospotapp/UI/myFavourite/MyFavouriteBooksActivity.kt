package com.audiospotapp.UI.myFavourite

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.audiospotapp.UI.BaseActivity

class MyFavouriteBooksActivity : BaseActivity() {

    override fun getHeaderTitle(): String {
        return "Books"
    }

    override fun getArrowHeaderVisibility(): Int {
       return View.GONE
    }

    override fun getFragment(ivArrow: ImageView): Fragment {
        return MyFavouriteBooksFragment.newInstance()
    }

    override fun getActiveTab(): ActiveTab {
        return ActiveTab.MENU
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
}
