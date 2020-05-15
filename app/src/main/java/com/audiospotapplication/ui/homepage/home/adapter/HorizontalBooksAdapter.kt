package com.audiospotapplication.ui.homepage.home.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.audiospot.DataLayer.Model.Book
import com.audiospotapplication.R
import com.audiospotapplication.ui.books.Interface.onBookItemClickListener
import com.audiospotapplication.utils.ImageUtils
import com.willy.ratingbar.ScaleRatingBar


class HorizontalBooksAdapter(
    private var books: List<Book>?,
    private var mOnItemClickListener: onBookItemClickListener,
    private var mPlaylistId: String?,
    private var playing: Boolean
) : RecyclerView.Adapter<HorizontalBooksAdapter.ViewHolder>() {

    private var context: Context? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.horizontal_book_list_view_item, parent, false)
        context = view.context
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val book = books!![position]
        holder.ivBook.setBackgroundResource(R.mipmap.login_icon)
        ImageUtils.setImageFromUrlIntoImageViewUsingGlide(book.cover, context, holder.ivBook)
        holder.tvBookName.text = book.title
        holder.tvAuthor.text = book.author
        holder.rating.rating = book.rate.toFloat()

        holder.rating.setOnRatingChangeListener(null)
        holder.rating.setOnTouchListener { p0, p1 -> true }

        if (!mPlaylistId.equals("")) {
            val id = book.id.toString() + book.id.toString()
            if (mPlaylistId.equals(id)) {
                if (playing) {
                    holder.ivPlay.setBackgroundResource(R.mipmap.homepage_play)
                } else {
                    holder.ivPlay.setBackgroundResource(R.mipmap.play)
                }
            } else {
                holder.ivPlay.setBackgroundResource(R.mipmap.play)
            }
        }

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
        val tvAuthor: TextView = mView.findViewById(R.id.tvAuthor)
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