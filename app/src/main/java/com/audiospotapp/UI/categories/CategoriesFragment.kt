package com.audiospotapp.UI.categories

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.audiospot.DataLayer.Model.CategoriesListData
import com.audiospot.DataLayer.Model.CategoriesListResponse
import com.audiospotapp.R
import com.audiospotapp.UI.books.BooksActivity
import com.audiospotapp.UI.categories.Interface.OnCategoryItemClickListener
import com.audiospotapp.UI.categories.adapter.CategoriesListAdapter
import com.audiospotapp.utils.DialogUtils
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_categories.*

class CategoriesFragment(var ivArrow: ImageView) : Fragment(), CategoriesContract.View, OnCategoryItemClickListener {

    override fun showLoadingDialog() {
        DialogUtils.showProgressDialog(activity!!,"Loading ....")
    }

    override fun dismissLoadingDialog() {
        DialogUtils.dismissProgressDialog()
    }

    override fun showBooksScreen() {
        val intent = Intent(activity!!, BooksActivity::class.java)
        startActivity(intent)
    }

    override fun onCategoryItemClicked(categoryListData: CategoriesListData) {
        mPresenter.handleCategoryItemClicked(categoryListData)
    }

    override fun getAppContext(): Context? {
        return activity!!.applicationContext
    }

    override fun setCategoriesList(result: CategoriesListResponse?) {
        recyclerCategories.layoutManager = LinearLayoutManager(context)
        recyclerCategories.setHasFixedSize(true)
        recyclerCategories.isNestedScrollingEnabled = false
        recyclerCategories.adapter = CategoriesListAdapter(result!!, this)
    }

    override fun showErrorMessage(message: String) {
        Snackbar.make(activity!!.findViewById(android.R.id.content), message, Snackbar.LENGTH_SHORT).show()
    }

    lateinit var mPresenter: CategoriesContract.Presenter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_categories, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ivArrow.setOnClickListener {
            Log.d("Arrow pressed", "Arrow pressed")
        }
        mPresenter = CategoriesPresenter(this)
        mPresenter.start()
    }

    companion object {
        @JvmStatic
        fun newInstance(ivArrow: ImageView) =
            CategoriesFragment(ivArrow)
    }
}