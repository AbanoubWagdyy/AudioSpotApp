package com.audiospotapp.UI.promoCodeCongratulations

import android.view.View
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.audiospotapp.DataLayer.DataRepository
import com.audiospotapp.UI.BaseActivity

class CongratulationsActivity : BaseActivity() {

    override fun getHeaderTitle(): String {
        return "Books"
    }

    override fun getArrowHeaderVisibility(): Int {
        return View.GONE
    }

    override fun getFragment(ivArrow: ImageView): Fragment {
        return CongratulationsFragment.newInstance()
    }

    override fun onStop() {
        var mRepositorySource = DataRepository.getInstance(applicationContext)
        mRepositorySource.saveVoucherBook(null)
        super.onStop()
    }
}
