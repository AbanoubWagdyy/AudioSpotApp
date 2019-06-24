package com.audiospotapp.UI.voucher

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.audiospotapp.UI.BaseActivity

class VoucherActivity : BaseActivity() {

    override fun getHeaderTitle(): String {
        return "Books"
    }

    override fun getArrowHeaderVisibility(): Int {
        return View.GONE
    }

    override fun getFragment(ivArrow: ImageView): Fragment {
        return VoucherFragment.newInstance()
    }

    override fun getActiveTab(): ActiveTab {
        return ActiveTab.MENU
    }
}
