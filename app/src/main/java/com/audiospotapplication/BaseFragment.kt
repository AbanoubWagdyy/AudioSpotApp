package com.audiospotapplication

import android.content.Intent
import androidx.fragment.app.Fragment
import com.audiospotapplication.UI.login.LoginActivity

open class BaseFragment() : Fragment(), BaseView {

    override fun showLoginPage() {
        val intent = Intent(activity!!, LoginActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
        activity!!.finish()
    }
}