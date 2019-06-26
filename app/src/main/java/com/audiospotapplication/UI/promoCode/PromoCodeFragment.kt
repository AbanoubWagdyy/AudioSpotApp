package com.audiospotapplication.UI.promoCode


import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.audiospotapplication.R
import com.audiospotapplication.UI.payment.PaymentActivity
import com.audiospotapplication.utils.DialogUtils
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_promo_code.*

class PromoCodeFragment : Fragment(), PromoCodeContract.View {

    override fun setSubTotal(toString: String) {
        subTotal.visibility = View.VISIBLE
        subTotal.text = "SubTotal: $toString"
    }

    override fun setDiscount(toString: String) {
        discount.visibility = View.VISIBLE
        discount.text = "Discount: $toString"
    }

    override fun setTotal(toString: String) {
        total.visibility = View.VISIBLE
        total.text = "Total: $toString"
    }

    override fun showMessage(message: String) {
        Snackbar.make(
            activity!!.findViewById(android.R.id.content),
            message,
            Snackbar.LENGTH_SHORT
        ).show()
    }

    override fun getAppContext(): Context? {
        return activity!!.applicationContext
    }

    override fun showLoadingDialog() {
        DialogUtils.showProgressDialog(activity!!, "Loading ...")
    }

    override fun dismissLoading() {
        DialogUtils.dismissProgressDialog()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_promo_code, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mPresenter = PromoCodePresenter(this)
        mPresenter.start()

        subTotal.visibility = View.GONE
        discount.visibility = View.GONE
        total.visibility = View.GONE

        btnApply.setOnClickListener {
            mPresenter.applyPromoCode(etPromoCode.text.toString())
        }
        proceedToPayment.setOnClickListener {
            val intent = Intent(activity!!, PaymentActivity::class.java)
            startActivity(intent)
            activity!!.finish()
        }
    }

    lateinit var mPresenter: PromoCodeContract.Presenter

    companion object {
        @JvmStatic
        fun newInstance() =
            PromoCodeFragment()
    }
}
