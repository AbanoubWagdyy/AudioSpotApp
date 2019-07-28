package com.audiospotapplication.UI.promoCodeCongratulations


import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.audiospot.DataLayer.Model.Book
import com.audiospotapplication.DataLayer.DataRepository

import com.audiospotapplication.R
import com.audiospotapplication.UI.bookDetails.BookDetailsActivity
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

        linearBook.setOnClickListener {
            mRepositorySource.saveBook(currentBook)
            val intent = Intent(activity!!, BookDetailsActivity::class.java)
            startActivity(intent)
            activity!!.finish()
        }
    }

    @SuppressLint("SetTextI18n")
    fun bindResponse(result: Book) {

        this.currentBook = result

        ratingBar.rating = result.rate.toFloat()
        ratingBar.setOnRatingChangeListener(null)
        ratingBar.setOnTouchListener { p0, p1 -> true }

        ratingBar.setOnRatingChangeListener { ratingBar, rating, fromUser ->
            ratingBar.rating = result.rate.toFloat()
        }

        tvBookTitle.text = result.title
        var numberofReviews= result.reviews.toString()
        tvNumberOfReviews.text = "($numberofReviews reviews)"
        tvAuthor.text = result.author
        if (result.narators.isNotEmpty()) {
            val builder = StringBuilder()
            for (narrator in result.narators) {
                builder.append(narrator.name).append(",")
            }
            tvNarrator.text = builder.toString().substring(0, builder.toString().length - 1)
        }
        ImageUtils.setImageFromUrlIntoImageViewUsingPicasso(result.cover, activity!!.applicationContext, ivBook)

        val listMyBooks = ArrayList<Book>()
        mRepositorySource.getMyBooks()?.let { listMyBooks.addAll(it) }
        listMyBooks.add(result)
        mRepositorySource.setMyBooks(listMyBooks)
    }

    private lateinit var currentBook: Book

    lateinit var mRepositorySource: RepositorySource

    companion object {

        @JvmStatic
        fun newInstance() =
            CongratulationsFragment()
    }
}
