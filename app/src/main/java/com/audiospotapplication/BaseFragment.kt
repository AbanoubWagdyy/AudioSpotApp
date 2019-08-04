package com.audiospotapplication

import android.content.Intent
import androidx.fragment.app.Fragment
import com.audiospotapplication.DataLayer.DataRepository
import com.audiospotapplication.UI.login.LoginActivity
import com.visionvalley.letuno.DataLayer.RepositorySource

open class BaseFragment() : Fragment(), BaseView {

    lateinit var mRepoSource: RepositorySource

    override fun showLoginPage() {
        mRepoSource = DataRepository.getInstance(context!!)
        mRepoSource.clear()
        val intent = Intent(activity!!, LoginActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or
                Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)
        activity!!.finish()
    }
}