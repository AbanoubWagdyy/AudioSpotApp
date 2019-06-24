package com.audiospotapp.UI.homepage.home.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.audiospot.DataLayer.Model.Book
import com.audiospotapp.R
import com.audiospotapp.UI.books.Interface.onBookItemClickListener
import com.audiospotapp.utils.ImageUtils
import com.willy.ratingbar.ScaleRatingBar
import com.willy.ratingbar.BaseRatingBar


class HorizontalBooksAdapter(
    private var books: List<Book>?,
    private var mOnItemClickListener: onBookItemClickListener
) : RecyclerView.Adapter<HorizontalBooksAdapter.ViewHolder>() {

    private var context: Context? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.horizontal_book_list_view_item, parent, false)
        context = view.context
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var book = books!![position]
        holder.ivBook.setBackgroundResource(R.mipmap.start)
        ImageUtils.setImageFromUrlIntoImageViewUsingPicasso(book.cover, context, holder.ivBook)
        holder.tvBookName.text = book.title

        holder.rating.rating = book.rate.toFloat()

//        holder.rating.isClickable = false
//        holder.rating.isScrollable = false
//        holder.rating.isEnabled = false
//        holder.rating.isFocusable = false

        holder.rating.setOnRatingChangeListener(null)
        holder.rating.setOnTouchListener { p0, p1 -> true }

        holder.rating.setOnRatingChangeListener { ratingBar, rating, fromUser ->
            holder.rating.rating = book.rate.toFloat()
        }

        holder.ivPlay.setOnClickListener {
            mOnItemClickListener.onPlayClicked(book)
        }
    }

    override fun getItemCount(): Int {
        return books!!.size
    }

    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView), View.OnClickListener {

        val ivBook: ImageView = mView.findViewById(R.id.ivBook)
        val tvBookName: TextView = mView.findViewById(R.id.tvBookName)
        val ivPlay: ImageView = mView.findViewById(R.id.ivPlay)
        val rating: ScaleRatingBar = mView.findViewById(R.id.ratingBar)

        init {
            mView.setOnClickListener(this)
        }

        override fun onClick(v: View) {
            mOnItemClickListener.onItemClicked(books!![position])
        }
    }
}