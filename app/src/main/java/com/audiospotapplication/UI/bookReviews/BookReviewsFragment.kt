package com.audiospotapplication.UI.bookReviews


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.audiospotapplication.BaseFragment
import com.audiospotapplication.DataLayer.DataRepository

import com.audiospotapplication.R
import com.audiospotapplication.UI.bookDetails.adapter.ReviewListAdapter
import com.visionvalley.letuno.DataLayer.RepositorySource
import kotlinx.android.synthetic.main.fragment_book_reviews.*

class BookReviewsFragment : BaseFragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_book_reviews, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mRepositorySource = DataRepository.getInstance(context!!)

        recyclerReviews.layoutManager = LinearLayoutManager(context!!)
        recyclerReviews.setHasFixedSize(true)
        recyclerReviews.isNestedScrollingEnabled = false

        recyclerReviews.adapter =
            ReviewListAdapter(mRepositorySource.getCurrentBookReviews(), ReviewListAdapter.Display.FULL)
    }


    companion object {
        @JvmStatic
        fun newInstance() =
            BookReviewsFragment()
    }

    lateinit var mRepositorySource: RepositorySource
}
