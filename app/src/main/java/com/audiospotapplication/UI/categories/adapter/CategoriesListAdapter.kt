package com.audiospotapplication.UI.categories.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.audiospot.DataLayer.Model.CategoriesListResponse
import com.audiospotapplication.R
import com.audiospotapplication.UI.categories.Interface.OnCategoryItemClickListener

class CategoriesListAdapter(
    private var response: CategoriesListResponse,
    private var mOnItemClickListener: OnCategoryItemClickListener
) : RecyclerView.Adapter<CategoriesListAdapter.ViewHolder>() {

    private var context: Context? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.category_list_view_item, parent, false)
        context = view.context
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return response.data.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = response.data[position]
        holder.tvCategory.text = data.title
    }

    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView), View.OnClickListener {
        val tvCategory: TextView = mView.findViewById(R.id.tvCategory)

        init {
            mView.setOnClickListener(this)
        }

        override fun onClick(v: View) {
            mOnItemClickListener.onCategoryItemClicked(response.data!![position])
        }
    }

}