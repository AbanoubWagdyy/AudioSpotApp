package com.audiospotapplication.UI.homepage.home.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.audiospot.DataLayer.Model.Book
import com.audiospot.DataLayer.Model.HomepageRepsonse
import com.audiospotapplication.R
import com.audiospotapplication.UI.books.Interface.onBookItemClickListener
import com.audiospotapplication.utils.BookDataConversion
import com.example.jean.jcplayer.model.JcAudio

class HomepageAdapter(
    private var response: HomepageRepsonse?,
    private var mOnItemClickListener: onBookItemClickListener,
    currentSong: JcAudio?
) : RecyclerView.Adapter<HomepageAdapter.ViewHolder>(), onBookItemClickListener {


    private var context: Context? = null
    private var currentSong: JcAudio? = null

    init {
        this.currentSong = currentSong
    }

    override fun onItemClicked(book: Book) {
        mOnItemClickListener.onItemClicked(book)
    }

    override fun onPlayClicked(book: Book) {
        currentSong = BookDataConversion.convertBookToJcAudio(book)
        notifyDataSetChanged()
        mOnItemClickListener.onPlayClicked(book)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.homepage_list_view_item, parent, false)
        context = view.context
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var homepageData = response!!.data[position]
        holder.tvTitle.text = homepageData.title

        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        holder.recyclerBooks.layoutManager = layoutManager
        holder.recyclerBooks.setHasFixedSize(true)
        holder.recyclerBooks.isNestedScrollingEnabled = false
        holder.recyclerBooks.adapter = HorizontalBooksAdapter(homepageData.books, this, currentSong)
    }

    override fun getItemCount(): Int {
        return response!!.data.size
    }

    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        val tvTitle: TextView = mView.findViewById(R.id.tvTitle)
        val recyclerBooks: RecyclerView = mView.findViewById(R.id.recyclerBooks)
    }
}