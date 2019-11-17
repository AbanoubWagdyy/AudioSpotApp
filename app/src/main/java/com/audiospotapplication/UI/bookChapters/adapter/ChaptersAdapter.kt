package com.audiospotapplication.UI.bookChapters.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.audiospotapplication.DataLayer.Model.ChaptersData
import com.audiospotapplication.R
import com.audiospotapplication.UI.bookChapters.Interface.OnChapterCLickListener
import com.audiospotapplication.utils.TimeUtils
import com.snatik.storage.Storage
import java.io.File

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
        val chapterData = data[position]
        holder.chapterName.text = chapterData.title
        holder.chapterDuration.text = TimeUtils.toTimeFormat(chapterData.duration.toInt())

        if (validateChapterDownloaded(chapterData))
            holder.download.visibility = View.GONE
        else
            holder.download.visibility = View.VISIBLE
    }

    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView), View.OnClickListener {
        val chapterName: TextView = mView.findViewById(R.id.chapterName)
        val chapterDuration: TextView = mView.findViewById(R.id.chapterDuration)
        val download: ImageView = mView.findViewById(R.id.download)

        init {
            chapterName.setOnClickListener(this)
            chapterDuration.setOnClickListener(this)
            download.setOnClickListener(this)
        }

        override fun onClick(v: View) {
            if (v == download) {
                mListener.onChapterClicked(data!![position], position)
            } else {
                mListener.onItemDownloadPressed(data!![position])
            }
        }
    }

    fun validateChapterDownloaded(data: ChaptersData): Boolean {
        val storage = Storage(context)
        val path = storage.internalCacheDirectory

        val newDir = path + File.separator + "AudioSpotDownloadsCache"

        val fileNameStr = data.id

        return storage.isFileExist("$newDir/$fileNameStr")
    }
}