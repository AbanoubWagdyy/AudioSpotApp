package com.audiospotapplication.ui.bookChapters

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.support.v4.media.session.MediaControllerCompat
import android.support.v4.media.session.PlaybackStateCompat
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.audiospotapplication.data.model.Bookmark
import com.audiospotapplication.data.model.ChaptersData
import com.audiospotapplication.R
import com.audiospotapplication.ui.addBookmark.AddBookmarkActivity
import com.audiospotapplication.ui.bookChapters.Interface.OnChapterCLickListener
import com.audiospotapplication.ui.bookChapters.adapter.ChaptersAdapter
import com.audiospotapplication.ui.homepage.HomepageActivity
import com.audiospotapplication.ui.login.LoginActivity
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

    override fun onItemDownloadPressed(data: ChaptersData) {
        mPresenter?.setCurrentChapterID(data.id)
        mPresenter?.handleDownloadPressed()
    }

    override fun onPlayerMediaControllerConnected(mediaController: MediaControllerCompat?) {
        this.mediaController = mediaController
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
        Log.d("onControllerDisconnect", "onControllerDisconnect")
    }

    override fun updatePlayerBuffer(progress: Int) {
        Log.d("updatePlayerBuffer", "updatePlayerBuffer")
    }

    override fun updatePlayerSeekBar(progress: Int, max: Int) {
        this.seekBarProgress = progress
        this.seekBarProgressMax = progress
        if (!seekBar.isTracking) {
            seekBar.progress = progress
            seekBar.max = max
            txtCurrentDuration.text = TimeUtils.toTimeFormat((progress / 1000))
            txtDuration.text = TimeUtils.toTimeFormat((max / 1000))
        }
        txtCurrentMusic.text = getParagraph(progress)
    }

    private fun getParagraph(progress: Int): CharSequence? {
        mPresenter?.getCurrentChapterParagraphs()?.let {
            for (paragraph in mPresenter?.getCurrentChapterParagraphs()!!) {
                val fromTime = paragraph.from_time.toInt() * 1000
                val toTime = paragraph.to_time.toInt() * 1000

                if (progress in fromTime..toTime) {
                    return paragraph.title
                }
            }

            return ""
        }
        return ""
    }

    override fun updateShuffle(state: Int) {
        Log.d("updateShuffle", "updateShuffle")
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

        uiInitialization()

        mPresenter = BookChaptersPresenter(this)

        mPresenter?.start()
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
            if (mPresenter?.isBookMine()!!) {
                pexoPlayerManager.setToAppOpen(true)
                if (mPresenter!!.getSavedBookId().equals(pexoPlayerManager.pexoInstance.playlistId)) {
                    pexoPlayerManager.onTogglePlayPause()
                } else {
                    val chapters = mPresenter!!.getChapters()
                    if (chapters != null) {
                        generateMediaItems(chapters)
                        pexoPlayerManager.startPlayback()
                    }
                }
            } else {
                Snackbar.make(
                    findViewById(android.R.id.content),
                    applicationContext.getString(R.string.you_should_own), Snackbar.LENGTH_SHORT
                ).show()
            }
        }

        btn_play_bottom.setOnClickListener {
            if (mPresenter?.isBookMine()!!) {
                pexoPlayerManager.setToAppOpen(true)
                if (mPresenter!!.getSavedBookId().equals(pexoPlayerManager.pexoInstance.playlistId)) {
                    pexoPlayerManager.onTogglePlayPause()
                } else {
                    val chapters = mPresenter!!.getChapters()
                    if (chapters != null) {
                        generateMediaItems(chapters)
                    }
                    Handler().postDelayed({
                        pexoPlayerManager.startPlayback()

                    }, 1300)
                }
            } else {
                Snackbar.make(
                    findViewById(android.R.id.content),
                    applicationContext.getString(R.string.you_should_own), Snackbar.LENGTH_SHORT
                ).show()
            }
        }

        bookmark.setOnClickListener {
            mPresenter?.handleBookmarkClicked(txtCurrentDuration.text.toString())
        }

        download.setOnClickListener {
            if (mPresenter?.isBookMine()!!) {
                mPresenter?.handleDownloadPressed()
            }
        }

        btnPrev.setOnClickListener {
            pexoPlayerManager.updateProgress(seekBarProgress - 30000)
        }

        btnNext.setOnClickListener {
            pexoPlayerManager.updateProgress(seekBarProgress + 30000)
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

    override fun playChapter(chaptersData: ChaptersData, index: Int) {
        onChapterClicked(chaptersData, position = index)
    }

    override fun refreshAdapter() {
       adapter.notifyDataSetChanged()
    }

    override fun onChapterClicked(data: ChaptersData, position: Int) {
        if (mPresenter?.isBookMine()!!) {
            if (mPresenter!!.getChapters() != null) {
                generateMediaItems(mPresenter!!.getChapters()!!)
            }
            mPresenter?.setCurrentChapterParagraphs(data.paragraphs)
            mPresenter?.setCurrentChapterID(data.id)
            mPresenter?.setCurrentChapterTitle(data.title)

            sliding_layout.panelState = SlidingUpPanelLayout.PanelState.EXPANDED

            Handler().postDelayed({
                pexoPlayerManager.startPlayback(position)

                if (currentBookmark != null) {
                    pexoPlayerManager.updateProgress(currentBookmark!!.time * 1000)
                }
            }, 1300)
        } else {
            Snackbar.make(
                findViewById(android.R.id.content),
                applicationContext.getString(R.string.you_should_own), Snackbar.LENGTH_SHORT
            ).show()
        }
    }

    override fun setChapters(
        data: List<ChaptersData>,
        id: Int
    ) {

        if (id.toString().equals(pexoPlayerManager.pexoInstance.playlistId)) {
            pexoPlayerManager.setPexoEventListener(this)
            pexoPlayerManager.subscribeCallBack()
        }

        if (data.isNotEmpty()) {
            recyclerChapters.layoutManager = LinearLayoutManager(applicationContext)
            recyclerChapters.setHasFixedSize(true)
            recyclerChapters.isNestedScrollingEnabled = false
            adapter = ChaptersAdapter(data, this, mPresenter?.isBookMine())
            recyclerChapters.adapter = adapter
        } else {
            Snackbar.make(
                findViewById(android.R.id.content), "Chapters not Found",
                Snackbar.LENGTH_SHORT
            ).show()
        }
    }

    private fun generateMediaItems(data: List<ChaptersData>) {
        mediaItemList = ArrayList()

        pexoPlayerManager.setPexoEventListener(this)

        pexoPlayerManager.subscribeCallBack()

        for (chapter in data) {
            var path = mPresenter?.validateChapterDownloaded(chapter)
            if (path.equals(""))
                path = chapter.sound_file
            val pexoMediaMetadata = PexoMediaMetadata(
                chapter.id.toString(),
                mPresenter?.getBookAuthor(),
                chapter.title,
                path,
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

    override fun updateProgress(progress: Int) {
        runOnUiThread {
            downloadProgressDialog!!.setMessage("Downloading(${progress} %) ....")
        }
    }

    override fun showDownloadingDialog() {
        downloadProgressDialog!!.show()
    }

    override fun dismissDownloadingDialog() {
        downloadProgressDialog!!.dismiss()
    }

//    override fun onBookmarkClicked() {
//        var audio = player.currentAudio
//        mPresenter.handleBookmarkClicked(player.getCurrentTimeString(), audio!!.id, audio.title)
//    }

//
//    override fun onSpeedClicked() {
//
//    }
//
//    override fun onTimerClicked() {
//
//    }

    override fun onClick(view: View?) {
        when (view!!.id) {
            R.id.ivParagraphs -> {
                if (getParagraphListItems()!!.size > 0) {
                    FilterableListDialog.create(
                        this,
                        getParagraphListItems()
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
        if (mPresenter?.getCurrentChapterParagraphs() == null) {
            return
        }

        val selectedParagraph =
            mPresenter?.getCurrentChapterParagraphs()!!.filter {
                str.equals(it.title)
            }

        if (selectedParagraph.isNullOrEmpty()) {
            return
        }

        pexoPlayerManager.updateProgress(selectedParagraph[0].from_time.toInt() * 1000)
    }

    private fun getParagraphListItems(): ArrayList<String>? {
        val items = ArrayList<String>()
        mPresenter?.getCurrentChapterParagraphs()?.let {
            for (paragraph in it) {
                items.add(paragraph.title)
            }
            return items
        }
        return ArrayList()
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
    }

    override fun showAddBookmarkScreen() {
        val intent = Intent(this, AddBookmarkActivity::class.java)
        startActivityForResult(intent, 1)
    }

    override fun onPause() {
        seekBar.disconnectController()
        super.onPause()
    }

    private lateinit var adapter: ChaptersAdapter
    private var seekBarProgress: Int = 0
    private var seekBarProgressMax: Int = 0
    private var mediaController: MediaControllerCompat? = null
    private lateinit var mediaItemList: ArrayList<PexoMediaMetadata>

    private var isExpand = false

    var currentBookmark: Bookmark? = null

    var mPresenter: BookChaptersContract.Presenter? = null

    private var downloadProgressDialog: ProgressDialog? = null

    private lateinit var pexoPlayerManager: PexoPlayerManager

    private val TAG: String = BookChaptersActivity::class.java.simpleName
}