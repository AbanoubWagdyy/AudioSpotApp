package com.audiospotapplication.ui.giftSelection

import android.view.View
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.audiospotapplication.ui.BaseActivity

class GiftSelectionActivity : BaseActivity() {

    override fun getHeaderTitle(): String {
        return "Receive a book"
    }

    override fun getArrowHeaderVisibility(): Int {
        return View.GONE
    }

    override fun getFragment(ivArrow: ImageView): Fragment = GiftSelectionFragment.newInstance()
}
