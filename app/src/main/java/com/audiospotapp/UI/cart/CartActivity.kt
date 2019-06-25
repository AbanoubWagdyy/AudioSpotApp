package com.audiospotapp.UI.cart

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.audiospotapp.UI.BaseActivity
import com.audiospotapp.UI.updateProfile.UpdateProfileActivity
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