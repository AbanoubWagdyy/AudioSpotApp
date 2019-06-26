package com.audiospotapplication.UI.contactUs


import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.audiospotapplication.R
import com.audiospotapplication.utils.DialogUtils
import kotlinx.android.synthetic.main.fragment_contact_us.*
import android.content.Intent.ACTION_VIEW
import android.net.Uri
import com.audiospotapplication.DataLayer.Retrofit.GlobalKeys
import com.audiospotapplication.UI.homepage.HomepageActivity
import com.google.android.material.snackbar.Snackbar


class ContactUsFragment : Fragment(), ContactUsContract.View {

    override fun showHompageScreen() {
        val intent = Intent(activity!!, HomepageActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
        activity!!.finish()
    }

    override fun showMessage(message: String) {
        Snackbar.make(activity!!.findViewById(android.R.id.content), message, Snackbar.LENGTH_SHORT).show()
    }

    override fun getAppContext(): Context? = activity!!.applicationContext

    override fun showLoadingDialog() {
        DialogUtils.showProgressDialog(activity!!, "Loading ...")
    }

    override fun dismissLoading() {
        DialogUtils.dismissProgressDialog()
    }

    override fun finalizeView() {
        activity!!.finish()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_contact_us, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mPresenter = ContactUsPresenter(this)
        mPresenter.start()
        send.setOnClickListener {
            mPresenter.contactUs(message.text.toString())
        }

        website.setOnClickListener {
            val url = GlobalKeys.Share.WEBSITE
            val i = Intent(ACTION_VIEW)
            i.data = Uri.parse(url)
            startActivity(i)
        }

        facebook.setOnClickListener {
            val url = GlobalKeys.Share.FACEBOOK
            val i = Intent(ACTION_VIEW)
            i.data = Uri.parse(url)
            startActivity(i)
        }

        instagram.setOnClickListener {
            val url = GlobalKeys.Share.INSTAGRAM
            val i = Intent(ACTION_VIEW)
            i.data = Uri.parse(url)
            startActivity(i)
        }

        youtube.setOnClickListener {
            val url = GlobalKeys.Share.YOUTUBE
            val i = Intent(ACTION_VIEW)
            i.data = Uri.parse(url)
            startActivity(i)
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            ContactUsFragment()
    }

    lateinit var mPresenter: ContactUsContract.Presenter
}