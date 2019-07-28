package com.audiospotapplication.UI.authors

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.audiospotapplication.DataLayer.Model.AuthorsResponse
import com.audiospotapplication.R
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_authors.*
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.audiospotapplication.DataLayer.Model.AuthorsData
import com.audiospotapplication.UI.authorDetails.AuthorDetailsActivity
import com.audiospotapplication.UI.authors.Adapter.AuthorsListAdapter
import com.audiospotapplication.UI.authors.Interface.OnAuthorsItemClickListener
import com.audiospotapplication.utils.DialogUtils


class AuthorsFragment(var ivArrow: ImageView) : Fragment(), AuthorsContract.View, OnAuthorsItemClickListener {
    override fun showLoading() {
        DialogUtils.showProgressDialog(activity!!,"Loading ....")
    }

    override fun dismissLoading() {
        DialogUtils.dismissProgressDialog()
    }

    override fun showAuthorDetailsScreen() {
        val intent = Intent(activity!!, AuthorDetailsActivity::class.java)
        startActivity(intent)
    }

    override fun onAuthorItemClicked(authorsData: AuthorsData) {
        mPresenter.handleAuthorItemClicked(authorsData)
    }

    override fun setAuthorsList(authorsResponse: AuthorsResponse?) {
        recyclerAuthors.layoutManager = GridLayoutManager(activity!!, 2) as RecyclerView.LayoutManager?
        recyclerAuthors.setHasFixedSize(true)
        recyclerAuthors.isNestedScrollingEnabled = false
        recyclerAuthors.adapter = AuthorsListAdapter(authorsResponse!!, this)
    }

    override fun getAppContext(): Context? {
        return activity!!.applicationContext
    }

    override fun showErrorMessage(message: String) {
        Snackbar.make(activity!!.findViewById(android.R.id.content), message, Snackbar.LENGTH_SHORT).show()
    }

    lateinit var mPresenter: AuthorsContract.Presenter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_authors, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mPresenter = AuthorsPresenter(this)
        mPresenter.start()
    }

    companion object {
        @JvmStatic
        fun newInstance(ivArrow: ImageView) =
            AuthorsFragment(ivArrow)
    }
}