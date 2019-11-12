package com.audiospotapplication.UI.bookChapters

import android.app.ProgressDialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaControllerCompat
import android.support.v4.media.session.PlaybackStateCompat
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.audiospotapplication.DataLayer.Model.Bookmark
import com.audiospotapplication.DataLayer.Model.ChaptersData
import com.audiospotapplication.DataLayer.Model.ChaptersResponse
import com.audiospotapplication.R
import com.audiospotapplication.UI.addBookmark.AddBookmarkActivity
import com.audiospotapplication.UI.bookChapters.Interface.OnChapterCLickListener
import com.audiospotapplication.UI.bookChapters.adapter.ChaptersAdapter
import com.audiospotapplication.UI.homepage.HomepageActivity
import com.audiospotapplication.UI.login.LoginActivity
import com.audiospotapplication.utils.Constants.*
import com.audiospotapplication.utils.DialogUtils
import com.audiospotapplication.utils.ImageUtils
import com.audiospotapplication.utils.TimeUtils
import com.audiospotapplication.utils.player.MyPreferenceManager
import com.audiospotapplication.utils.player.client.MediaBrowserHelper
import com.audiospotapplication.utils.player.client.MediaBrowserHelperCallback
import com.audiospotapplication.utils.player.services.MediaService
import com.google.android.material.snackbar.Snackbar
import com.sothree.slidinguppanel.SlidingUpPanelLayout
import kotlinx.android.synthetic.main.activity_chapters.*
import kotlinx.android.synthetic.main.activity_chapters_content.*
import kotlinx.android.synthetic.main.include_slidepanelchildtwo_topviewtwo.*
import kotlinx.android.synthetic.main.include_slidingpanel_childtwo.*
import java.util.ArrayList
import com.example.jean.jcplayer.model.JcAudio
import kotlinx.android.synthetic.main.header.*
import kotlinx.android.synthetic.main.include_slidepanelchildtwo_bottomview.*
import me.rohanpeshkar.filterablelistdialog.FilterableListDialog

class BookChaptersActivity : AppCompatActivity(), View.OnClickListener,
    BookChaptersContract.View, OnChapterCLickListener, MediaBrowserHelperCallback {

    override fun onMetadataChanged(metadata: MediaMetadataCompat?) {
        text_songName.setText(metadata?.getDescription()?.getTitle())
    }

    override fun onPlaybackStateChanged(state: PlaybackStateCompat?) {
        mIsPlaying = state != null && state.state == PlaybackStateCompat.STATE_PLAYING
        if (mIsPlaying) {
            btnPlay.setImageDrawable(getDrawable(R.drawable.ic_pause))
            btn_play_bottom.setImageResource(R.drawable.play_bottom)
        } else {
            btnPlay.setImageDrawable(getDrawable(R.drawable.ic_play))
            btn_play_bottom.setImageResource(R.drawable.pause_bottom)
        }
    }

    override fun onMediaControllerConnected(mediaController: MediaControllerCompat?) {
        seekBar.setMediaController(mediaController)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chapters)


        mMediaBrowserHelper = MediaBrowserHelper(this, MediaService::class.java)
        mMediaBrowserHelper!!.setMediaBrowserHelperCallback(this)

        uiInitialization()

        mMyPrefManager = MyPreferenceManager(this)

        mPresenter = BookChaptersPresenter(this)
        mPresenter.start(intent.extras)
    }

    private fun uiInitialization() {

        ivParagraphs.setOnClickListener(this)
        back.setOnClickListener(this)
        tvClose.setOnClickListener(this)
        ivParagraphs.setOnClickListener(this)
        ivChapters.setOnClickListener(this)

        tvTitle.text = applicationContext.getString(R.string.chapters)

        slideBottomView.visibility = View.VISIBLE
        slideBottomView.setOnClickListener(View.OnClickListener {
            sliding_layout.panelState = SlidingUpPanelLayout.PanelState.EXPANDED
        })

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
        downloadProgressDialog = ProgressDialog(this)
        downloadProgressDialog!!.setMessage("Downloading(0 %) ....")
        downloadProgressDialog!!.setCancelable(false)

        btnPlay.setOnClickListener {
            if (mIsPlaying) {
                mMediaBrowserHelper?.transportControls!!.pause()
            } else {
                mMediaBrowserHelper?.transportControls!!.play()
            }
        }

        btn_play_bottom.setOnClickListener {
            if (mIsPlaying) {
                mMediaBrowserHelper!!.transportControls!!.pause()
            } else {
                mMediaBrowserHelper!!.transportControls!!.play()
            }
        }
    }

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
        ImageUtils.setImageFromUrlIntoImageViewUsingGlide(
            cover,
            applicationContext,
            image_songAlbumArt,
            false
        )
        ImageUtils.setImageFromUrlIntoImageViewUsingGlide(
            cover,
            applicationContext,
            bookCover,
            false
        )
    }

    override fun playAllChapters(result: ChaptersResponse) {
        if (mPresenter.isBookMine()) {
            chapterData = result.data[0]
            val jcAudios = ArrayList<JcAudio>()
            for (data in result.data) {
                var isDownloadedPath = mPresenter.validateChapterDownloaded(data)
                if (!isDownloadedPath.equals("")) {
                    jcAudios.add(
                        JcAudio.createFromFilePath(
                            data.id,
                            data.title,
                            isDownloadedPath,
                            data.paragraphs
                        )
                    )
                } else {
                    jcAudios.add(
                        JcAudio.createFromURL(
                            data.id,
                            data.title,
                            data.sound_file,
                            data.paragraphs
                        )
                    )
                }
            }

//            player.initPlaylist(jcAudios, this, this)
//            player.playAudio(player.myPlaylist!![0])
//            player.createNotification(R.mipmap.ic_launcher)
            sliding_layout.panelState = SlidingUpPanelLayout.PanelState.EXPANDED
        }
    }

    override fun onChapterClicked(data: ChaptersData) {
        if (mPresenter.isBookMine()) {
            chapterData = data
            text_songName.text = chapterData.title
            val isDownloadedPath = mPresenter.validateChapterDownloaded(data)
            val jcAudios = ArrayList<JcAudio>()
            if (!isDownloadedPath.equals("")) {
                jcAudios.add(
                    JcAudio.createFromFilePath(
                        data.id,
                        data.title,
                        isDownloadedPath,
                        data.paragraphs
                    )
                )
            } else {
                mMediaBrowserHelper!!.onStart(mWasConfigurationChange)
                onMediaSelected(mPresenter.getSavedBookId(), data)
            }
            sliding_layout.panelState = SlidingUpPanelLayout.PanelState.EXPANDED
//            if (currentBookmark != null) {
//                player.setBookmarkTime(currentBookmark!!.time)
//            }
        } else {
            Snackbar.make(
                findViewById(android.R.id.content),
                applicationContext.getString(R.string.you_should_own), Snackbar.LENGTH_SHORT
            ).show()
        }
    }

    private fun onMediaSelected(playlistId: String, item: ChaptersData) {
        val mediaItem = mPresenter.getMediaItem(item)
        if (mediaItem != null) {
            val currentPlaylistId = mMyPrefManager!!.playlistId
            val bundle = Bundle()
            bundle.putInt(MEDIA_QUEUE_POSITION, 0)
            if (playlistId.equals(currentPlaylistId)) {
                mMediaBrowserHelper!!.transportControls
                    .playFromMediaId(mediaItem.description.mediaId, bundle)
            } else {
                bundle.putBoolean(
                    QUEUE_NEW_PLAYLIST,
                    true
                ) // let the player know this is a new playlist
                mMediaBrowserHelper!!.subscribeToNewPlaylist(currentPlaylistId, playlistId)
                mMediaBrowserHelper!!.getTransportControls()
                    .playFromMediaId(mediaItem.description.mediaId, bundle)
            }
            mOnAppOpen = true

        } else {
            Toast.makeText(this, "select something to play", Toast.LENGTH_SHORT).show()
        }
    }

    override fun setChapters(data: List<ChaptersData>) {
        if (data.isNotEmpty()) {
            recyclerChapters.layoutManager = LinearLayoutManager(applicationContext)
            recyclerChapters.setHasFixedSize(true)
            recyclerChapters.isNestedScrollingEnabled = false
            recyclerChapters.adapter = ChaptersAdapter(data!!, this)
            if (mPresenter.isBookMine()) {
//                val jcAudios = ArrayList<JcAudio>()
//                for (chapter in data) {
//                    jcAudios.add(
//                        JcAudio.createFromURL(
//                            chapter.id,
//                            chapter.title,
//                            chapter.sound_file,
//                            chapter.paragraphs
//                        )
//                    )
//                }
//                player.initPlaylist(jcAudios, this, this)
            }
        } else {
            Snackbar.make(
                findViewById(android.R.id.content), "Chapters not Found",
                Snackbar.LENGTH_SHORT
            ).show()
        }
    }

    override fun onBackPressed() {
        if (isExpand) {
            sliding_layout.panelState = SlidingUpPanelLayout.PanelState.COLLAPSED
        } else {
            super.onBackPressed()
            overridePendingTransition(0, 0)
            finish()
        }
    }

    override fun showHomepageScreen() {
        val intent = Intent(this, HomepageActivity::class.java)
        intent.addFlags(
            Intent.FLAG_ACTIVITY_CLEAR_TOP or
                    Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        )
        startActivity(intent)
        finish()
    }

    override fun showLoginPage() {
        mPresenter.resetRepo()
        val intent = Intent(this, LoginActivity::class.java)
        intent.addFlags(
            Intent.FLAG_ACTIVITY_CLEAR_TOP or
                    Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        )
        startActivity(intent)
        finish()
    }

    override fun dismissDownloadingDialog() {
        downloadProgressDialog!!.dismiss()
    }

    override fun updateProgress(progress: Int) {
        runOnUiThread {
            downloadProgressDialog!!.setMessage("Downloading($progress %) ....")
        }
    }

    override fun showDownloadingDialog() {
        downloadProgressDialog!!.show()
    }

//    override fun onBookmarkClicked() {
//        var audio = player.currentAudio
//        mPresenter.handleBookmarkClicked(player.getCurrentTimeString(), audio!!.id, audio.title)
//    }
//
//    override fun onDownloadClicked() {
//        var audio = player.currentAudio
//        if (audio != null)
//            mPresenter.handleDownloadPressed(audio)
//    }
//
//    override fun onSpeedClicked() {
//
//    }
//
//    override fun onTimerClicked() {
//
//    }
//
//    override fun onPreparedAudio(status: JcStatus) {
//        btn_play_bottom.setBackgroundResource(R.drawable.play_bottom)
//    }
//
//    override fun onCompletedAudio() {
//    }
//
//    override fun onPaused(status: JcStatus) {
//        btn_play_bottom.setBackgroundResource(R.drawable.pause_bottom)
//    }
//
//    override fun onContinueAudio(status: JcStatus) {
//        btn_play_bottom.setBackgroundResource(R.drawable.play_bottom)
//    }
//
//    override fun onPlaying(status: JcStatus) {
//        btn_play_bottom.setBackgroundResource(R.drawable.play_bottom)
//    }
//
//    override fun onTimeChanged(status: JcStatus) {
//    }
//
//    override fun onStopped(status: JcStatus) {
//    }
//
//    override fun onJcpError(throwable: Throwable) {
//    }

    override fun onClick(view: View?) {
        when (view!!.id) {
            R.id.ivParagraphs -> {
                if (getListItems()!!.size > 0) {
                    FilterableListDialog.create(
                        this,
                        getListItems()
                    ) {
                        validateParagraphClicked(it)
                    }.show()
                } else {
                    Snackbar.make(
                        findViewById(android.R.id.content),
                        "No Paragraphs Found !.", Snackbar.LENGTH_SHORT
                    ).show()
                }
            }

            R.id.ivChapters -> {
                sliding_layout.panelState = SlidingUpPanelLayout.PanelState.COLLAPSED
            }

            R.id.tvClose -> {
//                player.kill()
                mMediaBrowserHelper?.transportControls?.stop()
                include_sliding_panel_childtwo.visibility = View.GONE
            }

            R.id.back -> {
                finish()
            }
        }
    }

    private fun validateParagraphClicked(str: String) {
        var selectedParagraph = chapterData.paragraphs.filter {
            str.equals(it.title)
        }[0]

//        player.seekToParagraph(selectedParagraph)
    }

    private fun getListItems(): ArrayList<String>? {
        var items = ArrayList<String>()
        for (paragraph in chapterData.paragraphs) {
            items.add(paragraph.title)
        }
        return items
    }

    override fun showMessage(s: String) {
        Snackbar.make(
            findViewById(android.R.id.content),
            s, Snackbar.LENGTH_SHORT
        ).show()
    }

    override fun showDownloadComplete(message: String, currentPath: String) {
        Snackbar.make(
            findViewById(android.R.id.content),
            message, Snackbar.LENGTH_SHORT
        ).show()
    }

    override fun setBookmark(bookmark: Bookmark) {
        this.currentBookmark = bookmark
//        player.stopCurrentPlayingAudio()
    }

    override fun showAddBookmarkScreen() {
        val intent = Intent(this, AddBookmarkActivity::class.java)
        startActivityForResult(intent, 1)
    }

    private inner class SeekBarBroadcastReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val seekProgress = intent.getLongExtra(SEEK_BAR_PROGRESS, 0)
            val seekMax = intent.getLongExtra(SEEK_BAR_MAX, 0)
            if (!seekBar.isTracking()) {
                seekBar.progress = seekProgress.toInt()
                seekBar.max = seekMax.toInt()
                txtCurrentDuration.text = TimeUtils.toTimeFormat(seekProgress.toInt())
                txtDuration.text = TimeUtils.toTimeFormat(seekMax.toInt())
            }
        }
    }

    override fun onStart() {
        super.onStart()
        mMediaBrowserHelper!!.onStart(mWasConfigurationChange)
    }

    override fun onStop() {
        super.onStop()
        mMediaBrowserHelper!!.onStop()
        seekBar.disconnectController()
    }

    override fun onResume() {
        super.onResume()
        initSeekBarBroadcastReceiver()
    }

    override fun onPause() {
        super.onPause()
        if (mSeekbarBroadcastReceiver != null) {
            unregisterReceiver(mSeekbarBroadcastReceiver)
        }
    }

    private fun initSeekBarBroadcastReceiver() {
        val intentFilter = IntentFilter()
        intentFilter.addAction(getString(R.string.broadcast_seekbar_update))
        mSeekbarBroadcastReceiver = SeekBarBroadcastReceiver()
        registerReceiver(mSeekbarBroadcastReceiver, intentFilter)
    }


    private lateinit var chapterData: ChaptersData

    private var isExpand = false

    var currentBookmark: Bookmark? = null

    lateinit var mPresenter: BookChaptersContract.Presenter

    private var downloadProgressDialog: ProgressDialog? = null

    private var mMediaBrowserHelper: MediaBrowserHelper? = null

    private var mSeekbarBroadcastReceiver: SeekBarBroadcastReceiver? = null

    private var mMyPrefManager: MyPreferenceManager? = null

    private var mIsPlaying: Boolean = false

    private var mOnAppOpen: Boolean = false

    private var mWasConfigurationChange = false
}