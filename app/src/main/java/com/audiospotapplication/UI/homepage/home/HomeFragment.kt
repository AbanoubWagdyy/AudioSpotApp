package com.audiospotapplication.UI.homepage.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.audiospot.DataLayer.Model.Book

import com.audiospotapplication.R
import com.audiospotapplication.UI.BaseFragment
import com.audiospotapplication.UI.books.Interface.onBookItemClickListener
import com.audiospotapplication.UI.homepage.home.adapter.HomepageAdapter
import com.audiospotapplication.UI.bookDetails.BookDetailsActivity
import kotlinx.android.synthetic.main.fragment_home.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeFragment : BaseFragment(), onBookItemClickListener {

    override fun onItemClicked(book: Book) {
        viewModel.saveBook(book)
        showBookDetailsScreen()
    }

    fun showBookDetailsScreen() {
        val intent = Intent(requireActivity(), BookDetailsActivity::class.java)
        startActivity(intent)
    }

    override fun onPlayClicked(book: Book) {
        if (playBookSample(book) == R.drawable.ic_pause) {
            handlePlayPause()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ViewCompat.setNestedScrollingEnabled(recyclerHome, false)

        viewModel.getHomepageBooksLiveData().observe(viewLifecycleOwner, Observer {
            progress.visibility = View.GONE

            if (it != null) {
                adapter = HomepageAdapter(
                    it,
                    this
                )
                recyclerHome.layoutManager = LinearLayoutManager(requireActivity())
                recyclerHome.adapter = adapter

                getPlaylistIdObserver().observe(viewLifecycleOwner, Observer {
                    if (adapter != null && !it.equals(""))
                        adapter!!.updatePlaylistId(it, isPlaying)
                })

                getPlayingObserver().observe(viewLifecycleOwner, Observer {
                    if (adapter != null)
                        adapter!!.updatePlaylistId(getPlaylistIdObserver().value, it)
                })
            }
        })

        viewModel.getErrorObserverLiveData().observe(viewLifecycleOwner, Observer {
            progress.visibility = View.GONE
            recyclerHome.visibility = View.GONE
            relativeError.visibility = View.VISIBLE
            tvError.text = it
        })

        retry.setOnClickListener {
            recyclerHome.visibility = View.VISIBLE
            relativeError.visibility = View.GONE
            progress.visibility = View.VISIBLE
            viewModel.getHomepageBooks()
        }
    }

    var adapter: HomepageAdapter? = null

    private val viewModel: HomeViewModel by viewModel()

    companion object {

        @JvmStatic
        fun newInstance(): HomeFragment {
            return HomeFragment()
        }
    }
}