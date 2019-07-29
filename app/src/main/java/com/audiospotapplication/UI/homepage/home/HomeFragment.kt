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
import com.audiospotapplication.BaseFragment
import com.audiospotapplication.UI.books.Interface.onBookItemClickListener
import com.audiospotapplication.UI.homepage.home.adapter.HomepageAdapter
import com.audiospotapplication.UI.bookDetails.BookDetailsActivity
import com.audiospotapplication.utils.BookDataConversion
import com.example.jean.jcplayer.JcPlayerManager
import com.example.jean.jcplayer.JcPlayerManagerListener
import com.example.jean.jcplayer.general.JcStatus
import dm.audiostreamer.MediaMetaData


class HomeFragment : BaseFragment(), HomeContract.View, onBookItemClickListener, JcPlayerManagerListener {

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
        var currentAudio = jcPlayerManager.currentAudio
        if (jcPlayerManager.isPlaying()) {
            if (currentAudio?.path.equals(book.sample)) {
                jcPlayerManager.pauseAudio()
                adapter = HomepageAdapter(this.homePageResponse, this, null)
                recyclerHome.adapter = adapter
                return
            } else {
                mPlayCallback.OnItemPlayed(BookDataConversion.convertBookToMediaMetaData(book))
            }
        } else {
            if (currentAudio != null && currentAudio?.path.equals(book.sample)) {
                jcPlayerManager.continueAudio()
            } else {
                mPlayCallback.OnItemPlayed(BookDataConversion.convertBookToMediaMetaData(book))
            }
        }
    }

    override fun setHomeResponse(result: HomepageRepsonse?) {
        this.homePageResponse = result
        recyclerHome.layoutManager = LinearLayoutManager(context)
        recyclerHome.setHasFixedSize(true)
        recyclerHome.isNestedScrollingEnabled = false
        adapter = HomepageAdapter(this.homePageResponse, this, jcPlayerManager.currentAudio)
        recyclerHome.adapter = adapter
    }

    override fun showErrorMessage(message: String) {
        if (activity != null)
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
    }

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

    private val jcPlayerManager: JcPlayerManager by lazy {
        JcPlayerManager.getInstance(activity!!.applicationContext).get()!!
    }
}