package com.audiospotapp.UI.bookChapters

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.media.session.PlaybackStateCompat
import android.text.format.DateUtils
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.audiospotapp.DataLayer.Model.ChaptersData
import com.audiospotapp.R
import com.audiospotapp.UI.bookChapters.Interface.OnChapterCLickListener
import com.audiospotapp.UI.bookChapters.adapter.ChaptersAdapter
import com.audiospotapp.UI.splash.SplashActivity
import com.audiospotapp.utils.DialogUtils
import com.audiospotapp.utils.ImageUtils
import com.google.android.material.snackbar.Snackbar
import com.sothree.slidinguppanel.SlidingUpPanelLayout
import dm.audiostreamer.AudioStreamingManager
import dm.audiostreamer.CurrentSessionCallback
import dm.audiostreamer.Logger
import dm.audiostreamer.MediaMetaData
import dm.audiostreamerdemo.widgets.PlayPauseView
import dm.audiostreamerdemo.widgets.Slider
import kotlinx.android.synthetic.main.activity_chapters.*
import kotlinx.android.synthetic.main.activity_chapters_content.*
import kotlinx.android.synthetic.main.include_slidepanelchildtwo_bottomview.*
import kotlinx.android.synthetic.main.include_slidepanelchildtwo_bottomview.btn_play
import kotlinx.android.synthetic.main.include_slidepanelchildtwo_topviewtwo.*
import kotlinx.android.synthetic.main.include_slidingpanel_childtwo.*
import java.util.ArrayList

class BookChaptersActivity : AppCompatActivity(), CurrentSessionCallback, View.OnClickListener,
    Slider.OnValueChangedListener, BookChaptersContract.View, OnChapterCLickListener {

    override fun onChapterClicked(data: ChaptersData) {

    }

    override fun getAppContext(): Context? {
        return applicationContext
    }

    override fun showLoadingDialog() {
        DialogUtils.showProgressDialog(this, "Loading ....")
    }

    override fun dismissLoading() {
        DialogUtils.dismissProgressDialog()
    }

    override fun setChapters(data: List<ChaptersData>) {
        if (data.isNotEmpty()) {
            recyclerChapters.layoutManager = LinearLayoutManager(applicationContext)
            recyclerChapters.setHasFixedSize(true)
            recyclerChapters.isNestedScrollingEnabled = false
            recyclerChapters.adapter = ChaptersAdapter(data!!, this)
        } else {
            Snackbar.make(findViewById(android.R.id.content), "Chapters not Found", Snackbar.LENGTH_SHORT).show()
        }
    }

    override fun onValueChanged(value: Int) {
        streamingManager.onSeekTo(value.toLong())
        streamingManager.scheduleSeekBarUpdate()
    }

    override fun updatePlaybackState(state: Int) {
        Logger.e("updatePlaybackState: ", "" + state)
        when (state) {
            PlaybackStateCompat.STATE_PLAYING -> {
                pgPlayPauseLayout.visibility = View.INVISIBLE
                btn_play.Play()
                if (currentSong != null) {
                    currentSong!!.setPlayState(PlaybackStateCompat.STATE_PLAYING)

                }
            }
            PlaybackStateCompat.STATE_PAUSED -> {
                pgPlayPauseLayout.visibility = View.INVISIBLE
                btn_play.Pause()
                if (currentSong != null) {
                    currentSong!!.setPlayState(PlaybackStateCompat.STATE_PAUSED)
                }
            }
            PlaybackStateCompat.STATE_NONE -> {
                currentSong!!.setPlayState(PlaybackStateCompat.STATE_NONE)

            }
            PlaybackStateCompat.STATE_STOPPED -> {
                pgPlayPauseLayout.visibility = View.INVISIBLE
                btn_play.Pause()
                audio_progress_control.value = 0
                if (currentSong != null) {
                    currentSong!!.setPlayState(PlaybackStateCompat.STATE_NONE)
                }
            }
            PlaybackStateCompat.STATE_BUFFERING -> {
                pgPlayPauseLayout.visibility = View.VISIBLE
                if (currentSong != null) {
                    currentSong!!.setPlayState(PlaybackStateCompat.STATE_NONE)
                }
            }
        }
    }

    override fun playSongComplete() {
        val timeString = "00.00"
        slidepanel_time_total.text = timeString
        slidepanel_time_progress.text = timeString
        audio_progress_control.value = 0
    }

    override fun currentSeekBarPosition(progress: Int) {
        audio_progress_control.value = progress
        setPGTime(progress)
    }

    override fun playCurrent(indexP: Int, currentAudio: MediaMetaData?) {
        showMediaInfo(currentAudio!!)
    }

    override fun playNext(indexP: Int, currentAudio: MediaMetaData?) {
        showMediaInfo(currentAudio!!)
    }

    override fun playPrevious(indexP: Int, currentAudio: MediaMetaData?) {
        showMediaInfo(currentAudio!!)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chapters)

        configAudioStreamer()
        uiInitialization()
    }

    private fun uiInitialization() {
        btn_backward.setOnClickListener(this)
        btn_forward.setOnClickListener(this)
        btn_play.setOnClickListener(this)
        back.setOnClickListener(this)
        pgPlayPauseLayout.setOnClickListener(View.OnClickListener { return@OnClickListener })

        btn_play.Pause()

        changeButtonColor(btn_backward)
        changeButtonColor(btn_forward)

        slideBottomView.setVisibility(View.VISIBLE)
        slideBottomView.setOnClickListener(View.OnClickListener {
            sliding_layout.panelState = SlidingUpPanelLayout.PanelState.EXPANDED
        })

        audio_progress_control.max = 0
        audio_progress_control.onValueChangedListener = this

        sliding_layout.addPanelSlideListener(object : SlidingUpPanelLayout.PanelSlideListener {
            override fun onPanelSlide(view: View, slideOffset: Float) {
                if (slideOffset == 0.0f) {
                    isExpand = false
                    slideBottomView.visibility = View.VISIBLE
                    //slideBottomView.getBackground().setAlpha(0);
                } else if (slideOffset > 0.0f && slideOffset < 1.0f) {
                    //slideBottomView.getBackground().setAlpha((int) slideOffset * 255);
                } else {
                    //slideBottomView.getBackground().setAlpha(100);
                    isExpand = true
                    slideBottomView.visibility = View.GONE
                }
            }

            override fun onPanelStateChanged(
                view: View,
                panelState: SlidingUpPanelLayout.PanelState,
                panelState1: SlidingUpPanelLayout.PanelState
            ) {
                when (panelState) {
                    SlidingUpPanelLayout.PanelState.EXPANDED -> isExpand = true
                    SlidingUpPanelLayout.PanelState.COLLAPSED -> isExpand = false
                    else -> isExpand = false
                }
            }
        })

        mPresenter = BookChaptersPresenter(this)
        mPresenter.start()
    }

    private fun changeButtonColor(imageView: ImageView?) {
        try {
            val color = Color.BLACK
            imageView!!.setColorFilter(color)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun configAudioStreamer() {
        streamingManager = AudioStreamingManager.getInstance(applicationContext)
        //Set PlayMultiple 'true' if want to playing sequentially one by one songs
        // and provide the list of songs else set it 'false'
        streamingManager.setPlayMultiple(true)
        streamingManager.setMediaList(listOfSongs)
        //If you want to show the Player Notification then set ShowPlayerNotification as true
        //and provide the pending intent so that after click on notification it will redirect to an activity
        streamingManager.setShowPlayerNotification(true)
        streamingManager.setPendingIntentAct(getNotificationPendingIntent())
    }

    private fun getNotificationPendingIntent(): PendingIntent {
        val intent = Intent(applicationContext, SplashActivity::class.java)
        intent.setAction("openplayer")
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        return PendingIntent.getActivity(applicationContext, 0, intent, 0)
    }

    override fun onBackPressed() {
        if (isExpand) {
            sliding_layout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED)
        } else {
            super.onBackPressed()
            overridePendingTransition(0, 0)
            finish()
        }
    }


    public override fun onStart() {
        super.onStart()
        try {
            if (streamingManager != null) {
                streamingManager.subscribesCallBack(this)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    public override fun onStop() {
        try {
            if (streamingManager != null) {
                streamingManager.unSubscribeCallBack()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        super.onStop()
    }

    override fun onDestroy() {
        try {
            if (streamingManager != null) {
                streamingManager.unSubscribeCallBack()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        super.onDestroy()
    }

    private fun playPauseEvent(v: View) {
        if (streamingManager.isPlaying) {
            streamingManager.onPause()
            (v as PlayPauseView).Pause()
        } else {
            streamingManager.onPlay(currentSong)
            (v as PlayPauseView).Play()
        }
    }

    private fun playSong(media: MediaMetaData) {
        if (streamingManager != null) {
            streamingManager.onPlay(media)
            showMediaInfo(media)
        }
    }

    private fun showMediaInfo(media: MediaMetaData) {
        currentSong = media
        audio_progress_control.value = 0
        audio_progress_control.min = 0
        audio_progress_control.max = Integer.valueOf(media.mediaDuration) * 1000
        slidepanel_time_total.text = media.mediaDuration
        setPGTime(0)
        setMaxTime()
        loadSongDetails(media)
    }

    private fun setPGTime(progress: Int) {

        try {
            var timeString = "00.00"
            currentSong = streamingManager.currentAudio
            if (currentSong != null && progress.toLong() != java.lang.Long.parseLong(currentSong!!.getMediaDuration())) {
                timeString = DateUtils.formatElapsedTime((progress / 1000).toLong())
            }
            slidepanel_time_progress.text = timeString
        } catch (e: NumberFormatException) {
            e.printStackTrace()
        }
    }

    private fun setMaxTime() {
        try {
            val timeString = DateUtils.formatElapsedTime(java.lang.Long.parseLong(currentSong!!.getMediaDuration()))
            slidepanel_time_total.text = timeString
        } catch (e: NumberFormatException) {
            e.printStackTrace()
        }
    }

    private fun loadSongDetails(metaData: MediaMetaData) {
        text_songName.setText(metaData.mediaTitle)

        ImageUtils.setImageFromUrlIntoImageViewUsingPicasso(
            metaData.mediaUrl,
            applicationContext,
            image_songAlbumArt,
            false
        )

        ImageUtils.setImageFromUrlIntoImageViewUsingPicasso(
            metaData.mediaUrl,
            applicationContext,
            bookCover,
            false
        )
    }

    override fun onClick(view: View?) {
        when (view!!.getId()) {
            R.id.btn_forward -> streamingManager.onSkipToNext()
            R.id.btn_backward -> streamingManager.onSkipToPrevious()
            R.id.back -> finish()
            R.id.btn_play -> if (currentSong != null) {
                playPauseEvent(view)
            }
        }
    }

    private var isExpand = false
    private lateinit var streamingManager: AudioStreamingManager
    private var currentSong: MediaMetaData? = null
    private var listOfSongs: List<MediaMetaData> = ArrayList()

    lateinit var mPresenter: BookChaptersContract.Presenter

}
