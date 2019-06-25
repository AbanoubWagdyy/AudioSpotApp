package com.audiospotapp.UI.promoCode

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.audiospotapp.R
import com.audiospotapp.UI.BaseActivity

class PromoCodeActivity : BaseActivity() {
    override fun getHeaderTitle(): String {
        return "Promo Code"
    }

    override fun getArrowHeaderVisibility(): Int {
        return View.GONE
    }

    override fun getFragment(ivArrow: ImageView): Fragment {
        return PromoCodeFragment.newInstance()
    }
}
