package com.audiospotapp.UI.payment

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.audiospotapp.DataLayer.DataRepository
import com.audiospotapp.DataLayer.Retrofit.GlobalKeys
import com.audiospotapp.R
import im.delight.android.webview.AdvancedWebView
import kotlinx.android.synthetic.main.activity_payment.*
import org.apache.http.client.methods.HttpPost
import org.apache.http.impl.client.DefaultHttpClient
import java.net.URL
import java.io.BufferedReader
import java.io.InputStreamReader
import org.apache.http.impl.client.BasicResponseHandler
import org.apache.http.client.entity.UrlEncodedFormEntity
import org.apache.http.HttpResponse
import org.apache.http.NameValuePair
import org.apache.http.message.BasicNameValuePair




class PaymentActivity : AppCompatActivity(), AdvancedWebView.Listener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment)

        var mRepositorySource = DataRepository.getInstance(applicationContext)

        webView.addHttpHeader("AUTHORIZATION",mRepositorySource.getAuthResponse()!!.data.token)
        webView.addHttpHeader("APIKEY",GlobalKeys.API_KEY)
        webView.addHttpHeader("LANG","en")
        webView.addHttpHeader("DEVICEKEY",mRepositorySource.getDeviceToken())
        webView.addHttpHeader("PROMO",mRepositorySource.getPromoCode())
        webView.addHttpHeader("SENDTO","")
        webView.addHttpHeader("VOUCHER",mRepositorySource.getVoucher().toString())
        webView.setThirdPartyCookiesEnabled(true)

        webView.setListener(this, this)
        webView.loadUrl("https://www.audio-spot.com/payments", null);
    }

    override fun onResume() {
        super.onResume()
        webView.onResume()
    }

    override fun onPause() {
        super.onPause()
        webView.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        webView.onDestroy()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onBackPressed() {
        if (!webView.onBackPressed()) {
            return; }
        super.onBackPressed()
    }


    override fun onPageFinished(url: String?) {

    }

    override fun onPageError(errorCode: Int, description: String?, failingUrl: String?) {

    }

    override fun onDownloadRequested(
        url: String?,
        suggestedFilename: String?,
        mimeType: String?,
        contentLength: Long,
        contentDisposition: String?,
        userAgent: String?
    ) {

    }

    override fun onExternalPageRequest(url: String?) {

    }

    override fun onPageStarted(url: String?, favicon: Bitmap?) {

    }
}