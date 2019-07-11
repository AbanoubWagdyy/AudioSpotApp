package com.audiospotapplication.UI.payment

import android.content.Intent
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.audiospotapplication.DataLayer.DataRepository
import com.audiospotapplication.DataLayer.Retrofit.GlobalKeys
import com.audiospotapplication.R
import im.delight.android.webview.AdvancedWebView
import kotlinx.android.synthetic.main.activity_payment.*
import kotlinx.android.synthetic.main.header.*


class PaymentActivity : AppCompatActivity(), AdvancedWebView.Listener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment)
        
        tvTitle.text = "Payment"

        var bookId = 0
        var mRepositorySource = DataRepository.getInstance(applicationContext)
        if (mRepositorySource.getSavedBook() != null) {
            bookId = mRepositorySource.getSavedBook()!!.id
        }

        webView.addHttpHeader("AUTHORIZATION", mRepositorySource.getAuthResponse()!!.data.token)
        webView.addHttpHeader("APIKEY", GlobalKeys.API_KEY)
        webView.addHttpHeader("LANG", "en")
        webView.addHttpHeader("DEVICEKEY", mRepositorySource.getDeviceToken())
        webView.addHttpHeader("PROMO", mRepositorySource.getPromoCode())
        webView.addHttpHeader("SENDTO", "")
        webView.addHttpHeader("VOUCHER", mRepositorySource.getVoucher().toString())
        if (bookId != 0)
            webView.addHttpHeader("BOOKID", bookId.toString())

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