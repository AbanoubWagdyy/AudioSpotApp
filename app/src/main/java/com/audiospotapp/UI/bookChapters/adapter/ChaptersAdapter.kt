package com.audiospotapp.UI.bookChapters.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.audiospotapp.DataLayer.Model.ChaptersData
import com.audiospotapp.R
import com.audiospotapp.UI.bookChapters.Interface.OnChapterCLickListener
import com.audiospotapp.utils.TimeUtils

class ChaptersAdapter(
    private var data: List<ChaptersData>,
    private var mListener: OnChapterCLickListener
) : RecyclerView.Adapter<ChaptersAdapter.ViewHolder>() {

    private var context: Context? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.chapter_list_item, parent, false)
        context = view.context
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var chapterData = data[position]
        holder.chapterName.text = chapterData.title
        holder.chapterDuration.text = TimeUtils.toHours(chapterData)
    }

    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView), View.OnClickListener {
        val chapterName: TextView = mView.findViewById(R.id.chapterName)
        val chapterDuration: TextView = mView.findViewById(R.id.chapterDuration)

        init {
            mView.setOnClickListener(this)
        }

        override fun onClick(v: View) {
            mListener.onChapterClicked(data!![position])
        }
    }
}