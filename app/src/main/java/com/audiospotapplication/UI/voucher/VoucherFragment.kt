package com.audiospotapplication.UI.voucher


import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.audiospotapplication.UI.BaseFragment

import com.audiospotapplication.R
import com.audiospotapplication.UI.promoCodeCongratulations.CongratulationsActivity
import com.audiospotapplication.utils.DialogUtils
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_voucher.*

class VoucherFragment : BaseFragment(), VoucherContract.View {

    override fun showMessage(message: String) {
        if (activity != null)
            Snackbar.make(
                requireActivity().findViewById(android.R.id.content),
                message,
                Snackbar.LENGTH_SHORT
            ).show()
    }

    override fun showCongratulationScreen() {
        val intent = Intent(requireActivity(), CongratulationsActivity::class.java)
        startActivity(intent)
        requireActivity().finish()
    }

    override fun getAppContext(): Context? {
        return requireActivity().applicationContext
    }

    override fun showLoadingDialog() {
        DialogUtils.showProgressDialog(requireActivity(), "Loading ....")
    }

    override fun dismissLoading() {
        DialogUtils.dismissProgressDialog()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_voucher, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mPresenter = VoucherPresenter(this)
        mPresenter.start()

        btnSubmit.setOnClickListener {
            mPresenter.applyVoucher(voucher.text.toString())
        }
    }

    lateinit var mPresenter: VoucherContract.Presenter

    companion object {
        @JvmStatic
        fun newInstance() =
            VoucherFragment()
    }
}
