package com.audiospotapp.UI.myBookmarks.adapter

import android.content.Context
import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.audiospotapp.DataLayer.Model.Bookmark
import com.audiospotapp.R
import com.audiospotapp.UI.myBookmarks.Interface.OnBookmarkClickListener
import com.audiospotapp.utils.TimeUtils
import java.text.SimpleDateFormat
import java.util.*

class MyBookmarksAdapter(
    private var bookmarks: List<Bookmark>,
    private var mOnItemClickListener: OnBookmarkClickListener
) : RecyclerView.Adapter<MyBookmarksAdapter.ViewHolder>() {

    private var context: Context? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.bookmark_list_item, parent, false)
        context = view.context
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return bookmarks.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var bookmark = bookmarks[position]

        holder.bookmarkTime.text = TimeUtils.toTimeFormat(bookmark.time)

        holder.comment.text = bookmark.comment
        holder.bookName.text = ""
        holder.chapterName.text = ""
    }

    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView), View.OnClickListener {
        val comment: TextView = mView.findViewById(R.id.comment)
        val bookName: TextView = mView.findViewById(R.id.bookName)
        val chapterName: TextView = mView.findViewById(R.id.chapterName)
        val bookmarkTime: TextView = mView.findViewById(R.id.bookmarkTime)

        init {
            mView.setOnClickListener(this)
        }

        override fun onClick(v: View) {
            mOnItemClickListener.onBookmarkClicked(bookmarks[position])
        }
    }
}