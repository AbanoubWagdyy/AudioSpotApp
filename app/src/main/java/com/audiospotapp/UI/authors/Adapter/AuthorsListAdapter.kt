package com.audiospotapp.UI.authors.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.audiospot.DataLayer.Model.CategoriesListResponse
import com.audiospotapp.DataLayer.Model.AuthorsResponse
import com.audiospotapp.R
import com.audiospotapp.UI.authors.Interface.OnAuthorsItemClickListener
import com.audiospotapp.UI.categories.Interface.OnCategoryItemClickListener
import com.audiospotapp.utils.ImageUtils

class AuthorsListAdapter(
    private var response: AuthorsResponse,
    private var mOnItemClickListener: OnAuthorsItemClickListener
) : RecyclerView.Adapter<AuthorsListAdapter.ViewHolder>() {

    private var context: Context? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.author_list_view_item, parent, false)
        context = view.context
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return response.data.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = response.data[position]
        ImageUtils.setImageFromUrlIntoImageViewUsingPicasso(data.photo, context, holder.ivAuthor, false)
        holder.tvAuthor.text = data.name
    }

    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView), View.OnClickListener {

        val tvAuthor: TextView = mView.findViewById(R.id.tvAuthor)
        val ivAuthor: ImageView = mView.findViewById(R.id.ivAuthor)

        init {
            mView.setOnClickListener(this)
        }

        override fun onClick(v: View) {
            mOnItemClickListener.onAuthorItemClicked(response.data!![position])
        }
    }

}