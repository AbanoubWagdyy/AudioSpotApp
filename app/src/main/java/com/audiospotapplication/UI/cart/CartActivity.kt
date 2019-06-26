package com.audiospotapplication.UI.cart

import android.view.View
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.audiospotapplication.UI.BaseActivity
import kotlinx.android.synthetic.main.back_header.*

class CartActivity : BaseActivity() {


    private var fragment = CartFragment.newInstance()

    override fun getHeaderTitle(): String {
        return "Cart"
    }

    override fun getArrowHeaderVisibility(): Int {
        return View.GONE
    }

    override fun getFragment(ivArrow: ImageView): Fragment {
        return fragment
    }

    override fun manageEditVisibility() {
        ivEdit.visibility = View.VISIBLE
        ivEdit.setOnClickListener {
           fragment.onEditCartClicked()
        }
    }
}