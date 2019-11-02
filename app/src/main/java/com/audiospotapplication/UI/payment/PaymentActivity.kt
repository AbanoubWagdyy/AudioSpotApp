package com.audiospotapplication.UI.payment

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.audiospotapplication.DataLayer.Model.CreateOrderResponse
import com.audiospotapplication.DataLayer.Model.FawryCustomParams
import com.audiospotapplication.DataLayer.Model.PaypalArguments
import com.audiospotapplication.DataLayer.Retrofit.RetrofitCallbacks
import com.audiospotapplication.R
import com.audiospotapplication.UI.login.LoginActivity
import com.audiospotapplication.utils.DialogUtils
import com.emeint.android.fawryplugin.Plugininterfacing.FawrySdk
import com.emeint.android.fawryplugin.Plugininterfacing.PayableItem
import com.emeint.android.fawryplugin.exceptions.FawryException
import com.emeint.android.fawryplugin.interfaces.FawrySdkCallback
import com.emeint.android.fawryplugin.managers.FawryPluginAppClass
import com.emeint.android.fawryplugin.utils.UiUtils
import com.google.android.gms.common.GooglePlayServicesNotAvailableException
import com.google.android.gms.security.ProviderInstaller
import com.paypal.android.sdk.payments.PayPalConfiguration
import com.paypal.android.sdk.payments.PayPalPayment
import com.paypal.android.sdk.payments.PayPalService
import com.paypal.android.sdk.payments.PaymentConfirmation
import kotlinx.android.synthetic.main.activity_payment.*
import org.json.JSONException
import retrofit2.Call
import java.math.BigDecimal
import java.util.*

class PaymentActivity : AppCompatActivity(), PaymentContract.View, FawrySdkCallback {

    val PAYPAL_REQUEST_CODE = 123

    override fun setFawryCustomParams(fawryCustomParams: FawryCustomParams) {
        this.fawryCustomParams = fawryCustomParams
    }

    override fun showLoginPage() {
        mPresenter.resetRepo()
        val intent = Intent(this, LoginActivity::class.java)
        intent.addFlags(
            Intent.FLAG_ACTIVITY_CLEAR_TOP or
                    Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        )
        startActivity(intent)
        finish()
    }

    override fun onSuccess(trxId: String, customParams: Any?) {
        Log.d("", trxId)
        finish()
    }

    override fun onFailure(errorMessage: String?) {
        Log.d("", errorMessage)
        finish()
    }

    fun initSDK(
        items: ArrayList<PayableItem>,
        english: Boolean
    ) {
//        FawryPluginAppClass.enableLogging = true
//        var merchantID: String? = "wq9PvdmMBL0="
        var merchantID: String? = "4iZdY2gYUttKitmTMhLsqw=="
//        val serverUrl = "https://atfawry.fawrystaging.com"
        val serverUrl = "https://www.atfawry.com"
//        if (fawryCustomParams != null) {
//
//            items.add(PayableItem())
//        }

        try {
            FawrySdk.initialize(
                this,
                serverUrl,
                this,
                merchantID,
                mPresenter.getMerchantRefNumber(),
                items,
                if (english) FawrySdk.Language.EN else FawrySdk.Language.AR,
                300,
                fawryCustomParams.toString(),
                uuid
            )
            Handler().postDelayed({
                FawrySdk.startPaymentActivity(this)
                this@PaymentActivity.finish()
            }, 500)
        } catch (e: FawryException) {
            UiUtils.showDialog(this, e, false)
            e.printStackTrace()
            Toast.makeText(this, "Please try again later", Toast.LENGTH_LONG).show()
            finish()
        }
    }

    override fun getAppContext(): Context? {
        return applicationContext
    }

    override fun showLoadingDialog() {
        DialogUtils.showProgressDialog(this, "Loading ...")
    }

    override fun dismissLoading() {
        DialogUtils.dismissProgressDialog()
    }

    private var fawryCustomParams: FawryCustomParams? = null
    lateinit var mPresenter: PaymentContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        FawrySdk.init(FawrySdk.Styles.STYLE2)

        setContentView(R.layout.activity_payment)

        updateAndroidSecurityProvider(this)

        mPresenter = PaymentPresenter(this, intent.extras)
        mPresenter.start()

        btnPayWithFawry.setOnClickListener {
            DialogUtils.showProgressDialog(this, "Initializing Order ...")
            mPresenter.createOrder(
                fawryCustomParams,
                uuid,
                object : RetrofitCallbacks.CreateOrderResponseCallback {
                    override fun onSuccess(result: CreateOrderResponse?) {
                        DialogUtils.dismissProgressDialog()
                        initSDK(
                            mPresenter.getPayItems() as ArrayList<PayableItem>,
                            mPresenter.isEnglish()
                        )
                    }

                    override fun onFailure(call: Call<CreateOrderResponse>?, t: Throwable?) {
                        DialogUtils.dismissProgressDialog()
                        finish()
                    }
                })
        }

        btnPayWithPal.setOnClickListener {
            DialogUtils.showProgressDialog(this, "Initializing Order ...")
            mPresenter.createPaypalOrder(
                RetrofitCallbacks.PaypalArgumentCallback {
                    DialogUtils.dismissProgressDialog()
                    if (it != null) {
                        val config =
                            PayPalConfiguration() // Start with mock environment.  When ready, switch to sandbox (ENVIRONMENT_SANDBOX)
                                .environment(PayPalConfiguration.ENVIRONMENT_PRODUCTION)
                                .clientId(it.key)
                                .merchantName("AudioSpot")
                                .merchantPrivacyPolicyUri(
                                    Uri.parse("https://www.paypal.com/webapps/mpp/ua/privacy-full")
                                )
                                .merchantUserAgreementUri(
                                    Uri.parse("https://www.paypal.com/webapps/mpp/ua/useragreement-full")
                                )  // or live (ENVIRONMENT_PRODUCTION)

                        val payment = PayPalPayment(
                            BigDecimal(mPresenter.getPaypalItemsWholeAmount(it.dollarPrice)),
                            "USD",
                            "ORDER",
                            PayPalPayment.PAYMENT_INTENT_SALE
                        )

                        val intent = Intent(
                            this@PaymentActivity,
                            com.paypal.android.sdk.payments.PaymentActivity::class.java
                        )
                        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config)
                        payment.invoiceNumber(it.invoiceNumber)

                        intent.putExtra(
                            com.paypal.android.sdk.payments.PaymentActivity.EXTRA_PAYMENT,
                            payment
                        )
                        startActivityForResult(intent, PAYPAL_REQUEST_CODE)
                    } else {
                        Toast.makeText(
                            this,
                            "Paypal is not available right now ,please check later !.",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                })
        }
    }

    private fun updateAndroidSecurityProvider(activity: PaymentActivity) {
        try {
            ProviderInstaller.installIfNeeded(this)
        } catch (e: GooglePlayServicesNotAvailableException) {
            Log.e("SecurityException", "Google Play Services not available.")
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 300) {
            if (resultCode == Activity.RESULT_OK) {
                val requestResult = data!!.getIntExtra(FawryPluginAppClass.REQUEST_RESULT, -1)
                if (requestResult == FawryPluginAppClass.SUCCESS_CODE) {
                    Toast.makeText(this, "Payment done successfully", Toast.LENGTH_SHORT).show()
                } else if (requestResult == FawryPluginAppClass.FAILURE_CODE) {
                    Toast.makeText(
                        this,
                        "Fail to pay items ,please try again later",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } else {
            }
        } else if (requestCode == PAYPAL_REQUEST_CODE) {
            //If the result is OK i.e. user has not canceled the payment
            if (resultCode == Activity.RESULT_OK) {
                //Getting the payment confirmation
                val confirm =
                    data?.getParcelableExtra<PaymentConfirmation>(com.paypal.android.sdk.payments.PaymentActivity.EXTRA_RESULT_CONFIRMATION)

                Log.d("CONFIRM", confirm.toString())
                //if confirmation is not null
                if (confirm != null) {
                    try {
                        //Getting the payment details
                        val paymentDetails = confirm.toJSONObject().toString(4)
                        Log.d("paymentExample", paymentDetails)
                        Log.i("paymentExample", paymentDetails)
                        Log.d("Pay Confirm : ", confirm.payment.toJSONObject().toString())

                    } catch (e: JSONException) {
                        Log.e("paymentExample", "an extremely unlikely failure occurred : ", e)
                    }

                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Log.i("paymentExample", "The user canceled.")
            } else if (resultCode == com.paypal.android.sdk.payments.PaymentActivity.RESULT_EXTRAS_INVALID) {
                Log.i(
                    "paymentExample",
                    "An invalid Payment or PayPalConfiguration was submitted. Please see the docs."
                )
            }
        }
    }


    var uuid = UUID(1, 2)
}