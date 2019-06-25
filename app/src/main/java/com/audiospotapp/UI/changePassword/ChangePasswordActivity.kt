package com.audiospotapp.UI.changePassword

import android.view.View
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.audiospotapp.UI.BaseActivity

class ChangePasswordActivity : BaseActivity() {

    override fun getHeaderTitle(): String {
        return "Profile"
    }

    override fun getArrowHeaderVisibility(): Int {
        return View.GONE
    }

    override fun getFragment(ivArrow: ImageView): Fragment = ChangePasswordFragment.newInstance()
}
