package com.audiospotapplication

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.audiospotapplication.DataLayer.Model.BaseViewModel
import com.audiospotapplication.DataLayer.Retrofit.RetrofitResponseHandler

import com.audiospotapplication.UI.login.LoginActivity
import com.audiospotapplication.utils.DialogUtils
import com.google.android.material.snackbar.Snackbar
import org.koin.androidx.viewmodel.ext.android.viewModel

open class BaseFragment : Fragment(), BaseView {

    private val baseViewModel: BaseViewModel by viewModel()

    override fun showLoginPage() {
        baseViewModel.clearData()

        val intent = Intent(activity!!, LoginActivity::class.java)
        intent.addFlags(
            Intent.FLAG_ACTIVITY_CLEAR_TOP or
                    Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        )
        startActivity(intent)
        activity!!.finish()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        baseViewModel.getObserverForError().observe(this, Observer {
            Snackbar.make(activity!!.findViewById(android.R.id.content), it, Snackbar.LENGTH_SHORT)
                .show()
        })

        baseViewModel.getProgressLoadingObserver().observe(this, Observer {
            if (it) {
                DialogUtils.showProgressDialog(activity!!, context!!.getString(R.string.loading))
            }
        })

        baseViewModel.getErrorAuthenticationObserver().observe(this, Observer {
            if (it == RetrofitResponseHandler.Companion.Status.UNAUTHORIZED) {
                showLoginPage()
            }
        })
    }
}