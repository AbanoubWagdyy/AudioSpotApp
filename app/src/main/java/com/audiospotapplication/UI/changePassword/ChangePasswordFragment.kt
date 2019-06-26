package com.audiospotapplication.UI.changePassword


import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.audiospotapplication.R
import com.audiospotapplication.utils.DialogUtils
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_change_password.*


class ChangePasswordFragment : Fragment(), ChangePasswordContract.View {

    override fun showLoading() {
        DialogUtils.showProgressDialog(activity!!,"Loading ...")
    }

    override fun dismissLoading() {
        DialogUtils.dismissProgressDialog()
    }

    override fun getAppContext(): Context? {
        return activity!!.applicationContext
    }

    override fun showIncorrectOldPassword(s: String) {
        Snackbar.make(activity!!.findViewById(android.R.id.content), s, Snackbar.LENGTH_SHORT).show()
    }

    override fun showCompleteAllFieldsMessage(s: String) {
        Snackbar.make(activity!!.findViewById(android.R.id.content), s, Snackbar.LENGTH_SHORT).show()
    }

    override fun finalizeView() {
        activity!!.finish()
    }

    override fun showErrorMessage(message: String) {
        Snackbar.make(activity!!.findViewById(android.R.id.content), message, Snackbar.LENGTH_SHORT).show()
    }

    lateinit var mPresenter: ChangePasswordContract.Presenter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_change_password, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mPresenter = ChangePasswordPresenter(this)

        btnUpdate.setOnClickListener {
            mPresenter.updatePassword(
                etOldPassword.text.toString(),
                etNewPassword.text.toString(),
                etConfirmPassword.text.toString()
            )
        }
    }


    companion object {
        @JvmStatic
        fun newInstance() =
            ChangePasswordFragment()
    }
}
