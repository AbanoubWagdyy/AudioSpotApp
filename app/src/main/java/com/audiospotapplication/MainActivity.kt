package com.audiospotapplication

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.CheckBox
import android.widget.EditText
import com.audiospotapplication.UI.payment.Item

import com.emeint.android.fawryplugin.Plugininterfacing.FawrySdk
import com.emeint.android.fawryplugin.Plugininterfacing.PayableItem
import com.emeint.android.fawryplugin.exceptions.FawryException
import com.emeint.android.fawryplugin.interfaces.FawrySdkCallback
import com.emeint.android.fawryplugin.managers.FawryPluginAppClass
import com.emeint.android.fawryplugin.utils.UiUtils
import com.google.android.gms.common.GooglePlayServicesNotAvailableException
import com.google.android.gms.common.GooglePlayServicesRepairableException
import com.google.android.gms.common.GooglePlayServicesUtil
import com.google.android.gms.security.ProviderInstaller

import java.security.SecureRandom
import java.util.ArrayList
import java.util.Random
import java.util.UUID

//AppCompatActivity
class MainActivity : AppCompatActivity(), FawrySdkCallback {

    private var serverUrlEditText: EditText? = null

    // init the payment plugin
    private var nameEditText1: EditText? = null
    private var nameEditText2: EditText? = null
    private var priceEditText1: EditText? = null
    private var priceEditText2: EditText? = null
    private var qtyEditText1: EditText? = null
    private var qtyEditText2: EditText? = null
    private var addItemCheckBox1m: CheckBox? = null
    private var addItemCheckBox2: CheckBox? = null
    private var needShippingCheckBox: CheckBox? = null
    private var openInEnglishCheckBox: CheckBox? = null
    private var enableLoggingCheckBox: CheckBox? = null

    private var connectToIntegrationServer: CheckBox? = null

    private val mRandom = SecureRandom()

    private val requestID: Int = 0
    private val PAYMENT_PLUGIN_REQUEST = 1023


    private var merchantIDEditText: EditText? = null
    private var useWrittenMerchantID: CheckBox? = null


    private var languageEditText: EditText? = null
    private var useWrittenLanguage: CheckBox? = null

    private fun initUi() {
        openInEnglishCheckBox = findViewById<View>(R.id.language_option) as CheckBox
        needShippingCheckBox = findViewById<View>(R.id.shipping_option) as CheckBox
        enableLoggingCheckBox = findViewById<View>(R.id.enable_loggin_option) as CheckBox
        connectToIntegrationServer = findViewById<View>(R.id.server_option_check_box) as CheckBox

        addItemCheckBox1m = findViewById<View>(R.id.select_item_1) as CheckBox
        addItemCheckBox2 = findViewById<View>(R.id.select_item_2) as CheckBox

        serverUrlEditText = findViewById<View>(R.id.server_url_edittext) as EditText

        nameEditText1 = findViewById<View>(R.id.name_edit_text_1) as EditText
        priceEditText1 = findViewById<View>(R.id.price_edit_text_1) as EditText

        nameEditText2 = findViewById<View>(R.id.name_edit_text_2) as EditText
        priceEditText2 = findViewById<View>(R.id.price_edit_text_2) as EditText

        qtyEditText1 = findViewById<View>(R.id.qty_edit_text_1) as EditText
        qtyEditText2 = findViewById<View>(R.id.qty_edit_text_2) as EditText

        useWrittenMerchantID = findViewById<View>(R.id.use_written_checkbox) as CheckBox
        merchantIDEditText = findViewById<View>(R.id.merchant_value_edit_text) as EditText

        languageEditText = findViewById<View>(R.id.language_value_edit_text) as EditText
        useWrittenLanguage = findViewById<View>(R.id.use_written_language_checkbox) as CheckBox

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FawrySdk.init(FawrySdk.Styles.STYLE2)
        setContentView(R.layout.activity_main2)
        updateAndroidSecurityProvider(this)
        initUi()
    }

    fun initSDK(view: View) {
        var openInEnglish = true

        val items = ArrayList<PayableItem>()

        if (addItemCheckBox1m!!.isChecked) {
            val item = Item()
            item.setPrice(priceEditText1!!.text.toString())
            item.setDescription(nameEditText1!!.text.toString())
            item.qty = qtyEditText1!!.text.toString()
            item.sku = "1"
            items.add(item)
        }

        if (addItemCheckBox2!!.isChecked) {
            val item = Item()
            item.setPrice(priceEditText2!!.text.toString())
            item.setDescription(nameEditText2!!.text.toString())
            item.qty = qtyEditText2!!.text.toString()
            item.sku = "2"
            items.add(item)
        }

        FawryPluginAppClass.enableLogging = enableLoggingCheckBox!!.isChecked
        needShippingCheckBox!!.visibility = View.GONE
        openInEnglish = openInEnglishCheckBox!!.isChecked
        var merchantID: String? = null
        merchantID = merchantIDEditText!!.text.toString()
        val serverUrl = serverUrlEditText!!.text.toString()

        val digits = 10
        val n = nDigitRandomNo(digits)

        val merchantRefNum: String? = n.toString()
        try {
            FawrySdk.initialize(this, serverUrl, this, merchantID, merchantRefNum, items, if (openInEnglish) FawrySdk.Language.EN else FawrySdk.Language.AR, PAYMENT_PLUGIN_REQUEST, null, UUID(1, 2))
        } catch (e: FawryException) {
            UiUtils.showDialog(this, e, false)
            e.printStackTrace()
        }

    }

    private fun updateAndroidSecurityProvider(callingActivity: Activity) {
        try {
            ProviderInstaller.installIfNeeded(this)
        } catch (e: GooglePlayServicesRepairableException) {
            //             Thrown when Google Play Services is not installed, up-to-date, or enabled
            //             Show dialog to allow users to install, update, or otherwise enable Google Play services.
            GooglePlayServicesUtil.getErrorDialog(e.connectionStatusCode, callingActivity, 0)
        } catch (e: GooglePlayServicesNotAvailableException) {
            Log.e("SecurityException", "Google Play Services not available.")
        }

    }

    fun onFawryLogoClick(view: View) {

        if (false) {
            val p = Item()
            val items = ArrayList<PayableItem>()
            items.add(p)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PAYMENT_PLUGIN_REQUEST) {
            if (resultCode == Activity.RESULT_OK) {
                val requestResult = data!!.getIntExtra(FawryPluginAppClass.REQUEST_RESULT, -1)
                if (requestResult == FawryPluginAppClass.SUCCESS_CODE) {

                } else if (requestResult == FawryPluginAppClass.FAILURE_CODE) {

                }
            } else {
            }
        }
    }

    fun onFawryLogoShippingClick(view: View) {}

    override fun onSuccess(trxId: String, customParams: Any) {
        Log.d("","")
    }

    override fun onFailure(errorMessage: String) {
        Log.d("","")
    }

    private fun nDigitRandomNo(digits: Int): Int {
        val max = Math.pow(10.0, digits.toDouble()).toInt() - 1 //for digits =7, max will be 9999999
        val min = Math.pow(10.0, (digits - 1).toDouble()).toInt() //for digits = 7, min will be 1000000
        val range = max - min //This is 8999999
        val r = Random()
        val x = r.nextInt(range)// This will generate random integers in range 0 - 8999999
        return x + min
    }
}