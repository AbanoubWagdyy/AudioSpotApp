package com.audiospotapplication.UI.homepage.menu

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.audiospotapplication.BaseFragment

import com.audiospotapplication.R
import com.audiospotapplication.UI.contactUs.ContactUsActivity
import com.audiospotapplication.UI.login.LoginActivity
import com.audiospotapplication.UI.myBookmarks.MyBookmarksActivity
import com.audiospotapplication.UI.myFavourite.MyFavouriteBooksActivity
import com.audiospotapplication.UI.profile.ProfileActivity
import com.audiospotapplication.UI.settings.SettingsActivity
import com.audiospotapplication.UI.splash.SplashActivity
import com.audiospotapplication.UI.voucher.VoucherActivity
import com.audiospotapplication.utils.DialogUtils
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_menu.*

class MenuFragment : BaseFragment(), MenuContract.View {

    override fun showVoucherScreen() {
        val intent = Intent(activity!!, VoucherActivity::class.java)
        startActivity(intent)
    }

    override fun showMyBookmarksScreen() {
        val intent = Intent(activity!!, MyBookmarksActivity::class.java)
        startActivity(intent)
    }

    override fun showSettingsScreen() {
        val intent = Intent(activity!!, SettingsActivity::class.java)
        startActivity(intent)
    }

    override fun showContactUsScreen() {
        val intent = Intent(activity!!, ContactUsActivity::class.java)
        startActivity(intent)
    }

    override fun showLoading() {
        DialogUtils.showProgressDialog(activity!!, "Signing Out ...")
    }

    override fun dismissLoading() {
        DialogUtils.dismissProgressDialog()
    }

    override fun showShouldBeLoggedInMessage() {
        Snackbar.make(
            linearProfile,
            "You have to be logged in first", Snackbar.LENGTH_LONG
        ).show()
        Handler().postDelayed({
            val mainIntent = Intent(activity!!, LoginActivity::class.java)
            activity!!.startActivity(mainIntent)
        }, 500)
    }

    override fun showProfilePageScreen() {
        val intent = Intent(activity!!, ProfileActivity::class.java)
        startActivity(intent)
    }

    override fun showMyFavouriteScreen() {
        val intent = Intent(activity!!, MyFavouriteBooksActivity::class.java)
        startActivity(intent)
    }

    override fun showLoginScreen() {
        val intent = Intent(activity!!, SplashActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }

    override fun showErrorMessage() {
        if (activity != null)
            Snackbar.make(
                activity!!.findViewById(android.R.id.content),
                "Please Check Your Internet Connection",
                Snackbar.LENGTH_SHORT
            ).show()
    }

    override fun getAppContext(): Context? {
        return activity!!.applicationContext
    }

    lateinit var mPresenter: MenuContract.Presenter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_menu, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mPresenter = MenuPresenter(this)
        mPresenter.start()

        linearProfile.setOnClickListener {
            mPresenter.handleProfilePressed()
        }

        linearMyFavourite.setOnClickListener {
            mPresenter.handleMyFavouritePressed()
        }

        linearReceiveGift.setOnClickListener {
            mPresenter.handleReceviveGiftPressed()
        }

        linearSettings.setOnClickListener {
            mPresenter.handleSettingsPressed()
        }

        linearTermsAndConditions.setOnClickListener {
            mPresenter.handleTermsAndConditionsPressed()
        }

        linearSignOut.setOnClickListener {
            mPresenter.handleSignoutPressed()
        }

        linearAboutUs.setOnClickListener {
            mPresenter.handleAboutUsPressed()
        }

        linearContactUs.setOnClickListener {
            mPresenter.handleContactUsPressed()
        }

        linearNotifications.setOnClickListener {
            mPresenter.handleNotificationsPressed()
        }

        linearMyBookmarks.setOnClickListener {
            mPresenter.handleMyBookmarksClicked()
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            MenuFragment()
    }
}