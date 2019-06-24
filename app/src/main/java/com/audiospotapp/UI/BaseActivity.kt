package com.audiospotapp.UI

import android.app.PendingIntent
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
import com.audiospotapp.UI.giftSelection.GiftSelectionActivity
import com.audiospotapp.UI.giveAgift.GiveGiftActivity
import com.audiospotapp.UI.profile.ProfileActivity
import com.audiospotapp.UI.promoCodeCongratulations.CongratulationsActivity
import com.audiospotapp.UI.rateBook.RateBookActivity
import com.audiospotapp.UI.splash.SplashActivity
import com.audiospotapp.UI.updateProfile.UpdateProfileActivity
import com.audiospotapp.UI.voucher.VoucherActivity
import com.visionvalley.letuno.DataLayer.RepositorySource
import dm.audiostreamer.AudioStreamingManager
import dm.audiostreamer.MediaMetaData
import kotlinx.android.synthetic.main.activity_base.*
import kotlinx.android.synthetic.main.back_header.*
import kotlinx.android.synthetic.main.header.*
import retrofit2.Call

abstract class BaseActivity : AppCompatActivity() {

    lateinit var ivArrow: ImageView
    lateinit var mRepositorySource: RepositorySource
    private var streamingManager: AudioStreamingManager? = null
    private var listOfSongs: MutableList<MediaMetaData> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_base)

        mRepositorySource = DataRepository.getInstance(applicationContext)

        configAudioStreamer()

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

        if (this is GiveGiftActivity || this is RateBookActivity ||
            this is GiftSelectionActivity ||
            this is VoucherActivity||
            this is CongratulationsActivity
        ) {
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

    abstract fun getHeaderTitle(): String
    abstract fun getArrowHeaderVisibility(): Int
    abstract fun getFragment(ivArrow: ImageView): Fragment
    abstract fun getActiveTab(): ActiveTab

    enum class ActiveTab {
        HOME, LIBRARY, MYBOOKS, MENU
    }
}