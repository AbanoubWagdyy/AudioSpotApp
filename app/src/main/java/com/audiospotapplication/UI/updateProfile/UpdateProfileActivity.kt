package com.audiospotapplication.UI.updateProfile

import android.view.View
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.audiospotapplication.UI.BaseActivity

class UpdateProfileActivity : BaseActivity() {

    override fun getHeaderTitle(): String {
        return "Profile"
    }

    override fun getArrowHeaderVisibility(): Int {
        return View.GONE
    }

    override fun getFragment(ivArrow: ImageView): Fragment {
        return UpdateProfileFragment.newInstance()
    }
}