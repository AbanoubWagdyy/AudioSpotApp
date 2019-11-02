package com.audiospotapplication.UI.homepage

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import com.audiospotapplication.R
import com.audiospotapplication.UI.ActiveTab
import com.audiospotapplication.UI.cart.CartActivity
import com.audiospotapplication.UI.homepage.home.HomeFragment
import com.audiospotapplication.UI.homepage.Library.LibraryFragment
import com.audiospotapplication.UI.homepage.menu.MenuFragment
import com.audiospotapplication.UI.homepage.myBooks.MyBooksFragment
import com.audiospotapplication.UI.login.LoginActivity
import com.audiospotapplication.utils.DialogUtils
import com.example.jean.jcplayer.JcPlayerManager
import com.example.jean.jcplayer.JcPlayerManagerListener
import com.example.jean.jcplayer.general.JcStatus
import com.example.jean.jcplayer.model.JcAudio
import com.google.android.material.snackbar.Snackbar
import dm.audiostreamer.MediaMetaData
import kotlinx.android.synthetic.main.activity_homepage.*
import kotlinx.android.synthetic.main.header.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.KoinComponent

class HomepageActivity : AppCompatActivity(), HomeFragment.onItemPlayClickListener,
    MyBooksFragment.onItemPlayClickListener, JcPlayerManagerListener, KoinComponent {

    override fun onPreparedAudio(status: JcStatus) {

    }

    override fun onCompletedAudio() {

    }

    override fun onPaused(status: JcStatus) {

    }

    override fun onContinueAudio(status: JcStatus) {

    }

    override fun onPlaying(status: JcStatus) {

    }

    override fun onTimeChanged(status: JcStatus) {
        DialogUtils.dismissPlayingProgressDialog()
    }

    override fun onStopped(status: JcStatus) {

    }

    override fun onJcpError(throwable: Throwable) {

    }

    override fun OnItemPlayed(mediaData: MediaMetaData) {
        DialogUtils.showPlayingProgressDialog(this)
        playAudio(mediaData)
    }

    private fun playAudio(mediaData: MediaMetaData) {
        jcPlayerManager.kill()
        val audio = JcAudio.createFromURL(
            mediaData.mediaId.toInt(), mediaData.mediaTitle,
            mediaData.mediaUrl, null
        )
        val playlist = ArrayList<JcAudio>()
        playlist.add(audio)

        jcPlayerManager.playlist = playlist
        jcPlayerManager.jcPlayerManagerListener = this

        jcPlayerManager.playAudio(audio)
        jcPlayerManager.createNewNotification(R.mipmap.ic_launcher)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_homepage)

        showHomePageContent()
    }

    private fun showHomePageContent() {
        linearHome.setOnClickListener {
            if (tabShown != ActiveTab.HOME) {
                tabShown = ActiveTab.HOME
                supportFragmentManager.beginTransaction()
                    .add(R.id.container, HomeFragment.newInstance())
                    .commitAllowingStateLoss()
                tvTitle.text = getString(R.string.app_name)
                validateTabColorVisibility(tabShown)
            }
        }

        linearMenu.setOnClickListener {
            if (tabShown != ActiveTab.MENU) {
                tabShown = ActiveTab.MENU
                supportFragmentManager.beginTransaction()
                    .add(R.id.container, MenuFragment.newInstance())
                    .commitAllowingStateLoss()
                tvTitle.text = getString(R.string.menu)
                validateTabColorVisibility(tabShown)
            }
        }

        linearMyBooks.setOnClickListener {
            if (tabShown != ActiveTab.MYBOOKS) {
                tabShown = ActiveTab.MYBOOKS
                supportFragmentManager.beginTransaction()
                    .add(R.id.container, MyBooksFragment.newInstance())
                    .commitAllowingStateLoss()
                tvTitle.text = getString(R.string.menu_my_books)
                validateTabColorVisibility(tabShown)
            }
        }

        linearLibrary.setOnClickListener {
            if (tabShown != ActiveTab.LIBRARY) {
                tabShown = ActiveTab.LIBRARY
                supportFragmentManager.beginTransaction()
                    .add(R.id.container, LibraryFragment.newInstance())
                    .commitAllowingStateLoss()
                tvTitle.text = getString(R.string.library)
                validateTabColorVisibility(tabShown)
            }
        }

        ivCart.setOnClickListener {
            val authResponse = viewModel.getAuthResponse()
            if (authResponse != null) {
                val intent = Intent(this@HomepageActivity, CartActivity::class.java)
                startActivity(intent)
            } else {
                Snackbar.make(
                    findViewById(android.R.id.content), "You have to be logged in First !.",
                    Snackbar.LENGTH_LONG
                ).show()
                Handler().postDelayed({
                    val mainIntent = Intent(this@HomepageActivity, LoginActivity::class.java)
                    startActivity(mainIntent)
                }, 700)
            }
        }

        supportFragmentManager.beginTransaction().add(R.id.container, HomeFragment.newInstance())
            .commitAllowingStateLoss()
        tvTitle.text = getString(R.string.app_name)
        validateTabColorVisibility(tabShown)
    }

    private fun validateTabColorVisibility(tabShown: ActiveTab) {
        when (tabShown) {
            ActiveTab.HOME -> {
                ivHome.setImageResource(R.mipmap.tab_home)
                tvHome.setTextColor(ContextCompat.getColor(applicationContext,R.color.white))
                ivMenu.setImageResource(R.mipmap.tab_menu_inactive)
                tvMenu.setTextColor(ContextCompat.getColor(applicationContext,R.color.grey))
                ivMyBooks.setImageResource(R.mipmap.tab_mybooks_inactive)
                tvMyBooks.setTextColor(ContextCompat.getColor(applicationContext,R.color.grey))
                ivLibrary.setImageResource(R.mipmap.tab_library_inactive)
                tvLibrary.setTextColor(ContextCompat.getColor(applicationContext,R.color.grey))
                viewModel.setActiveTab(ActiveTab.HOME)
            }

            ActiveTab.LIBRARY -> {
                ivHome.setImageResource(R.mipmap.tab_home_inactive)
                tvHome.setTextColor(ContextCompat.getColor(applicationContext,R.color.grey))
                ivMenu.setImageResource(R.mipmap.tab_menu_inactive)
                tvMenu.setTextColor(ContextCompat.getColor(applicationContext,R.color.grey))
                ivMyBooks.setImageResource(R.mipmap.tab_mybooks_inactive)
                tvMyBooks.setTextColor(ContextCompat.getColor(applicationContext,R.color.grey))
                ivLibrary.setImageResource(R.mipmap.tab_library)
                tvLibrary.setTextColor(ContextCompat.getColor(applicationContext,R.color.white))
                viewModel.setActiveTab(ActiveTab.LIBRARY)
            }

            ActiveTab.MYBOOKS -> {
                ivHome.setImageResource(R.mipmap.tab_home_inactive)
                tvHome.setTextColor(ContextCompat.getColor(applicationContext,R.color.grey))
                ivMenu.setImageResource(R.mipmap.tab_menu_inactive)
                tvMenu.setTextColor(ContextCompat.getColor(applicationContext,R.color.grey))
                ivMyBooks.setImageResource(R.mipmap.tab_mybooks)
                tvMyBooks.setTextColor(ContextCompat.getColor(applicationContext,R.color.white))
                ivLibrary.setImageResource(R.mipmap.tab_library_inactive)
                tvLibrary.setTextColor(ContextCompat.getColor(applicationContext,R.color.grey))
                viewModel.setActiveTab(ActiveTab.MYBOOKS)
            }

            ActiveTab.MENU -> {
                ivHome.setImageResource(R.mipmap.tab_home_inactive)
                tvHome.setTextColor(ContextCompat.getColor(applicationContext,R.color.grey))
                ivMenu.setImageResource(R.mipmap.tab_menu)
                tvMenu.setTextColor(ContextCompat.getColor(applicationContext,R.color.white))
                ivMyBooks.setImageResource(R.mipmap.tab_mybooks_inactive)
                tvMyBooks.setTextColor(ContextCompat.getColor(applicationContext,R.color.grey))
                ivLibrary.setImageResource(R.mipmap.tab_library_inactive)
                tvLibrary.setTextColor(ContextCompat.getColor(applicationContext,R.color.grey))
                viewModel.setActiveTab(ActiveTab.MENU)
            }
        }
    }

    override fun onResume() {
        tvCartCount.visibility = View.GONE
        viewModel.getCartCountObserver().observe(this, Observer {
            tvCartCount.visibility = View.VISIBLE
            tvCartCount.text = it.toString()
        })
        super.onResume()
    }

    private val jcPlayerManager: JcPlayerManager by lazy {
        JcPlayerManager.getInstance(applicationContext).get()!!
    }

    var tabShown = ActiveTab.HOME
    val viewModel: HomepageViewModel by viewModel()
}