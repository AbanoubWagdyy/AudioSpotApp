package com.audiospotapplication.UI.changePassword

import android.view.View
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.audiospotapplication.UI.BaseActivity

class ChangePasswordActivity : BaseActivity() {

    override fun getHeaderTitle(): String {
        return "Change Password"
    }

    override fun getArrowHeaderVisibility(): Int {
        return View.GONE
    }

    override fun getFragment(ivArrow: ImageView): Fragment = ChangePasswordFragment.newInstance()
}
