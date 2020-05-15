package com.audiospotapplication.ui.updateProfile

import android.view.View
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.audiospotapplication.ui.BaseActivity

class UpdateProfileActivity : BaseActivity() {

    override fun getHeaderTitle(): String {
        return "Update a profile"
    }

    override fun getArrowHeaderVisibility(): Int {
        return View.GONE
    }

    override fun getFragment(ivArrow: ImageView): Fragment {
        return UpdateProfileFragment.newInstance()
    }
}