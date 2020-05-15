package com.audiospotapplication.ui.contactUs


import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.audiospotapplication.R
import com.audiospotapplication.utils.DialogUtils
import kotlinx.android.synthetic.main.fragment_contact_us.*
import android.content.Intent.ACTION_VIEW
import android.net.Uri
import android.os.Handler
import com.audiospotapplication.ui.BaseFragment
import com.audiospotapplication.data.retrofit.GlobalKeys
import com.audiospotapplication.ui.homepage.HomepageActivity
import com.google.android.material.snackbar.Snackbar
import android.view.inputmethod.InputMethodManager


class ContactUsFragment : BaseFragment(), ContactUsContract.View {

    override fun showHompageScreen() {

        Handler().postDelayed({
            val intent = Intent(requireActivity(), HomepageActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or
                    Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(intent)
            requireActivity().finish()
        }, 2500)
    }

    override fun showMessage(message: String) {
        Snackbar.make(requireActivity().findViewById(android.R.id.content), message, Snackbar.LENGTH_SHORT).show()
    }

    override fun getAppContext(): Context? = requireActivity().applicationContext

    override fun showLoadingDialog() {
        DialogUtils.showProgressDialog(requireActivity(), "Loading ...")
    }

    override fun dismissLoading() {
        DialogUtils.dismissProgressDialog()
    }

    override fun finalizeView() {
        requireActivity().finish()
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
            hideKeyboard(requireActivity())
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

    fun hideKeyboard(activity: Activity) {
        val imm = activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        //Find the currently focused view, so we can grab the correct window token from it.
        var view = activity.getCurrentFocus()
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = View(activity)
        }
        imm.hideSoftInputFromWindow(view!!.getWindowToken(), 0)
    }

    lateinit var mPresenter: ContactUsContract.Presenter
}