package com.audiospotapplication.UI.bookChapters

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
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
import com.audiospotapplication.utils.DialogUtils
import com.audiospotapplication.utils.ImageUtils
import com.example.jean.jcplayer.JcPlayerManagerListener
import com.example.jean.jcplayer.general.JcStatus
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
    BookChaptersContract.View, OnChapterCLickListener, JcPlayerManagerListener,
    JcPlayerManagerListener.PlayerFunctionsListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chapters)
        uiInitialization()

        mPresenter = BookChaptersPresenter(this)
        mPresenter.start(intent.extras)
    }

    private fun uiInitialization() {

        btn_play_bottom.setOnClickListener(this)
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
        ImageUtils.setImageFromUrlIntoImageViewUsingPicasso(cover, applicationContext, image_songAlbumArt, false)
        ImageUtils.setImageFromUrlIntoImageViewUsingPicasso(cover, applicationContext, bookCover, false)
    }

    override fun playAllChapters(result: ChaptersResponse) {
        if (mPresenter.isBookMine()) {
            chapterData = result.data[0]
            val jcAudios = ArrayList<JcAudio>()
            for (data in result.data) {
                var isDownloadedPath = mPresenter.validateChapterDownloaded(data)
                if (!isDownloadedPath.equals("")) {
                    jcAudios.add(JcAudio.createFromFilePath(data.id, data.title, isDownloadedPath, data.paragraphs))
                } else {
                    jcAudios.add(JcAudio.createFromURL(data.id, data.title, data.sound_file, data.paragraphs))
                }
            }

            player.initPlaylist(jcAudios, this, this)
            player.playAudio(player.myPlaylist!![0])
            player.createNotification(R.mipmap.ic_launcher)
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
                jcAudios.add(JcAudio.createFromFilePath(data.id, data.title, isDownloadedPath, data.paragraphs))
            } else {
                jcAudios.add(JcAudio.createFromURL(data.id, data.title, data.sound_file, data.paragraphs))
            }
            player.initPlaylist(jcAudios, this, this)
            player.playAudio(player.myPlaylist!![0])
            player.createNotification(R.mipmap.ic_launcher)
            sliding_layout.panelState = SlidingUpPanelLayout.PanelState.EXPANDED
            if (currentBookmark != null) {
                player.setBookmarkTime(currentBookmark!!.time)
            }
        } else {
            Snackbar.make(
                findViewById(android.R.id.content),
                applicationContext.getString(R.string.you_should_own), Snackbar.LENGTH_SHORT
            ).show()
        }
    }

    override fun setChapters(data: List<ChaptersData>) {
        if (data.isNotEmpty()) {
            recyclerChapters.layoutManager = LinearLayoutManager(applicationContext)
            recyclerChapters.setHasFixedSize(true)
            recyclerChapters.isNestedScrollingEnabled = false
            recyclerChapters.adapter = ChaptersAdapter(data!!, this)
            if (mPresenter.isBookMine()) {
                val jcAudios = ArrayList<JcAudio>()
                for (chapter in data) {
                    jcAudios.add(
                        JcAudio.createFromURL(
                            chapter.id,
                            chapter.title,
                            chapter.sound_file,
                            chapter.paragraphs
                        )
                    )
                }
                player.initPlaylist(jcAudios, this, this)
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

    override fun onChapterClicked(data: ChaptersData, currentAudioStatus: JcStatus?) {
        this.chapterData = data
        text_songName.text = data.title
        sliding_layout.panelState = SlidingUpPanelLayout.PanelState.EXPANDED
        player.seekTo(currentAudioStatus!!.currentPosition.toInt())
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

    override fun onBookmarkClicked() {
        var audio = player.currentAudio
        mPresenter.handleBookmarkClicked(player.getCurrentTimeString(), audio!!.id, audio.title)
    }

    override fun onDownloadClicked() {
        var audio = player.currentAudio
        if (audio != null)
            mPresenter.handleDownloadPressed(audio)
    }

    override fun onSpeedClicked() {

    }

    override fun onTimerClicked() {

    }

    override fun onPreparedAudio(status: JcStatus) {
        btn_play_bottom.setBackgroundResource(R.drawable.play_bottom)
    }

    override fun onCompletedAudio() {
    }

    override fun onPaused(status: JcStatus) {
        btn_play_bottom.setBackgroundResource(R.drawable.pause_bottom)
    }

    override fun onContinueAudio(status: JcStatus) {
        btn_play_bottom.setBackgroundResource(R.drawable.play_bottom)
    }

    override fun onPlaying(status: JcStatus) {
        btn_play_bottom.setBackgroundResource(R.drawable.play_bottom)
    }

    override fun onTimeChanged(status: JcStatus) {
    }

    override fun onStopped(status: JcStatus) {
    }

    override fun onJcpError(throwable: Throwable) {
    }

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
                player.kill()
                include_sliding_panel_childtwo.visibility = View.GONE
            }
            R.id.btn_play_bottom ->
                if (player.isPlaying) {
                    btn_play_bottom.setBackgroundResource(R.drawable.pause_bottom)
                    player.pause()
                } else {
                    player.continueAudio()
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

        player.seekToParagraph(selectedParagraph)
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
        player.stopCurrentPlayingAudio()
    }

    override fun showAddBookmarkScreen() {
        val intent = Intent(this, AddBookmarkActivity::class.java)
        startActivityForResult(intent, 1)
    }

    private lateinit var chapterData: ChaptersData
    private var isExpand = false
    var currentBookmark: Bookmark? = null
    lateinit var mPresenter: BookChaptersContract.Presenter

    private var downloadProgressDialog: ProgressDialog? = null
}