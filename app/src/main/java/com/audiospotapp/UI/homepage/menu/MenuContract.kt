package com.audiospotapp.UI.homepage.menu

import android.content.Context

interface MenuContract {

    interface Presenter {
        fun handleProfilePressed()
        fun handleMyFavouritePressed()
        fun handleSettingsPressed()
        fun handleReceviveGiftPressed()
        fun handleTermsAndConditionsPressed()
        fun handleContactUsPressed()
        fun handleAboutUsPressed()
        fun handleSignoutPressed()
        fun handleNotificationsPressed()
        fun start()
        fun handleMyBookmarksClicked()

    }

    interface View {
        fun getAppContext(): Context?
        fun showShouldBeLoggedInMessage()
        fun showProfilePageScreen()
        fun showMyFavouriteScreen()
        fun showLoginScreen()
        fun showErrorMessage()
        fun showLoading()
        fun dismissLoading()
        fun showContactUsScreen()
        fun showSettingsScreen()
        fun showMyBookmarksScreen()
        fun showVoucherScreen()
    }
}