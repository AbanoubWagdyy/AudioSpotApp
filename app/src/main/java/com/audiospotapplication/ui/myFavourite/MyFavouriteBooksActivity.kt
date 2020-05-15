package com.audiospotapplication.ui.myFavourite

import android.view.View
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.audiospotapplication.ui.BaseActivity
import kotlinx.android.synthetic.main.back_header.*

class MyFavouriteBooksActivity : BaseActivity() {

    override fun getHeaderTitle(): String {
        return "My Favorites"
    }

    override fun getArrowHeaderVisibility(): Int {
        return View.GONE
    }

    override fun getFragment(ivArrow: ImageView): Fragment {
        return fragment
    }

    override fun manageEditVisibility() {
        ivEdit.visibility = View.VISIBLE
        ivEdit.setOnClickListener {
            fragment.onEditCartClicked()
        }
    }

    var fragment = MyFavouriteBooksFragment.newInstance()
}
