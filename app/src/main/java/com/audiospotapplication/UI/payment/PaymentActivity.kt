package com.audiospotapplication.UI.payment

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
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
import java.util.*

class PaymentActivity : AppCompatActivity(), PaymentContract.View, FawrySdkCallback {

    override fun showLoginPage() {
        mPresenter.resetRepo()
        val intent = Intent(this, LoginActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or
                Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
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

    override fun setPayableItems(
        items: ArrayList<PayableItem>,
        isEnglish: Boolean
    ) {
        initSDK(items, isEnglish)
    }

    fun initSDK(
        items: ArrayList<PayableItem>,
        english: Boolean
    ) {
//        FawryPluginAppClass.enableLogging = true
        var merchantID: String? = "wq9PvdmMBL0="
//        merchantID = "2n5QDrrCsUU="
        val serverUrl = "https://atfawry.fawrystaging.com"

        val digits = 10
        val n = nDigitRandomNo(digits)

        val merchantRefNum: String? = n.toString()
        try {
            FawrySdk.initialize(
                this,
                serverUrl,
                this,
                merchantID,
                merchantRefNum,
                items,
                if (english) FawrySdk.Language.EN else FawrySdk.Language.AR,
                300,
                null,
                UUID(1, 2)
            )
            Handler().postDelayed({
                FawrySdk.startPaymentActivity(this)
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

    lateinit var mPresenter: PaymentContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        FawrySdk.init(FawrySdk.Styles.STYLE2)
        setContentView(R.layout.activity_payment)

        updateAndroidSecurityProvider(this)

        mPresenter = PaymentPresenter(this, intent.extras)
        mPresenter.start()
    }

    private fun updateAndroidSecurityProvider(activity: PaymentActivity) {
        try {
            ProviderInstaller.installIfNeeded(this)
        } catch (e: GooglePlayServicesNotAvailableException) {
            Log.e("SecurityException", "Google Play Services not available.")
        }
    }

    private fun nDigitRandomNo(digits: Int): Int {
        val max = Math.pow(10.0, digits.toDouble()).toInt() - 1
        val min = 0
        val range = max - min
        val r = Random()
        val x = r.nextInt(range)// This will generate random integers in range 0 - 8999999
        return x + min
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 300) {
            if (resultCode == Activity.RESULT_OK) {
                val requestResult = data!!.getIntExtra(FawryPluginAppClass.REQUEST_RESULT, -1)
                if (requestResult == FawryPluginAppClass.SUCCESS_CODE) {

                } else if (requestResult == FawryPluginAppClass.FAILURE_CODE) {
                }
            } else {
            }
        }
    }
}