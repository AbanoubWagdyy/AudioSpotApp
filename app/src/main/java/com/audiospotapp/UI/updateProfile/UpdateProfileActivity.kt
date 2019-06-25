package com.audiospotapp.UI.updateProfile

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.audiospotapp.UI.ActiveTab
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.audiospotapp.UI.BaseActivity

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