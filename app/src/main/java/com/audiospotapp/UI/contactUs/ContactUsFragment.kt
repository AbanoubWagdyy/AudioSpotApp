package com.audiospotapp.UI.contactUs


import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.audiospotapp.R
import com.audiospotapp.utils.DialogUtils
import kotlinx.android.synthetic.main.fragment_contact_us.*


class ContactUsFragment : Fragment(), ContactUsContract.View {

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
            mPresenter.contactUs(email.text.toString(), subject.text.toString(), message.text.toString())
        }
    }


    companion object {
        @JvmStatic
        fun newInstance() =
            ContactUsFragment()
    }

    lateinit var mPresenter: ContactUsContract.Presenter
}
