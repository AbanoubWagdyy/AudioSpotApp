package com.audiospotapplication.ui.promoCode


import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.audiospotapplication.ui.BaseFragment

import com.audiospotapplication.R
import com.audiospotapplication.ui.payment.PaymentActivity
import com.audiospotapplication.utils.DialogUtils
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_promo_code.*

class PromoCodeFragment : BaseFragment(), PromoCodeContract.View {

    override fun showPayment(promoCode: String) {
        val intent = Intent(requireActivity(), PaymentActivity::class.java)
        intent.putExtra("promoCode", promoCode)
        startActivity(intent)
        requireActivity().finish()
    }

    override fun setSubTotal() {
        subTotal.visibility = View.VISIBLE
        subTotal.text = "SubTotal: ${requireArguments().getInt("price_before_promo")} L.E"
    }

    var discountValue: Int = 0

    override fun setDiscount(toString: String) {
        discountValue = toString.toInt()
        discount.visibility = View.VISIBLE
        discount.text = "Discount: $toString %"
    }

    override fun setTotal() {
        val totalValue =
            (requireArguments().getInt("price_before_promo")) - ((requireArguments().getInt("price_before_promo") * discountValue) / 100)
        total.visibility = View.VISIBLE
        total.text = "Total: $totalValue L.E"
    }

    override fun showMessage(message: String) {
        if (activity != null)
            Snackbar.make(
                requireActivity().findViewById(android.R.id.content),
                message,
                Snackbar.LENGTH_SHORT
            ).show()
    }

    override fun getAppContext(): Context? {
        return requireActivity().applicationContext
    }

    override fun showLoadingDialog() {
        DialogUtils.showProgressDialog(requireActivity(), "Loading ...")
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

        btnApply.setOnClickListener {
            mPresenter.applyPromoCode(etPromoCode.text.toString())
        }

        proceedToPayment.setOnClickListener {
            mPresenter.handleProceedToPaymentClicked()
        }
    }

    lateinit var mPresenter: PromoCodeContract.Presenter

    companion object {
        @JvmStatic
        fun newInstance(price_before_promo: Int): PromoCodeFragment {
            val bundle = Bundle()
            bundle.putInt("price_before_promo", price_before_promo)
            val fragment = PromoCodeFragment()
            fragment.arguments = bundle
            return fragment
        }
    }
}