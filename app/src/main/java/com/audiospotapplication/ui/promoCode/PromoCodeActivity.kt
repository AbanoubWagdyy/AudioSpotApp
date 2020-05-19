package com.audiospotapplication.ui.promoCode

import android.view.View
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.audiospotapplication.ui.BaseActivity

class PromoCodeActivity : BaseActivity() {

    override fun getHeaderTitle(): String {
        return "Promo Code"
    }

    override fun getArrowHeaderVisibility(): Int {
        return View.GONE
    }

    override fun getFragment(ivArrow: ImageView): Fragment {
        return PromoCodeFragment.newInstance(intent.extras!!.getInt("price_before_promo"))
    }
}