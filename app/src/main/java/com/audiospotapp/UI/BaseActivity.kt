package com.audiospotapp.UI

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.audiospotapp.DataLayer.DataRepository
import com.audiospotapp.DataLayer.Model.BookListResponse
import com.audiospotapp.DataLayer.Retrofit.RetrofitCallbacks
import com.audiospotapp.R
import com.audiospotapp.UI.giveAgift.GiveGiftActivity
import com.audiospotapp.UI.profile.ProfileActivity
import com.audiospotapp.UI.updateProfile.UpdateProfileActivity
import com.visionvalley.letuno.DataLayer.RepositorySource
import kotlinx.android.synthetic.main.activity_base.*
import kotlinx.android.synthetic.main.back_header.*
import kotlinx.android.synthetic.main.header.*
import retrofit2.Call

abstract class BaseActivity : AppCompatActivity() {

    lateinit var ivArrow: ImageView
    lateinit var mRepositorySource: RepositorySource


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_base)

        mRepositorySource = DataRepository.getInstance(applicationContext)

        tvTitle.text = getHeaderTitle()
        ivArrow = findViewById(R.id.ivArrow)
        ivArrow.visibility = getArrowHeaderVisibility()

        ivBack.setOnClickListener {
            finish()
        }

        supportFragmentManager.beginTransaction()
            .replace(R.id.container, getFragment(ivArrow)).commitAllowingStateLoss()

        when (getActiveTab()) {
            ActiveTab.HOME -> {
                ivHome.setImageResource(R.mipmap.tab_home)
                ivMenu.setImageResource(R.mipmap.tab_menu_inactive)
                ivMyBooks.setImageResource(R.mipmap.tab_mybooks_inactive)
                ivLibrary.setImageResource(R.mipmap.tab_library_inactive)
            }
            ActiveTab.LIBRARY -> {
                ivHome.setImageResource(R.mipmap.tab_home_inactive)
                ivMenu.setImageResource(R.mipmap.tab_menu_inactive)
                ivMyBooks.setImageResource(R.mipmap.tab_mybooks_inactive)
                ivLibrary.setImageResource(R.mipmap.tab_library)
            }
            ActiveTab.MYBOOKS -> {
                ivHome.setImageResource(R.mipmap.tab_home_inactive)
                ivMenu.setImageResource(R.mipmap.tab_menu_inactive)
                ivMyBooks.setImageResource(R.mipmap.tab_mybooks)
                ivLibrary.setImageResource(R.mipmap.tab_library_inactive)
            }
            ActiveTab.MENU -> {
                ivHome.setImageResource(R.mipmap.tab_home_inactive)
                ivMenu.setImageResource(R.mipmap.tab_menu)
                ivMyBooks.setImageResource(R.mipmap.tab_mybooks_inactive)
                ivLibrary.setImageResource(R.mipmap.tab_library_inactive)
            }
        }

        manageEditVisibility()

        if (this is GiveGiftActivity) {
            bottom_navigation.visibility = View.GONE
        }
    }

    open fun manageEditVisibility() {
        ivEdit.visibility = View.GONE
    }

    override fun onResume() {
        tvCartCount.visibility = View.INVISIBLE
        mRepositorySource.getMyCart(object : RetrofitCallbacks.BookListCallback {
            override fun onSuccess(result: BookListResponse?) {
                tvCartCount.visibility = View.VISIBLE
                tvCartCount.text = result!!.data.size.toString()
            }

            override fun onFailure(call: Call<BookListResponse>?, t: Throwable?) {
                Log.d("", "")
            }
        })
        super.onResume()
    }

    abstract fun getHeaderTitle(): String
    abstract fun getArrowHeaderVisibility(): Int
    abstract fun getFragment(ivArrow: ImageView): Fragment
    abstract fun getActiveTab(): ActiveTab

    enum class ActiveTab {
        HOME, LIBRARY, MYBOOKS, MENU
    }
}