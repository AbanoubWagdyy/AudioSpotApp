package com.audiospotapp.UI.homepage

import android.app.PendingIntent
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.support.v4.media.session.PlaybackStateCompat
import android.text.format.DateUtils
import android.util.Log
import android.view.View
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import com.audiospotapp.DataLayer.DataRepository
import com.audiospotapp.DataLayer.Model.BookListResponse
import com.audiospotapp.DataLayer.Retrofit.RetrofitCallbacks
import com.audiospotapp.R
import com.audiospotapp.UI.cart.CartActivity
import com.audiospotapp.UI.homepage.home.HomeFragment
import com.audiospotapp.UI.homepage.Library.LibraryFragment
import com.audiospotapp.UI.homepage.menu.MenuFragment
import com.audiospotapp.UI.homepage.myBooks.MyBooksFragment
import com.audiospotapp.UI.login.LoginActivity
import com.audiospotapp.utils.DialogUtils
import com.google.android.material.snackbar.Snackbar
import com.visionvalley.letuno.DataLayer.RepositorySource
import dm.audiostreamer.AudioStreamingManager
import dm.audiostreamer.MediaMetaData
import dm.audiostreamerdemo.widgets.Slider
import kotlinx.android.synthetic.main.activity_homepage3.*
import kotlinx.android.synthetic.main.header.*
import retrofit2.Call

class HomepageActivity : AppCompatActivity(),
    Slider.OnValueChangedListener, HomeFragment.onItemPlayClickListener {

    private var listOfSongs: MutableList<MediaMetaData> = ArrayList()

    override fun OnItemPlayed(mediaData: MediaMetaData) {

        listOfSongs = ArrayList()
        listOfSongs.add(mediaData)

        configAudioStreamer()

        playSong(mediaData)
    }

    var tabShown = Tab.HOME
    lateinit var mRepositorySource: RepositorySource

    private var streamingManager: AudioStreamingManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_homepage3)

        mRepositorySource = DataRepository.getInstance(applicationContext)

        configAudioStreamer()

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
        //Set PlayMultiple 'true' if want to playing sequentially one by one songs
        // and provide the list of songs else set it 'false'
        streamingManager!!.isPlayMultiple = mRepositorySource.getAuthResponse() != null

        streamingManager!!.setMediaList(listOfSongs)

        //If you want to show the Player Notification then set ShowPlayerNotification as true
        //and provide the pending intent so that after click on notification it will redirect to an activity

        streamingManager!!.setShowPlayerNotification(true)
        streamingManager!!.setPendingIntentAct(getNotificationPendingIntent())
    }

    private fun showHomePageContent() {
        linearHome.setOnClickListener {
            if (tabShown != Tab.HOME) {
                tabShown = Tab.HOME
                supportFragmentManager.beginTransaction().add(R.id.container, HomeFragment.newInstance())
                    .commitAllowingStateLoss()

                validateTabColorVisibility(tabShown)
            }
        }

        linearMenu.setOnClickListener {
            if (tabShown != Tab.MENU) {
                tabShown = Tab.MENU
                supportFragmentManager.beginTransaction().add(R.id.container, MenuFragment.newInstance())
                    .commitAllowingStateLoss()
                validateTabColorVisibility(tabShown)
            }
        }

        linearMyBooks.setOnClickListener {
            if (tabShown != Tab.MYBOOKS) {
                tabShown = Tab.MYBOOKS
                supportFragmentManager.beginTransaction().add(R.id.container, MyBooksFragment.newInstance())
                    .commitAllowingStateLoss()

                validateTabColorVisibility(tabShown)
            }
        }

        linearLibrary.setOnClickListener {
            if (tabShown != Tab.LIBRARY) {
                tabShown = Tab.LIBRARY
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

    private fun validateTabColorVisibility(tabShown: Tab) {
        when (tabShown) {
            Tab.HOME -> {
                ivHome.setImageResource(R.mipmap.tab_home)
                tvHome.setTextColor(resources.getColor(R.color.white))
                ivMenu.setImageResource(R.mipmap.tab_menu_inactive)
                tvMenu.setTextColor(resources.getColor(R.color.grey))
                ivMyBooks.setImageResource(R.mipmap.tab_mybooks_inactive)
                tvMyBooks.setTextColor(resources.getColor(R.color.grey))
                ivLibrary.setImageResource(R.mipmap.tab_library_inactive)
                tvLibrary.setTextColor(resources.getColor(R.color.grey))
            }

            Tab.LIBRARY -> {
                ivHome.setImageResource(R.mipmap.tab_home_inactive)
                tvHome.setTextColor(resources.getColor(R.color.grey))
                ivMenu.setImageResource(R.mipmap.tab_menu_inactive)
                tvMenu.setTextColor(resources.getColor(R.color.grey))
                ivMyBooks.setImageResource(R.mipmap.tab_mybooks_inactive)
                tvMyBooks.setTextColor(resources.getColor(R.color.grey))
                ivLibrary.setImageResource(R.mipmap.tab_library)
                tvLibrary.setTextColor(resources.getColor(R.color.white))
            }

            Tab.MYBOOKS -> {
                ivHome.setImageResource(R.mipmap.tab_home_inactive)
                tvHome.setTextColor(resources.getColor(R.color.grey))
                ivMenu.setImageResource(R.mipmap.tab_menu_inactive)
                tvMenu.setTextColor(resources.getColor(R.color.grey))
                ivMyBooks.setImageResource(R.mipmap.tab_mybooks)
                tvMyBooks.setTextColor(resources.getColor(R.color.white))
                ivLibrary.setImageResource(R.mipmap.tab_library_inactive)
                tvLibrary.setTextColor(resources.getColor(R.color.grey))
            }

            Tab.MENU -> {
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

    private fun playSong(media: MediaMetaData) {
        if (streamingManager != null) {
            streamingManager!!.onPlay(media)
        }
    }

    override fun onValueChanged(value: Int) {
        streamingManager!!.onSeekTo(value.toLong())
        streamingManager!!.scheduleSeekBarUpdate()
    }

    private fun getNotificationPendingIntent(): PendingIntent {
        val intent = Intent(applicationContext, HomepageActivity::class.java)
        intent.setAction("openplayer")
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        return PendingIntent.getActivity(applicationContext, 0, intent, 0)
    }

    enum class Tab {
        HOME, LIBRARY, MYBOOKS, MENU
    }
}