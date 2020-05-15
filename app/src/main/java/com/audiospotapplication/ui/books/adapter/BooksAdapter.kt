package com.audiospotapplication.ui.books.adapter

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
import com.audiospotapplication.utils.TimeUtils
import com.willy.ratingbar.ScaleRatingBar

class BooksAdapter() : RecyclerView.Adapter<BooksAdapter.ViewHolder>() {

    constructor(
        books: List<Book>,
        mOnItemClickListener: onBookItemClickListener,
        currentPlayListId: String,
        IsPlaying: Boolean
    ) : this() {
        this.books = books
        this.mOnItemClickListener = mOnItemClickListener
        this.currentPlayListId = currentPlayListId
        this.IsPlaying = IsPlaying
    }

    constructor(
        books: List<Book>,
        mOnItemClickListener: onBookItemClickListener,
        currentPlayListId: String,
        IsPlaying: Boolean,
        mDeleteListener: onBookItemClickListener.onCartBookDeleteClickListener
    ) : this() {
        this.books = books
        this.mOnItemClickListener = mOnItemClickListener
        this.mDeleteListener = mDeleteListener
        this.currentPlayListId = currentPlayListId
        this.IsPlaying = IsPlaying
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.book_list_view_item, parent, false)
        context = view.context
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val book = books[position]
        holder.ivBook.setBackgroundResource(R.mipmap.login_icon)
        ImageUtils.setImageFromUrlIntoImageViewUsingGlide(book.cover, context, holder.ivBook)
        holder.tvAuthor.text = book.author
        holder.tvPublisher.text = book.publisher

        if (book.narators.isNotEmpty()) {
            val builder = StringBuilder()
            for (narator in book.narators) {
                builder.append(narator.name).append(",")
            }
            holder.tvNarrator.text = builder.toString().substring(0, builder.toString().length - 1)
        }

        holder.tvLength.text = TimeUtils.toTimeFormat(book.total_time.toInt()) + " Hours"
        holder.tvLanguage.text = book.language
        holder.tvBookName.text = book.title
        holder.tvPrice.text = book.price.toString() + " EGP."

        holder.ratingBar.rating = book.rate.toFloat()

        holder.ratingBar.setOnRatingChangeListener(null)
        holder.ratingBar.setOnTouchListener { p0, p1 -> true }

        holder.ratingBar.setOnRatingChangeListener { ratingBar, rating, fromUser ->
            holder.ratingBar.rating = book.rate.toFloat()
        }

        if (isToShowDelete) {
            holder.delete.visibility = View.VISIBLE
        } else {
            holder.delete.visibility = View.GONE
        }

        holder.delete.setOnClickListener {
            mDeleteListener.onItemDeleted(book)
        }

        if (!currentPlayListId.equals("")) {
            val id = book.id.toString() + book.id.toString()
            if (currentPlayListId.equals(id)) {
                if (IsPlaying) {
                    holder.ivPlay.setBackgroundResource(R.mipmap.homepage_play)
                } else {
                    holder.ivPlay.setBackgroundResource(R.mipmap.play)
                }
            } else {
                holder.ivPlay.setBackgroundResource(R.mipmap.play)
            }
        }

        holder.ivPlay.setOnClickListener {
            if (IsPlaying) {
                val id = book.id.toString() + book.id.toString()
                if (currentPlayListId.equals(id)) {
                    holder.ivPlay.setBackgroundResource(R.mipmap.play)
                } else {
                    holder.ivPlay.setBackgroundResource(R.mipmap.homepage_play)
                }
            }
            mOnItemClickListener.onPlayClicked(book)
        }
    }

    override fun getItemCount(): Int {
        return books.size
    }

    fun updatePlaylistId(it: String?, playing: Boolean) {
        currentPlayListId = it!!
        this.IsPlaying = playing
        notifyDataSetChanged()
    }

    fun showDelete() {
        isToShowDelete = true
        notifyDataSetChanged()
    }

    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView), View.OnClickListener {

        val ivBook: ImageView = mView.findViewById(R.id.ivBook)
        val ivPlay: ImageView = mView.findViewById(R.id.ivPlay)
        val delete: ImageView = mView.findViewById(R.id.delete)
        val tvAuthor: TextView = mView.findViewById(R.id.tvAuthor)
        val tvBookName: TextView = mView.findViewById(R.id.tvBookName)
        val tvPublisher: TextView = mView.findViewById(R.id.tvPublisher)
        val tvNarrator: TextView = mView.findViewById(R.id.tvNarrator)
        val tvLength: TextView = mView.findViewById(R.id.tvLength)
        val tvLanguage: TextView = mView.findViewById(R.id.tvLanguage)
        val ratingBar: ScaleRatingBar = mView.findViewById(R.id.ratingBar)
        val tvPrice: TextView = mView.findViewById(R.id.tvPrice)

        init {
            mView.setOnClickListener(this)
        }

        override fun onClick(v: View) {
            mOnItemClickListener.onItemClicked(books[position])
        }
    }

    private lateinit var books: List<Book>

    private lateinit var mOnItemClickListener: onBookItemClickListener

    private lateinit var mDeleteListener: onBookItemClickListener.onCartBookDeleteClickListener

    var currentPlayListId: String = ""

    var IsPlaying: Boolean = false

    var isToShowDelete: Boolean = false

    private var context: Context? = null
}