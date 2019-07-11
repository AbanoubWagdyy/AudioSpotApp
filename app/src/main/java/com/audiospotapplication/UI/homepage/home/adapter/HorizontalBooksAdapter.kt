package com.audiospotapplication.UI.homepage.home.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.audiospot.DataLayer.Model.Book
import com.audiospotapplication.R
import com.audiospotapplication.UI.books.Interface.onBookItemClickListener
import com.audiospotapplication.utils.ImageUtils
import com.example.jean.jcplayer.model.JcAudio
import com.willy.ratingbar.ScaleRatingBar
import dm.audiostreamer.MediaMetaData


class HorizontalBooksAdapter(
    private var books: List<Book>?,
    private var mOnItemClickListener: onBookItemClickListener,
    currentSong: JcAudio?
) : RecyclerView.Adapter<HorizontalBooksAdapter.ViewHolder>() {

    private var context: Context? = null
    private var currentSong: JcAudio? = null

    init {
        this.currentSong = currentSong
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.horizontal_book_list_view_item, parent, false)
        context = view.context
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var book = books!![position]
        holder.ivBook.setBackgroundResource(R.mipmap.login_icon)
        ImageUtils.setImageFromUrlIntoImageViewUsingPicasso(book.cover, context, holder.ivBook)
        holder.tvBookName.text = book.title
        holder.rating.rating = book.rate.toFloat()

        holder.rating.setOnRatingChangeListener(null)
        holder.rating.setOnTouchListener { p0, p1 -> true }

        if (currentSong != null) {
            if (book.id == currentSong!!.id) {
                holder.ivPlay.setBackgroundResource(R.mipmap.homepage_play)
                holder.ivPlay.setTag(R.mipmap.homepage_play)
            } else {
                holder.ivPlay.setBackgroundResource(R.mipmap.play)
                holder.ivPlay.setTag(R.mipmap.play)
            }
        }

        holder.rating.setOnRatingChangeListener { ratingBar, rating, fromUser ->
            holder.rating.rating = book.rate.toFloat()
        }

        holder.ivPlay.setOnClickListener {
            if (holder.ivPlay.getTag() != null) {
                val resourceID = holder.ivPlay.getTag() as Int
                if (resourceID == R.mipmap.homepage_play) {
                    holder.ivPlay.setBackgroundResource(R.mipmap.play)
                    holder.ivPlay.setTag(R.mipmap.play)
                } else {
                    holder.ivPlay.setBackgroundResource(R.mipmap.homepage_play)
                    holder.ivPlay.setTag(R.mipmap.homepage_play)
                }
            }
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