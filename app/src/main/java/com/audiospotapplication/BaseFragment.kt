package com.audiospotapplication

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.media.session.MediaControllerCompat
import android.support.v4.media.session.PlaybackStateCompat
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.audiospot.DataLayer.Model.Book
import com.audiospotapplication.DataLayer.Model.BaseViewModel
import com.audiospotapplication.DataLayer.Retrofit.RetrofitResponseHandler
import com.audiospotapplication.UI.bookChapters.BookChaptersActivity

import com.audiospotapplication.UI.login.LoginActivity
import com.audiospotapplication.utils.DialogUtils
import com.google.android.material.snackbar.Snackbar
import com.ps.pexoplayer.PexoEventListener
import com.ps.pexoplayer.PexoPlayerManager
import com.ps.pexoplayer.model.PexoMediaMetadata
import com.visionvalley.letuno.DataLayer.RepositorySource
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

open class BaseFragment : Fragment(), BaseView, PexoEventListener {

    override fun onPlayerMediaControllerConnected(mediaController: MediaControllerCompat?) {
        Log.d("", "")
    }

    override fun onPlayerMetadataChanged(pexoMediaMetadata: PexoMediaMetadata?) {
        playlistObserver.value = pexoMediaMetadata?.mediaId
    }

    override fun onPlayerPlaybackStateChanged(state: PlaybackStateCompat?) {
        if (state != null) {
            when (state.state) {
                PlaybackStateCompat.STATE_BUFFERING -> {
                }
                PlaybackStateCompat.STATE_PLAYING -> {
                    IsPlayingObserver.value = true
                }
                PlaybackStateCompat.STATE_PAUSED -> {
                    IsPlayingObserver.value = false
                }
                PlaybackStateCompat.STATE_ERROR -> {
                }
            }
        }
    }

    override fun onControllerDisconnect() {
        Log.d("", "")
    }

    override fun updatePlayerBuffer(progress: Int) {
        Log.d("", "")
    }

    override fun updatePlayerSeekBar(progress: Int, max: Int) {
        Log.d("", "")
    }

    override fun updateShuffle(state: Int) {
        Log.d("", "")
    }

    override fun updateRepeat(state: Int) {
        Log.d("", "")
    }

    override fun prepareLastPlayedMedia() {
        pexoPlayerManager.onFinishedGettingPreviousSessionData(ArrayList<PexoMediaMetadata>())
    }

    open var isPlaying: Boolean = false

    private val baseViewModel: BaseViewModel by viewModel()
//    private val mRepositorySource: RepositorySource by inject()

    override fun showLoginPage() {
        baseViewModel.clearData()

        val intent = Intent(activity!!, LoginActivity::class.java)
        intent.addFlags(
            Intent.FLAG_ACTIVITY_CLEAR_TOP or
                    Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        )
        startActivity(intent)
        activity!!.finish()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        baseViewModel.getObserverForError().observe(this, Observer {
            Snackbar.make(activity!!.findViewById(android.R.id.content), it, Snackbar.LENGTH_SHORT)
                .show()
        })

        baseViewModel.getProgressLoadingObserver().observe(this, Observer {
            if (it) {
                DialogUtils.showProgressDialog(activity!!, context!!.getString(R.string.loading))
            }
        })

        baseViewModel.getErrorAuthenticationObserver().observe(this, Observer {
            if (it == RetrofitResponseHandler.Companion.Status.UNAUTHORIZED) {
                showLoginPage()
            }
        })

        pexoPlayerManager.subscribeCallBack()

        isPlaying = pexoPlayerManager.pexoInstance.isPlaying

        getPlaylistIdObserver().value = pexoPlayerManager.pexoInstance.playlistId
    }

    override fun onAttach(context: Context?) {
        pexoPlayerManager = PexoPlayerManager(context)
        pexoPlayerManager.setPendingIntentClass(BookChaptersActivity::class.java)
        pexoPlayerManager.setPexoEventListener(this)
        super.onAttach(context)
    }

    override fun onDetach() {
        pexoPlayerManager.unSubscribeCallBack()
        super.onDetach()
    }

    open fun playBookSample(book: Book): Int {
        if (!getPlaylistIdObserver().value!!.equals(book.id.toString() + book.id.toString())) {
            val mediaItemList = ArrayList<PexoMediaMetadata>()
            val id = book.id.toString() + book.id.toString()
            val pexoMediaMetadata = PexoMediaMetadata(
                id,
                book.author,
                book.title,
                book.sample,
                "",
                book.release_date,
                book.cover,
                ""
            )

            mediaItemList.add(pexoMediaMetadata)

            pexoPlayerManager.setupNewPlaylist(
                book.id.toString() + book.id.toString(), java.util.ArrayList<PexoMediaMetadata>(),
                mediaItemList, 0
            )

            pexoPlayerManager.startPlayback()

            return R.drawable.play
        } else {
            return R.drawable.ic_pause
        }
    }


    open fun handlePlayPause() {
        pexoPlayerManager.setToAppOpen(true)
        pexoPlayerManager.onTogglePlayPause()
    }

    open fun getPlaylistIdObserver(): MutableLiveData<String> {
        return playlistObserver
    }

    open fun getPlayingObserver(): MutableLiveData<Boolean> {
        return IsPlayingObserver
    }

    private var playlistObserver = MutableLiveData<String>()
    private var IsPlayingObserver = MutableLiveData<Boolean>()
    private lateinit var pexoPlayerManager: PexoPlayerManager
}