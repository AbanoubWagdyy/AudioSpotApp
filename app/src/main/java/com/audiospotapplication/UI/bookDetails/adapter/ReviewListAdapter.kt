package com.audiospotapplication.UI.bookDetails.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.audiospotapplication.DataLayer.Model.Review
import com.audiospotapplication.R
import com.willy.ratingbar.ScaleRatingBar

class ReviewListAdapter(
    private var reviews: List<Review>,
    private var display: Display
) : RecyclerView.Adapter<ReviewListAdapter.ViewHolder>() {

    private var context: Context? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.review_list_item, parent, false)
        context = view.context
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        if (reviews.size < 2) {
            return reviews.size
        } else {
            return if (display == Display.SEMI) {
                2
            } else {
                reviews.size
            }
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var review = reviews[position]
        holder.ratingBar.rating = review.rate.toFloat()
        holder.username.text = review.name
        holder.date.text = review.created_at
        holder.tvReviewTitle.text = review.comment
        holder.tvReviewBody.text = review.comment
    }

    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        val username: TextView = mView.findViewById(R.id.username)
        val date: TextView = mView.findViewById(R.id.date)
        val ratingBar: ScaleRatingBar = mView.findViewById(R.id.ratingBar)
        val tvReviewTitle: TextView = mView.findViewById(R.id.tvReviewTitle)
        val tvReviewBody: TextView = mView.findViewById(R.id.tvReviewBody)
    }

    enum class Display {
        SEMI, FULL
    }
}