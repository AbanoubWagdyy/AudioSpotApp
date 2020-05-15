package com.audiospotapplication.ui.homepage.menu

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.audiospotapplication.ui.BaseFragment

import com.audiospotapplication.R
import com.audiospotapplication.ui.contactUs.ContactUsActivity
import com.audiospotapplication.ui.login.LoginActivity
import com.audiospotapplication.ui.myBookmarks.MyBookmarksActivity
import com.audiospotapplication.ui.myFavourite.MyFavouriteBooksActivity
import com.audiospotapplication.ui.profile.ProfileActivity
import com.audiospotapplication.ui.settings.SettingsActivity
import com.audiospotapplication.ui.splash.SplashActivity
import com.audiospotapplication.ui.voucher.VoucherActivity
import com.audiospotapplication.utils.DialogUtils
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_menu.*

class MenuFragment : BaseFragment(), MenuContract.View {

    override fun showVoucherScreen() {
        val intent = Intent(requireActivity(), VoucherActivity::class.java)
        startActivity(intent)
    }

    override fun showMyBookmarksScreen() {
        val intent = Intent(requireActivity(), MyBookmarksActivity::class.java)
        startActivity(intent)
    }

    override fun showSettingsScreen() {
        val intent = Intent(requireActivity(), SettingsActivity::class.java)
        startActivity(intent)
    }

    override fun showContactUsScreen() {
        val intent = Intent(requireActivity(), ContactUsActivity::class.java)
        startActivity(intent)
    }

    override fun showLoading() {
        DialogUtils.showProgressDialog(requireActivity(), "Signing Out ...")
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
            val mainIntent = Intent(requireActivity(), LoginActivity::class.java)
            requireActivity().startActivity(mainIntent)
        }, 500)
    }

    override fun showProfilePageScreen() {
        val intent = Intent(requireActivity(), ProfileActivity::class.java)
        startActivity(intent)
    }

    override fun showMyFavouriteScreen() {
        val intent = Intent(requireActivity(), MyFavouriteBooksActivity::class.java)
        startActivity(intent)
    }

    override fun showLoginScreen() {
        val intent = Intent(requireActivity(), SplashActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }

    override fun showErrorMessage() {
        if (activity != null)
            Snackbar.make(
                requireActivity().findViewById(android.R.id.content),
                "Please Check Your Internet Connection",
                Snackbar.LENGTH_SHORT
            ).show()
    }

    override fun getAppContext(): Context? {
        return requireActivity().applicationContext
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