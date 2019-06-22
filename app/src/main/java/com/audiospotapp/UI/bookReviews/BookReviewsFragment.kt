package com.audiospotapp.UI.bookReviews


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.audiospotapp.DataLayer.DataRepository

import com.audiospotapp.R
import com.audiospotapp.UI.bookDetails.adapter.ReviewListAdapter
import com.visionvalley.letuno.DataLayer.RepositorySource
import kotlinx.android.synthetic.main.fragment_book_reviews.*

class BookReviewsFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_book_reviews, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mRepositorySource = DataRepository.getInstance(context)

        recyclerReviews.layoutManager = LinearLayoutManager(context)
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
