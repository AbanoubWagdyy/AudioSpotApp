package com.audiospotapplication.UI.publishers

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.audiospotapplication.DataLayer.Model.PublishersResponse
import com.audiospotapplication.DataLayer.Model.PublishersResponseData
import com.audiospotapplication.R
import com.audiospotapplication.UI.publisherDetails.PublisherDetailsActivity
import com.audiospotapplication.UI.publishers.Adapter.PublishersListAdapter
import com.audiospotapplication.UI.publishers.Interface.OnPublishersItemClickListener
import com.audiospotapplication.utils.DialogUtils
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_publishers.*

class PublishersFragment(var ivArrow: ImageView) : Fragment(), PublishersContract.View, OnPublishersItemClickListener {

    override fun showLoading() {
        DialogUtils.showProgressDialog(activity!!,"Loading ....")
    }

    override fun dismissLoading() {
        DialogUtils.dismissProgressDialog()
    }

    override fun showPublishersDetailsScreen() {
        val intent = Intent(activity!!, PublisherDetailsActivity::class.java)
        startActivity(intent)
    }

    override fun onPublisherItemClicked(data: PublishersResponseData) {
        mPresenter.handlePublisherItemClicked(data)
    }

    override fun setPublishersList(result: PublishersResponse?) {
        recyclerPublishers.layoutManager = GridLayoutManager(activity!!, 2)
        recyclerPublishers.setHasFixedSize(true)
        recyclerPublishers.isNestedScrollingEnabled = false
        recyclerPublishers.adapter = PublishersListAdapter(result!!, this)
    }

    override fun getAppContext(): Context? {
        return activity!!.applicationContext
    }

    override fun showErrorMessage(message: String) {
        Snackbar.make(activity!!.findViewById(android.R.id.content), message, Snackbar.LENGTH_SHORT).show()
    }

    lateinit var mPresenter: PublishersContract.Presenter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_publishers, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mPresenter = PublishersPresenter(this)
        mPresenter.start()
    }

    companion object {
        @JvmStatic
        fun newInstance(ivArrow: ImageView) =
            PublishersFragment(ivArrow)
    }
}