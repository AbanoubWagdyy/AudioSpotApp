package com.audiospotapplication.UI

import android.app.PendingIntent
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
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
import com.audiospotapplication.UI.promoCodeCongratulations.CongratulationsActivity
import com.audiospotapplication.UI.rateBook.RateBookActivity
import com.audiospotapplication.UI.splash.SplashActivity
import com.audiospotapplication.UI.voucher.VoucherActivity
import com.visionvalley.letuno.DataLayer.RepositorySource
import dm.audiostreamer.AudioStreamingManager
import dm.audiostreamer.MediaMetaData
import kotlinx.android.synthetic.main.activity_base.*
import kotlinx.android.synthetic.main.back_header.*
import kotlinx.android.synthetic.main.header.*
import retrofit2.Call

abstract class BaseActivity : AppCompatActivity(),
    MyBooksFragment.onItemPlayClickListener,
    HomeFragment.onItemPlayClickListener {

    lateinit var ivArrow: ImageView
    lateinit var tabShown: ActiveTab
    lateinit var mRepositorySource: RepositorySource
    private var streamingManager: AudioStreamingManager? = null
    private var listOfSongs: MutableList<MediaMetaData> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_base)

        mRepositorySource = DataRepository.getInstance(applicationContext)
        tabShown = mRepositorySource.getActiveTab()

        configAudioStreamer()

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

        validateTabColorVisibility(mRepositorySource.getActiveTab())

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
                    ivArrow.visibility = View.GONE
                } else {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.container, getFragment(ivArrow)).commitAllowingStateLoss()
                    ivArrow.visibility = getArrowHeaderVisibility()
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
                } else {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.container, getFragment(ivArrow)).commitAllowingStateLoss()
                    ivArrow.visibility = getArrowHeaderVisibility()
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
                } else {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.container, getFragment(ivArrow)).commitAllowingStateLoss()
                    ivArrow.visibility = getArrowHeaderVisibility()
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
                } else {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.container, getFragment(ivArrow)).commitAllowingStateLoss()
                    ivArrow.visibility = getArrowHeaderVisibility()
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

    private fun configAudioStreamer() {
        streamingManager = AudioStreamingManager.getInstance(applicationContext)
        //Set PlayMultiple 'true' if want to playing sequentially one by one songs
        // and provide the list of songs else set it 'false'
        streamingManager!!.isPlayMultiple = mRepositorySource.getAuthResponse() != null

        streamingManager!!.setMediaList(listOfSongs)

        //If you want to show the Player Notification then set ShowPlayerNotification as true
        //and provide the pending intent so that after click on notification it will redirect to an activity

        streamingManager!!.setShowPlayerNotification(true)
        streamingManager!!.setPendingIntentAct(getNotificationPendingIntent())
    }

    private fun getNotificationPendingIntent(): PendingIntent {
        val intent = Intent(applicationContext, SplashActivity::class.java)
        intent.setAction("openplayer")
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        return PendingIntent.getActivity(applicationContext, 0, intent, 0)
    }


    protected fun playSong(media: MediaMetaData) {
        if (streamingManager != null) {
            streamingManager!!.onPlay(media)
        }
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
        listOfSongs = ArrayList()
        listOfSongs.add(mediaData)
        configAudioStreamer()
        playSong(mediaData)
    }

    override fun onStop() {
        mRepositorySource.setActiveTab(tabShown)
        super.onStop()
    }

    abstract fun getHeaderTitle(): String
    abstract fun getArrowHeaderVisibility(): Int
    abstract fun getFragment(ivArrow: ImageView): Fragment
}