package com.audiospotapplication.UI.bookChapters

import android.app.ProgressDialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.media.session.MediaControllerCompat
import android.support.v4.media.session.PlaybackStateCompat
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.audiospotapplication.DataLayer.Model.Bookmark
import com.audiospotapplication.DataLayer.Model.ChaptersData
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
import com.google.android.material.snackbar.Snackbar
import com.sothree.slidinguppanel.SlidingUpPanelLayout
import kotlinx.android.synthetic.main.activity_chapters.*
import kotlinx.android.synthetic.main.activity_chapters_content.*
import kotlinx.android.synthetic.main.include_slidepanelchildtwo_topviewtwo.*
import kotlinx.android.synthetic.main.include_slidingpanel_childtwo.*
import java.util.ArrayList
import com.ps.pexoplayer.PexoEventListener
import com.ps.pexoplayer.PexoPlayerManager
import com.ps.pexoplayer.model.PexoMediaMetadata
import kotlinx.android.synthetic.main.header.*
import kotlinx.android.synthetic.main.include_slidepanelchildtwo_bottomview.*
import me.rohanpeshkar.filterablelistdialog.FilterableListDialog

class BookChaptersActivity : AppCompatActivity(), View.OnClickListener,
    BookChaptersContract.View, OnChapterCLickListener, PexoEventListener {

    override fun onPlayerMediaControllerConnected(mediaController: MediaControllerCompat?) {
        seekBar.setMediaController(mediaController)
    }

    override fun onPlayerMetadataChanged(pexoMediaMetadata: PexoMediaMetadata?) {
        val chapter = mPresenter?.getBookByID(pexoMediaMetadata?.mediaId)
        if (chapter != null)
            text_songName.text = chapter.title
    }

    override fun onPlayerPlaybackStateChanged(state: PlaybackStateCompat?) {
        if (state != null) {
            when (state.state) {
                PlaybackStateCompat.STATE_BUFFERING -> {
                }
                PlaybackStateCompat.STATE_PLAYING -> {
                    btnPlay.setImageDrawable(getDrawable(R.drawable.ic_pause))
                    btn_play_bottom.setImageResource(R.drawable.play_bottom)
                }
                PlaybackStateCompat.STATE_PAUSED -> {
                    btnPlay.setImageDrawable(getDrawable(R.drawable.ic_play))
                    btn_play_bottom.setImageResource(R.drawable.pause_bottom)
                }
                PlaybackStateCompat.STATE_ERROR -> {
                }
            }
        }
    }

    override fun onControllerDisconnect() {

    }

    override fun updatePlayerBuffer(progress: Int) {

    }

    override fun updatePlayerSeekBar(progress: Int, max: Int) {

    }

    override fun updateShuffle(state: Int) {

    }

    override fun updateRepeat(state: Int) {
        when (state) {
            PlaybackStateCompat.SHUFFLE_MODE_ALL -> {
            }
            PlaybackStateCompat.SHUFFLE_MODE_NONE -> {
            }
        }
    }

    override fun prepareLastPlayedMedia() {
        pexoPlayerManager.onFinishedGettingPreviousSessionData(ArrayList<PexoMediaMetadata>())
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        Log.i(TAG, "onConfigurationChanged()")
        pexoPlayerManager.setConfigurationChanged(newConfig)
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.i(TAG, "onDestroy()")
        pexoPlayerManager.unSubscribeCallBack()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chapters)

        pexoPlayerManager = PexoPlayerManager(this)
        pexoPlayerManager.setPendingIntentClass(BookChaptersActivity::class.java)
        pexoPlayerManager.setPexoEventListener(this)

        uiInitialization()

        mPresenter = BookChaptersPresenter(this)

        mPresenter?.start(intent.extras)
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
            pexoPlayerManager.setToAppOpen(true)
            pexoPlayerManager.onTogglePlayPause()
        }

        btn_play_bottom.setOnClickListener {
            pexoPlayerManager.setToAppOpen(true)
            pexoPlayerManager.onTogglePlayPause()
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

    override fun onChapterClicked(
        data: ChaptersData,
        position: Int
    ) {
        if (mPresenter?.isBookMine()!!) {
            pexoPlayerManager.startPlayback(position)
            sliding_layout.panelState = SlidingUpPanelLayout.PanelState.EXPANDED
        } else {
            Snackbar.make(
                findViewById(android.R.id.content),
                applicationContext.getString(R.string.you_should_own), Snackbar.LENGTH_SHORT
            ).show()
        }
    }

    override fun setChapters(data: List<ChaptersData>) {

        mediaItemList = ArrayList<PexoMediaMetadata>()

        for (chapter in data) {
            val pexoMediaMetadata = PexoMediaMetadata(
                chapter.id.toString(),
                mPresenter?.getBookAuthor(),
                chapter.title,
                chapter.sound_file,
                "",
                mPresenter?.getBookReleasedDate(),
                mPresenter?.getBookIcon(),
                ""
            )

            mediaItemList.add(pexoMediaMetadata)
        }

        pexoPlayerManager.setupNewPlaylist(
            mPresenter?.getSavedBookId(), ArrayList<PexoMediaMetadata>(),
            mediaItemList, 0
        )

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
        mPresenter?.resetRepo()
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
                include_sliding_panel_childtwo.visibility = View.GONE
            }

            R.id.back -> {
                finish()
            }
        }
    }

    private fun validateParagraphClicked(str: String) {
        var selectedParagraph = chapterData!!.paragraphs.filter {
            str.equals(it.title)
        }[0]

//        player.seekToParagraph(selectedParagraph)
    }

    private fun getListItems(): ArrayList<String>? {
        var items = ArrayList<String>()
        for (paragraph in chapterData!!.paragraphs) {
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
                txtCurrentDuration.text = TimeUtils.toTimeFormat((seekProgress / 1000).toInt())
                txtDuration.text = TimeUtils.toTimeFormat((seekMax / 1000).toInt())
            }
        }
    }

//    private inner class UpdateUIBroadcastReceiver : BroadcastReceiver() {
//
//        override fun onReceive(context: Context, intent: Intent) {
//            val newMediaId = intent.getStringExtra(getString(R.string.broadcast_new_media_id))
//            val chapter = mPresenter?.getBookByID(newMediaId)
//            if (chapter != null)
//                text_songName.setText(chapter?.title)
//        }
//    }

    override fun onStart() {
        pexoPlayerManager.subscribeCallBack()
        super.onStart()
    }

    override fun onResume() {
        initSeekBarBroadcastReceiver()
//        initUpdateUIBroadcastReceiver()
        super.onResume()
    }

    override fun onPause() {
        super.onPause()
        if (mSeekbarBroadcastReceiver != null) {
            unregisterReceiver(mSeekbarBroadcastReceiver)
        }
//        if (mUpdateUIBroadcastReceiver != null) {
//            unregisterReceiver(mUpdateUIBroadcastReceiver)
//        }
    }

    private fun initSeekBarBroadcastReceiver() {
        val intentFilter = IntentFilter()
        intentFilter.addAction(getString(R.string.broadcast_seekbar_update))
        mSeekbarBroadcastReceiver = SeekBarBroadcastReceiver()
        registerReceiver(mSeekbarBroadcastReceiver, intentFilter)
    }

//    private fun initUpdateUIBroadcastReceiver() {
//        val intentFilter = IntentFilter()
//        intentFilter.addAction(getString(R.string.broadcast_update_ui))
//        mUpdateUIBroadcastReceiver = UpdateUIBroadcastReceiver()
//        registerReceiver(mUpdateUIBroadcastReceiver, intentFilter)
//    }

    private lateinit var mediaItemList: ArrayList<PexoMediaMetadata>
    private var chapterData: ChaptersData? = null

    private var isExpand = false

    var currentBookmark: Bookmark? = null

    var mPresenter: BookChaptersContract.Presenter? = null

    private var downloadProgressDialog: ProgressDialog? = null

    private var mSeekbarBroadcastReceiver: SeekBarBroadcastReceiver? = null

//    private var mUpdateUIBroadcastReceiver: UpdateUIBroadcastReceiver? = null

    private lateinit var pexoPlayerManager: PexoPlayerManager

    private val TAG: String = BookChaptersActivity::class.java.simpleName
}