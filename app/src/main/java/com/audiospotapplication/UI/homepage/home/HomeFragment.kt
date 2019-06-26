package com.audiospotapplication.UI.homepage.home

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import com.audiospot.DataLayer.Model.Book

import com.audiospotapplication.R
import com.google.android.material.snackbar.Snackbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.audiospot.DataLayer.Model.HomepageRepsonse
import com.audiospotapplication.UI.books.Interface.onBookItemClickListener
import com.audiospotapplication.UI.homepage.home.adapter.HomepageAdapter
import com.audiospotapplication.UI.bookDetails.BookDetailsActivity
import com.audiospotapplication.utils.BookMediaDataConversion
import dm.audiostreamer.AudioStreamingManager
import dm.audiostreamer.MediaMetaData


class HomeFragment : Fragment(), HomeContract.View, onBookItemClickListener {

    override fun setCartNumber(size: Int) {
    }

    override fun showBookDetailsScreen() {
        val intent = Intent(activity!!, BookDetailsActivity::class.java)
        startActivity(intent)
    }

    override fun onItemClicked(book: Book) {
        mPresenter.saveBook(book)
    }

    override fun onPlayClicked(book: Book) {
        if (streamingManager.isPlaying) {
            if (book.id == streamingManager.currentAudioId.toInt()) {
                streamingManager.handlePauseRequest()
                adapter = HomepageAdapter(this.homePageResponse, this, null)
                recyclerHome.adapter = adapter
            } else {
                var mediaData = BookMediaDataConversion.convertBookToMediaMetaData(book)
                mPlayCallback.OnItemPlayed(mediaData)
            }
        } else {
            if (currentSong != null) {
                streamingManager.handlePlayRequest()
            } else {
                var mediaData = BookMediaDataConversion.convertBookToMediaMetaData(book)
                mPlayCallback.OnItemPlayed(mediaData)
            }
        }
    }

    override fun setHomeResponse(result: HomepageRepsonse?) {
        this.homePageResponse = result
        recyclerHome.layoutManager = LinearLayoutManager(context)
        recyclerHome.setHasFixedSize(true)
        recyclerHome.isNestedScrollingEnabled = false
        adapter = HomepageAdapter(this.homePageResponse, this, currentSong)
        recyclerHome.adapter = adapter
    }

    override fun showErrorMessage(message: String) {
        Snackbar.make(activity!!.findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG).show()
    }

    override fun showDialog() {
        progress.visibility = View.VISIBLE
    }

    override fun hideDialog() {
        progress.visibility = View.GONE
    }

    override fun getContainingActivity(): AppCompatActivity {
        return activity as AppCompatActivity
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_home, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerHome = view.findViewById(R.id.recyclerHome)
        progress = view.findViewById(R.id.progress)
        mPresenter = HomePresenter(this)
        mPresenter.start()
        configAudioStreamer()
        checkAlreadyPlaying()
    }

    private fun configAudioStreamer() {
        streamingManager = AudioStreamingManager.getInstance(activity!!.applicationContext)
        streamingManager!!.isPlayMultiple = false
        streamingManager!!.setShowPlayerNotification(true)
    }

    private fun checkAlreadyPlaying() {
        if (streamingManager!!.isPlaying) {
            currentSong = streamingManager!!.currentAudio
            if (currentSong != null) {
                currentSong!!.playState = streamingManager!!.mLastPlaybackState
            }
        }
    }

    private var currentSong: MediaMetaData? = null
    private lateinit var streamingManager: AudioStreamingManager
    private lateinit var mPlayCallback: onItemPlayClickListener
    lateinit var mPresenter: HomeContract.Presenter
    lateinit var recyclerHome: RecyclerView
    lateinit var progress: ProgressBar
    var homePageResponse: HomepageRepsonse? = null
    var adapter: HomepageAdapter? = null

    companion object {

        @JvmStatic
        fun newInstance(): HomeFragment {
            return HomeFragment()
        }
    }

    override fun onAttach(activity: Activity) {
        super.onAttach(activity)

        try {
            mPlayCallback = activity as onItemPlayClickListener
        } catch (e: ClassCastException) {
            throw ClassCastException(activity.toString() + " must implement MyInterface ")
        }
    }

    interface onItemPlayClickListener {
        fun OnItemPlayed(mediaData: MediaMetaData)
    }
}