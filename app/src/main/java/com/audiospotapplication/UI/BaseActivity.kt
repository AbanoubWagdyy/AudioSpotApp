package com.audiospotapplication.UI

import android.app.PendingIntent
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.audiospotapplication.BaseView
import com.audiospotapplication.DataLayer.DataRepository
import com.audiospotapplication.DataLayer.Model.BookListResponse
import com.audiospotapplication.DataLayer.Retrofit.RetrofitCallbacks
import com.audiospotapplication.R
import com.audiospotapplication.UI.cart.CartActivity
import com.audiospotapplication.UI.giftSelection.GiftSelectionActivity
import com.audiospotapplication.UI.giveAgift.GiveGiftActivity
import com.audiospotapplication.UI.homepage.Library.LibraryFragment
import com.audiospotapplication.UI.homepage.home.HomeFragment
import com.audiospotapplication.UI.homepage.menu.MenuFragment
import com.audiospotapplication.UI.homepage.myBooks.MyBooksFragment
import com.audiospotapplication.UI.login.LoginActivity
import com.audiospotapplication.UI.promoCodeCongratulations.CongratulationsActivity
import com.audiospotapplication.UI.rateBook.RateBookActivity
import com.audiospotapplication.UI.splash.SplashActivity
import com.audiospotapplication.UI.voucher.VoucherActivity
import com.example.jean.jcplayer.JcPlayerManager
import com.example.jean.jcplayer.JcPlayerManagerListener
import com.example.jean.jcplayer.general.JcStatus
import com.example.jean.jcplayer.model.JcAudio
import com.visionvalley.letuno.DataLayer.RepositorySource
import dm.audiostreamer.MediaMetaData
import kotlinx.android.synthetic.main.audio_spot_activity_base.*
import kotlinx.android.synthetic.main.back_header.*
import kotlinx.android.synthetic.main.header.*
import retrofit2.Call

abstract class BaseActivity : AppCompatActivity(),
    MyBooksFragment.onItemPlayClickListener,
    HomeFragment.onItemPlayClickListener,
    BaseView,
    JcPlayerManagerListener {

    override fun showLoginPage() {
        mRepositorySource.reset()
        val intent = Intent(this, LoginActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or
                Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)
        finish()
    }

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

    }

    override fun onStopped(status: JcStatus) {

    }

    override fun onJcpError(throwable: Throwable) {

    }

    lateinit var ivArrow: ImageView
    lateinit var tabShown: ActiveTab
    lateinit var mRepositorySource: RepositorySource

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.audio_spot_activity_base)

        if (supportActionBar != null)
            supportActionBar!!.hide()

        mRepositorySource = DataRepository.getInstance(applicationContext)
        tabShown = mRepositorySource.getActiveTab()!!

        tvTitle.text = getHeaderTitle()
        ivArrow = findViewById(R.id.ivArrow)
        ivArrow.visibility = getArrowHeaderVisibility()

        ivBack.setOnClickListener {
            finish()
        }

        ivCart.setOnClickListener {
            val intent = Intent(applicationContext, CartActivity::class.java)
            startActivity(intent)
        }

        manageEditVisibility()

        validateTabColorVisibility(mRepositorySource.getActiveTab()!!)

        supportFragmentManager.beginTransaction()
            .replace(R.id.container, getFragment(ivArrow)).commitAllowingStateLoss()

        initListeners()
    }

    private fun initListeners() {
        linearHome.setOnClickListener {
            if (tabShown != ActiveTab.HOME) {
                tabShown = ActiveTab.HOME
                if (mRepositorySource.getActiveTab() != ActiveTab.HOME) {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.container, HomeFragment.newInstance()).commitAllowingStateLoss()
                    tvTitle.text = getString(R.string.app_name)
                    ivArrow.visibility = View.GONE
                } else {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.container, getFragment(ivArrow)).commitAllowingStateLoss()
                    ivArrow.visibility = getArrowHeaderVisibility()
                    tvTitle.text = getHeaderTitle()
                }
                validateTabColorVisibility(tabShown)
            }
        }

        linearLibrary.setOnClickListener {
            if (tabShown != ActiveTab.LIBRARY) {
                tabShown = ActiveTab.LIBRARY
                if (mRepositorySource.getActiveTab() != ActiveTab.LIBRARY) {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.container, LibraryFragment.newInstance()).commitAllowingStateLoss()
                    ivArrow.visibility = View.GONE
                    tvTitle.text = getString(R.string.library)
                } else {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.container, getFragment(ivArrow)).commitAllowingStateLoss()
                    ivArrow.visibility = getArrowHeaderVisibility()
                    tvTitle.text = getHeaderTitle()
                }
                validateTabColorVisibility(tabShown)
            }
        }

        linearMyBooks.setOnClickListener {
            if (tabShown != ActiveTab.MYBOOKS) {
                tabShown = ActiveTab.MYBOOKS
                if (mRepositorySource.getActiveTab() != ActiveTab.MYBOOKS) {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.container, MyBooksFragment.newInstance()).commitAllowingStateLoss()
                    ivArrow.visibility = View.GONE
                    tvTitle.text = getString(R.string.menu_my_books)
                } else {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.container, getFragment(ivArrow)).commitAllowingStateLoss()
                    ivArrow.visibility = getArrowHeaderVisibility()
                    tvTitle.text = getHeaderTitle()
                }
                validateTabColorVisibility(tabShown)
            }
        }

        linearMenu.setOnClickListener {
            if (tabShown != ActiveTab.MENU) {
                tabShown = ActiveTab.MENU
                if (mRepositorySource.getActiveTab() != ActiveTab.MENU) {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.container, MenuFragment.newInstance()).commitAllowingStateLoss()
                    ivArrow.visibility = View.GONE
                    tvTitle.text = getString(R.string.menu)
                } else {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.container, getFragment(ivArrow)).commitAllowingStateLoss()
                    ivArrow.visibility = getArrowHeaderVisibility()
                    tvTitle.text = getHeaderTitle()
                }
                validateTabColorVisibility(tabShown)
            }
        }
    }

    open fun manageEditVisibility() {
        ivEdit.visibility = View.GONE
        if (this is GiveGiftActivity || this is RateBookActivity ||
            this is GiftSelectionActivity ||
            this is VoucherActivity ||
            this is CongratulationsActivity
        ) {
            bottom_navigation.visibility = View.GONE
        }
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

    private fun getNotificationPendingIntent(): PendingIntent {
        val intent = Intent(applicationContext, SplashActivity::class.java)
        intent.setAction("openplayer")
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        return PendingIntent.getActivity(applicationContext, 0, intent, 0)
    }

    private fun validateTabColorVisibility(tabShown: ActiveTab) {
        when (tabShown) {
            ActiveTab.HOME -> {
                ivHome.setImageResource(R.mipmap.tab_home)
                tvHome.setTextColor(resources.getColor(R.color.white))
                ivMenu.setImageResource(R.mipmap.tab_menu_inactive)
                tvMenu.setTextColor(resources.getColor(R.color.grey))
                ivMyBooks.setImageResource(R.mipmap.tab_mybooks_inactive)
                tvMyBooks.setTextColor(resources.getColor(R.color.grey))
                ivLibrary.setImageResource(R.mipmap.tab_library_inactive)
                tvLibrary.setTextColor(resources.getColor(R.color.grey))
            }

            ActiveTab.LIBRARY -> {
                ivHome.setImageResource(R.mipmap.tab_home_inactive)
                tvHome.setTextColor(resources.getColor(R.color.grey))
                ivMenu.setImageResource(R.mipmap.tab_menu_inactive)
                tvMenu.setTextColor(resources.getColor(R.color.grey))
                ivMyBooks.setImageResource(R.mipmap.tab_mybooks_inactive)
                tvMyBooks.setTextColor(resources.getColor(R.color.grey))
                ivLibrary.setImageResource(R.mipmap.tab_library)
                tvLibrary.setTextColor(resources.getColor(R.color.white))
            }

            ActiveTab.MYBOOKS -> {
                ivHome.setImageResource(R.mipmap.tab_home_inactive)
                tvHome.setTextColor(resources.getColor(R.color.grey))
                ivMenu.setImageResource(R.mipmap.tab_menu_inactive)
                tvMenu.setTextColor(resources.getColor(R.color.grey))
                ivMyBooks.setImageResource(R.mipmap.tab_mybooks)
                tvMyBooks.setTextColor(resources.getColor(R.color.white))
                ivLibrary.setImageResource(R.mipmap.tab_library_inactive)
                tvLibrary.setTextColor(resources.getColor(R.color.grey))
            }

            ActiveTab.MENU -> {
                ivHome.setImageResource(R.mipmap.tab_home_inactive)
                tvHome.setTextColor(resources.getColor(R.color.grey))
                ivMenu.setImageResource(R.mipmap.tab_menu)
                tvMenu.setTextColor(resources.getColor(R.color.white))
                ivMyBooks.setImageResource(R.mipmap.tab_mybooks_inactive)
                tvMyBooks.setTextColor(resources.getColor(R.color.grey))
                ivLibrary.setImageResource(R.mipmap.tab_library_inactive)
                tvLibrary.setTextColor(resources.getColor(R.color.grey))
            }
        }
    }

    override fun OnItemPlayed(mediaData: MediaMetaData) {
//        listOfSongs = ArrayList()
//        listOfSongs.add(mediaData)
//        configAudioStreamer()
//        playSong(mediaData)

        if (jcPlayerManager.isPlaying()) {
            jcPlayerManager.pauseAudio()
            return
        }

        if (jcPlayerManager.currentAudio == null) {
            if (mediaData != null && mediaData.mediaUrl != null && !mediaData.mediaUrl.equals("")) {
                var audio = JcAudio.createFromURL(
                    mediaData.mediaId.toInt(), mediaData.mediaTitle,
                    mediaData.mediaUrl, null
                )
//
                var playlist = ArrayList<JcAudio>()
                playlist.add(audio)

                jcPlayerManager.playlist = playlist as ArrayList<JcAudio>
                jcPlayerManager.jcPlayerManagerListener = this

                jcPlayerManager.playAudio(audio)
                jcPlayerManager.createNewNotification(R.mipmap.ic_launcher)
            }
        } else {
            jcPlayerManager.continueAudio()
        }
    }

    override fun onStop() {
        mRepositorySource.setActiveTab(tabShown)
        super.onStop()
    }

    abstract fun getHeaderTitle(): String
    abstract fun getArrowHeaderVisibility(): Int
    abstract fun getFragment(ivArrow: ImageView): Fragment

    fun setCartNumber(size: Int?) {
        tvCartCount.text = "$size"
    }

    private val jcPlayerManager: JcPlayerManager by lazy {
        JcPlayerManager.getInstance(applicationContext).get()!!
    }
}