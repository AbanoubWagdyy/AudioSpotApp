package com.audiospotapp.UI.settings

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.audiospotapp.UI.BaseActivity

class SettingsActivity : BaseActivity() {

    override fun getHeaderTitle(): String {
        return "Settings"
    }

    override fun getArrowHeaderVisibility(): Int {
        return View.GONE
    }

    override fun getFragment(ivArrow: ImageView): Fragment = SettingsFragment.newInstance()

    override fun getActiveTab(): ActiveTab = ActiveTab.MENU

}
