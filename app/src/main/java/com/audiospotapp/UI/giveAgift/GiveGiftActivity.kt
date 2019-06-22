package com.audiospotapp.UI.giveAgift

import android.view.View
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.audiospotapp.UI.BaseActivity

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

    override fun getActiveTab(): ActiveTab {
        //useless :D
        return ActiveTab.MENU
    }
}
