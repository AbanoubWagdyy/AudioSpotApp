package com.audiospotapp.UI.promoCodeCongratulations


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.audiospot.DataLayer.Model.BookDetailsResponse
import com.audiospotapp.DataLayer.DataRepository

import com.audiospotapp.R
import com.audiospotapp.utils.ImageUtils
import com.visionvalley.letuno.DataLayer.RepositorySource
import kotlinx.android.synthetic.main.fragment_congratulations.*

class CongratulationsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_congratulations, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mRepositorySource = DataRepository.getInstance(activity!!.applicationContext)

    }

    fun bindResponse(result: BookDetailsResponse?) {

        ratingBar.rating = result!!.data.rate.toFloat()
        ratingBar.setOnRatingChangeListener(null)
        ratingBar.setOnTouchListener { p0, p1 -> true }

        ratingBar.setOnRatingChangeListener { ratingBar, rating, fromUser ->
            ratingBar.rating = result.data.rate.toFloat()
        }

        tvBookTitle.text = result!!.data.title
        tvNumberOfReviews.text = "(" + result!!.data.reviews.toString() + " reviews" + ")"
        tvAuthor.text = result!!.data.author
        if (result.data.narators.isNotEmpty()) {
            val builder = StringBuilder()
            for (narator in result.data.narators) {
                builder.append(narator.name).append(",")
            }
            tvNarrator.text = builder.toString().substring(0, builder.toString().length - 1)
        }
        ImageUtils.setImageFromUrlIntoImageViewUsingPicasso(result.data.cover, activity!!.applicationContext, ivBook)
    }

    lateinit var mRepositorySource: RepositorySource

    companion object {

        @JvmStatic
        fun newInstance() =
            CongratulationsFragment()
    }
}
