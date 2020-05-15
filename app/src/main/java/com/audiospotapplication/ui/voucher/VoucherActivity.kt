package com.audiospotapplication.ui.voucher

import android.view.View
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.audiospotapplication.ui.BaseActivity

class VoucherActivity : BaseActivity() {

    override fun getHeaderTitle(): String {
        return "Voucher"
    }

    override fun getArrowHeaderVisibility(): Int {
        return View.GONE
    }

    override fun getFragment(ivArrow: ImageView): Fragment {
        return VoucherFragment.newInstance()
    }
}