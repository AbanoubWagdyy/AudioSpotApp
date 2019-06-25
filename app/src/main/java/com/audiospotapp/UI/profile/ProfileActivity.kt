package com.audiospotapp.UI.profile

import android.content.Intent
import android.view.View
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.audiospotapp.UI.BaseActivity
import com.audiospotapp.UI.updateProfile.UpdateProfileActivity
import kotlinx.android.synthetic.main.back_header.*

class ProfileActivity : BaseActivity() {

    override fun getHeaderTitle(): String {
        return "Profile"
    }

    override fun getArrowHeaderVisibility(): Int {
        return View.GONE
    }

    override fun getFragment(ivArrow: ImageView): Fragment {
        return ProfileFragment.newInstance()
    }

    override fun manageEditVisibility() {
        ivEdit.visibility = View.VISIBLE
        ivEdit.setOnClickListener {
            val intent = Intent(this, UpdateProfileActivity::class.java)
            startActivity(intent)
        }
    }
}