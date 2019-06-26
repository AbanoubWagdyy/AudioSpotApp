package com.audiospotapplication.UI.settings

import android.view.View
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.audiospotapplication.UI.BaseActivity

class SettingsActivity : BaseActivity() {

    override fun getHeaderTitle(): String {
        return "Settings"
    }

    override fun getArrowHeaderVisibility(): Int {
        return View.GONE
    }

    override fun getFragment(ivArrow: ImageView): Fragment = SettingsFragment.newInstance()
}