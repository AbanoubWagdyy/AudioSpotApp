package com.audiospotapplication.UI.promoCodeCongratulations


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.audiospot.DataLayer.Model.Book
import com.audiospotapplication.DataLayer.DataRepository

import com.audiospotapplication.R
import com.audiospotapplication.utils.ImageUtils
import com.visionvalley.letuno.DataLayer.RepositorySource
import kotlinx.android.synthetic.main.fragment_congratulations.*

class CongratulationsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_congratulations, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mRepositorySource = DataRepository.getInstance(activity!!.applicationContext)
        mRepositorySource.getVoucherBook()?.let { bindResponse(it) }
    }

    fun bindResponse(result: Book) {

        ratingBar.rating = result!!.rate.toFloat()
        ratingBar.setOnRatingChangeListener(null)
        ratingBar.setOnTouchListener { p0, p1 -> true }

        ratingBar.setOnRatingChangeListener { ratingBar, rating, fromUser ->
            ratingBar.rating = result.rate.toFloat()
        }

        tvBookTitle.text = result!!.title
        tvNumberOfReviews.text = "(" + result!!.reviews.toString() + " reviews" + ")"
        tvAuthor.text = result!!.author
        if (result.narators.isNotEmpty()) {
            val builder = StringBuilder()
            for (narator in result.narators) {
                builder.append(narator.name).append(",")
            }
            tvNarrator.text = builder.toString().substring(0, builder.toString().length - 1)
        }
        ImageUtils.setImageFromUrlIntoImageViewUsingPicasso(result.cover, activity!!.applicationContext, ivBook)
    }

    lateinit var mRepositorySource: RepositorySource

    companion object {

        @JvmStatic
        fun newInstance() =
            CongratulationsFragment()
    }
}
