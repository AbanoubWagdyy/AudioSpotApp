package com.audiospotapplication.UI.myFavourite

import android.view.View
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.audiospotapplication.UI.BaseActivity
import com.audiospotapplication.UI.onItemPlayClickListener
import dm.audiostreamer.MediaMetaData
import kotlinx.android.synthetic.main.back_header.*

class MyFavouriteBooksActivity : BaseActivity(), onItemPlayClickListener {

    override fun OnItemPlayed(mediaData: MediaMetaData) {
        super.OnItemPlayed(mediaData)
    }

    override fun getHeaderTitle(): String {
        return "My Favorites"
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

    var fragment = MyFavouriteBooksFragment.newInstance()
}