package com.audiospotapplication.ui.giveAgift

import android.view.View
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.audiospotapplication.ui.BaseActivity

class GiveGiftActivity : BaseActivity() {

    override fun getHeaderTitle(): String {
        return "Books"
    }

    override fun getArrowHeaderVisibility(): Int {
        return View.GONE
    }

    override fun getFragment(ivArrow: ImageView): Fragment {
        return GiveGiftFragment.newInstance()
    }
}
