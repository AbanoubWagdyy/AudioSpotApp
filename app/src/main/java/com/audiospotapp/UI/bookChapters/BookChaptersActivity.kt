package com.audiospotapp.UI.bookChapters

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.media.session.PlaybackStateCompat
import android.text.format.DateUtils
import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import com.audiospotapp.DataLayer.Model.Bookmark
import com.audiospotapp.DataLayer.Model.ChaptersData
import com.audiospotapp.DataLayer.Model.Paragraph
import com.audiospotapp.R
import com.audiospotapp.UI.addBookmark.AddBookmarkActivity
import com.audiospotapp.UI.bookChapters.Interface.OnChapterCLickListener
import com.audiospotapp.UI.bookChapters.adapter.ChaptersAdapter
import com.audiospotapp.UI.splash.SplashActivity
import com.audiospotapp.utils.BookMediaDataConversion
import com.audiospotapp.utils.DialogUtils
import com.audiospotapp.utils.ImageUtils
import com.audiospotapp.utils.TimeUtils
import com.google.android.material.snackbar.Snackbar
import com.sothree.slidinguppanel.SlidingUpPanelLayout
import dm.audiostreamer.AudioStreamingManager
import dm.audiostreamer.CurrentSessionCallback
import dm.audiostreamer.Logger
import dm.audiostreamer.MediaMetaData
import dm.audiostreamerdemo.widgets.Slider
import kotlinx.android.synthetic.main.activity_chapters.*
import kotlinx.android.synthetic.main.activity_chapters_content.*
import kotlinx.android.synthetic.main.include_slidepanelchildtwo_bottomview.*
import kotlinx.android.synthetic.main.include_slidepanelchildtwo_bottomview.btn_play
import kotlinx.android.synthetic.main.include_slidepanelchildtwo_topviewtwo.*
import kotlinx.android.synthetic.main.include_slidingpanel_childtwo.*
import me.rohanpeshkar.filterablelistdialog.FilterableListDialog
import java.util.ArrayList

class BookChaptersActivity : AppCompatActivity(), CurrentSessionCallback, View.OnClickListener,
    Slider.OnValueChangedListener, BookChaptersContract.View, OnChapterCLickListener {

    override fun setBookmark(bookmark: Bookmark) {
        this.currentBookmark = bookmark
    }

    override fun showAddBookmarkScreen() {
        val intent = Intent(this, AddBookmarkActivity::class.java)
        startActivityForResult(intent, 1)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chapters)
        configAudioStreamer()
        uiInitialization()
        checkAlreadyPlaying()
        mPresenter = BookChaptersPresenter(this)
        mPresenter.start()
    }

    private fun checkAlreadyPlaying() {
        if (streamingManager.isPlaying) {
            currentSong = streamingManager.currentAudio
            if (currentSong != null) {
                currentSong!!.playState = streamingManager.mLastPlaybackState
                showMediaInfo(currentSong!!)
                btn_play.setBackgroundResource(R.drawable.play_bottom)
                btn_play_bottom.setBackgroundResource(R.drawable.play_bottom)
            }
        }
    }

    private fun uiInitialization() {
        btn_backward.setOnClickListener(this)
        btn_forward.setOnClickListener(this)
        btn_play.setOnClickListener(this)
        btn_play_bottom.setOnClickListener(this)
        ivParagraphs.setOnClickListener(this)
        back.setOnClickListener(this)
        tvClose.setOnClickListener(this)
        share.setOnClickListener(this)
        bookmark.setOnClickListener(this)
        download.setOnClickListener(this)
        timer.setOnClickListener(this)
        speed.setOnClickListener(this)

        changeButtonColor(btn_backward)
        changeButtonColor(btn_forward)

        slideBottomView.visibility = View.VISIBLE
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
                } else if (slideOffset > 0.0f && slideOffset < 1.0f) {
                    //slideBottomView.getBackground().setAlpha((int) slideOffset * 255);
                } else {
                    isExpand = true
                    slideBottomView.visibility = View.GONE
                }
            }

            override fun onPanelStateChanged(
                view: View,
                panelState: SlidingUpPanelLayout.PanelState,
                panelState1: SlidingUpPanelLayout.PanelState
            ) {
                isExpand = when (panelState) {
                    SlidingUpPanelLayout.PanelState.EXPANDED -> true
                    SlidingUpPanelLayout.PanelState.COLLAPSED -> false
                    else -> false
                }
            }
        })
    }

    private fun configAudioStreamer() {
        streamingManager = AudioStreamingManager.getInstance(applicationContext)
        //Set PlayMultiple 'true' if want to playing sequentially one by one songs
        // and provide the list of songs else set it 'false'
        streamingManager.isPlayMultiple = true
        streamingManager.setMediaList(listOfSongs)
        //If you want to show the Player Notification then set ShowPlayerNotification as true
        //and provide the pending intent so that after click on notification it will redirect to an activity
        streamingManager.setShowPlayerNotification(true)
        streamingManager.setPendingIntentAct(getNotificationPendingIntent())
    }

    //book details

    override fun getAppContext(): Context? {
        return applicationContext
    }

    override fun showLoadingDialog() {
        DialogUtils.showProgressDialog(this, "Loading ...")
    }

    override fun dismissLoading() {
        DialogUtils.dismissProgressDialog()
    }

    override fun setBookName(title: String) {
        bookName.text = title
    }

    override fun setBookImage(cover: String) {
        ImageUtils.setImageFromUrlIntoImageViewUsingPicasso(cover, applicationContext, image_songAlbumArt, false)
        ImageUtils.setImageFromUrlIntoImageViewUsingPicasso(cover, applicationContext, bookCover, false)
    }

    override fun onChapterClicked(data: ChaptersData) {
        configAudioStreamer()
        slidepanel_time_total.text = data.duration_str
        paragraphs = data.paragraphs
        chapterData = data
        var mediaData = BookMediaDataConversion.convertBookToMediaMetaData(data)
        playSong(mediaData)
    }

    override fun setChapters(data: List<ChaptersData>) {
        if (data.isNotEmpty()) {
            recyclerChapters.layoutManager = LinearLayoutManager(applicationContext)
            recyclerChapters.setHasFixedSize(true)
            recyclerChapters.isNestedScrollingEnabled = false
            recyclerChapters.adapter = ChaptersAdapter(data!!, this)
        } else {
            Snackbar.make(
                findViewById(android.R.id.content), "Chapters not Found",
                Snackbar.LENGTH_SHORT
            ).show()
        }
    }

    override fun onValueChanged(value: Int) {
        Log.d("SliderValue", value.toLong().toString())
        streamingManager.onSeekTo(value.toLong())
        streamingManager.scheduleSeekBarUpdate()
    }

    override fun updatePlaybackState(state: Int) {
        Logger.e("updatePlaybackState: ", "" + state)
        when (state) {
            PlaybackStateCompat.STATE_PLAYING -> {
                btn_play.setBackgroundResource(R.drawable.play_bottom)
                btn_play_bottom.setBackgroundResource(R.drawable.play_bottom)
                if (currentSong != null) {
                    currentSong!!.playState = PlaybackStateCompat.STATE_PLAYING
                }
            }
            PlaybackStateCompat.STATE_PAUSED -> {
                btn_play.setBackgroundResource(R.drawable.pause_bottom)
                btn_play_bottom.setBackgroundResource(R.drawable.pause_bottom)
                if (currentSong != null) {
                    currentSong!!.playState = PlaybackStateCompat.STATE_PAUSED
                }
            }
            PlaybackStateCompat.STATE_NONE -> {
                currentSong!!.playState = PlaybackStateCompat.STATE_NONE

            }
            PlaybackStateCompat.STATE_STOPPED -> {
                btn_play.setBackgroundResource(R.drawable.pause_bottom)
                btn_play_bottom.setBackgroundResource(R.drawable.pause_bottom)
                audio_progress_control.value = 0
                if (currentSong != null) {
                    currentSong!!.playState = PlaybackStateCompat.STATE_NONE
                }
            }
            PlaybackStateCompat.STATE_BUFFERING -> {
                if (currentSong != null) {
                    currentSong!!.playState = PlaybackStateCompat.STATE_NONE
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
        if (progress == 0) {
            if (currentBookmark != null) {
                if (!isBookmarkSlided) {
                    audio_progress_control.value = currentBookmark!!.time * 1000
                    streamingManager.onSeekTo((currentBookmark!!.time * 1000).toLong())
                    streamingManager.scheduleSeekBarUpdate()
                    isBookmarkSlided = true
                } else {
                    audio_progress_control.value = progress
                }
            } else {
                audio_progress_control.value = progress
            }

        } else {
            audio_progress_control.value = progress
        }

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

    private fun changeButtonColor(imageView: ImageView?) {
        try {
            val color = Color.BLACK
            imageView!!.setColorFilter(color)
        } catch (e: Exception) {
            e.printStackTrace()
        }
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
            btn_play.setBackgroundResource(R.drawable.pause_bottom)
            btn_play_bottom.setBackgroundResource(R.drawable.pause_bottom)
        } else {
            streamingManager.onPlay(currentSong)
            btn_play.setBackgroundResource(R.drawable.play_bottom)
            btn_play_bottom.setBackgroundResource(R.drawable.play_bottom)
        }
    }

    private fun playSong(media: MediaMetaData) {
        include_sliding_panel_childtwo.visibility = View.VISIBLE
        if (streamingManager != null) {
            streamingManager.onPlay(media)
            showMediaInfo(media)
        }
    }

    private fun showMediaInfo(media: MediaMetaData) {
        currentSong = media
        audio_progress_control.min = 0
        audio_progress_control.max = Integer.valueOf(media.mediaDuration) * 1000
        setMaxTime()
        loadSongDetails(media)
    }

    @Synchronized
    private fun setPGTime(progress: Int) {
        try {
            timeString = "00.00"
            currentSong = streamingManager.currentAudio
            if (currentSong != null && progress.toLong() != java.lang.Long.parseLong(currentSong!!.mediaDuration)) {
                timeString = TimeUtils.toTimeFormat(progress / 1000)
//                timeString = DateUtils.formatElapsedTime((progress / 1000).toLong())
                Log.d("timeString", timeString)
                Log.d("MyProgress", (progress / 1000).toString())
            }

            slidepanel_time_progress.text = timeString

            for (paragraph in paragraphs) {
                if ((progress / 1000) in paragraph.from_time.toLong()..paragraph.to_time.toLong()) {
                    paragraphTitle.text = paragraph.title
                }
            }

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
        text_songName.text = metaData.mediaTitle
        var chapterId = metaData.mediaId
        text_chapterNumber.text = "#Chapter $chapterId"

    }

    override fun onClick(view: View?) {
        when (view!!.id) {
            R.id.btn_forward -> streamingManager.onSkipToNext()
            R.id.btn_backward -> streamingManager.onSkipToPrevious()
            R.id.back -> finish()
            R.id.btn_play -> if (currentSong != null) {
                playPauseEvent(view)
            }
            R.id.btn_play_bottom -> if (currentSong != null) {
                playPauseEvent(view)
            }
            R.id.tvClose -> {
                include_sliding_panel_childtwo.visibility = View.GONE
                streamingManager.handleStopRequest(null)
            }

            R.id.share -> {

            }

            R.id.bookmark -> {
                streamingManager.handlePauseRequest()
                mPresenter.handleBookmarkClicked(timeString, chapterData.id, chapterData.title)
            }

            R.id.download -> {

            }

            R.id.timer -> {

            }

            R.id.speed -> {

            }

            R.id.ivParagraphs -> {
                FilterableListDialog.create(
                    this,
                    getListItems()
                ) {
                    validateParagraphClicked(it)
                }.show()
            }
        }
    }

    private fun validateParagraphClicked(str: String) {
        var selectedParagraph = paragraphs.filter {
            str.equals(it.title)
        }[0]

        audio_progress_control.value = selectedParagraph!!.from_time.toInt() * 1000
        streamingManager.onSeekTo((selectedParagraph!!.from_time.toInt() * 1000).toLong())
        streamingManager.scheduleSeekBarUpdate()

        paragraphTitle.text = selectedParagraph.title
    }

    private fun getListItems(): ArrayList<String>? {
        var items = ArrayList<String>()
        for (paragraph in paragraphs) {
            items.add(paragraph.title)
        }
        return items
    }

    private lateinit var chapterData: ChaptersData
    private lateinit var timeString: String
    private var isExpand = false
    private lateinit var streamingManager: AudioStreamingManager
    private var currentSong: MediaMetaData? = null
    private var listOfSongs: List<MediaMetaData> = ArrayList()
    var currentBookmark: Bookmark? = null
    var isBookmarkSlided: Boolean = false
    lateinit var mPresenter: BookChaptersContract.Presenter
    lateinit var paragraphs: List<Paragraph>
}