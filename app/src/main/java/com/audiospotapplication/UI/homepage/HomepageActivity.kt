package com.audiospotapplication.UI.homepage

import android.app.PendingIntent
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import com.audiospotapplication.DataLayer.DataRepository
import com.audiospotapplication.DataLayer.Model.BookListResponse
import com.audiospotapplication.DataLayer.Retrofit.RetrofitCallbacks
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
import com.visionvalley.letuno.DataLayer.RepositorySource
import dm.audiostreamer.AudioStreamingManager
import dm.audiostreamer.MediaMetaData
import kotlinx.android.synthetic.main.activity_homepage3.*
import kotlinx.android.synthetic.main.header.*
import retrofit2.Call

class HomepageActivity : AppCompatActivity(), HomeFragment.onItemPlayClickListener,
    MyBooksFragment.onItemPlayClickListener, JcPlayerManagerListener {
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

    private var currentSong: MediaMetaData? = null
    private var listOfSongs: MutableList<MediaMetaData> = ArrayList()

    private val jcPlayerManager: JcPlayerManager by lazy {
        JcPlayerManager.getInstance(applicationContext).get()!!
    }

    override fun OnItemPlayed(mediaData: MediaMetaData) {
//        listOfSongs = ArrayList()
//        listOfSongs.add(mediaData)
//        configAudioStreamer()
//        playSong(mediaData)

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


    }

    private fun checkAlreadyPlaying() {
        if (streamingManager!!.isPlaying) {
            currentSong = streamingManager!!.currentAudio
            if (currentSong != null) {
                currentSong!!.playState = streamingManager!!.mLastPlaybackState
            }
        }
    }

    var tabShown = ActiveTab.HOME
    lateinit var mRepositorySource: RepositorySource

    private var streamingManager: AudioStreamingManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_homepage3)

        mRepositorySource = DataRepository.getInstance(applicationContext)

        configAudioStreamer()

        checkAlreadyPlaying()

        var authResponse = mRepositorySource.getAuthResponse()

        if (authResponse != null) {
            DialogUtils.showProgressDialog(this, "Loading ...")
            mRepositorySource.getMyBooks(object : RetrofitCallbacks.BookListCallback {
                override fun onSuccess(result: BookListResponse?) {
                    DialogUtils.dismissProgressDialog()
                    showHomePageContent()
                }

                override fun onFailure(call: Call<BookListResponse>?, t: Throwable?) {
                    DialogUtils.dismissProgressDialog()
                    Log.d("", "")
                }
            })

            mRepositorySource.getMyCart(object : RetrofitCallbacks.BookListCallback {
                override fun onSuccess(result: BookListResponse?) {
                    tvCartCount.text = result!!.data.size.toString()
                }

                override fun onFailure(call: Call<BookListResponse>?, t: Throwable?) {
                    Log.d("", "")
                }
            })
        } else {
            showHomePageContent()
        }
    }

    private fun configAudioStreamer() {
        streamingManager = AudioStreamingManager.getInstance(applicationContext)
        streamingManager!!.isPlayMultiple = mRepositorySource.getAuthResponse() != null
        streamingManager!!.setMediaList(listOfSongs)
        streamingManager!!.setShowPlayerNotification(true)
        streamingManager!!.setPendingIntentAct(getNotificationPendingIntent())
    }

    private fun showHomePageContent() {
        linearHome.setOnClickListener {
            if (tabShown != ActiveTab.HOME) {
                tabShown = ActiveTab.HOME
                supportFragmentManager.beginTransaction().add(R.id.container, HomeFragment.newInstance())
                    .commitAllowingStateLoss()

                validateTabColorVisibility(tabShown)
            }
        }

        linearMenu.setOnClickListener {
            if (tabShown != ActiveTab.MENU) {
                tabShown = ActiveTab.MENU
                supportFragmentManager.beginTransaction().add(R.id.container, MenuFragment.newInstance())
                    .commitAllowingStateLoss()
                validateTabColorVisibility(tabShown)
            }
        }

        linearMyBooks.setOnClickListener {
            if (tabShown != ActiveTab.MYBOOKS) {
                tabShown = ActiveTab.MYBOOKS
                supportFragmentManager.beginTransaction().add(R.id.container, MyBooksFragment.newInstance())
                    .commitAllowingStateLoss()

                validateTabColorVisibility(tabShown)
            }
        }

        linearLibrary.setOnClickListener {
            if (tabShown != ActiveTab.LIBRARY) {
                tabShown = ActiveTab.LIBRARY
                supportFragmentManager.beginTransaction().add(R.id.container, LibraryFragment.newInstance())
                    .commitAllowingStateLoss()
                validateTabColorVisibility(tabShown)
            }
        }

        ivCart.setOnClickListener {
            var authResponse = mRepositorySource.getAuthResponse()
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
                }, 500)
            }
        }

        supportFragmentManager.beginTransaction().add(R.id.container, HomeFragment.newInstance())
            .commitAllowingStateLoss()
        validateTabColorVisibility(tabShown)
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
                mRepositorySource.setActiveTab(ActiveTab.HOME)
                tvTitle.text = "AudioSpot"
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
                mRepositorySource.setActiveTab(ActiveTab.LIBRARY)
                tvTitle.text = "AudioSpot"
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
                mRepositorySource.setActiveTab(ActiveTab.MYBOOKS)
                tvTitle.text = "AudioSpot"
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
                mRepositorySource.setActiveTab(ActiveTab.MENU)
                tvTitle.text = "AudioSpot"
            }
        }
    }

    private fun getNotificationPendingIntent(): PendingIntent {
        val intent = Intent(applicationContext, HomepageActivity::class.java)
        intent.setAction("openplayer")
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        return PendingIntent.getActivity(applicationContext, 0, intent, 0)
    }
}