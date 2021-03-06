package com.audiospotapplication.ui.categories

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import com.audiospot.DataLayer.Model.CategoriesListData
import com.audiospot.DataLayer.Model.CategoriesListResponse
import com.audiospotapplication.ui.BaseFragment
import com.audiospotapplication.R
import com.audiospotapplication.ui.books.BooksActivity
import com.audiospotapplication.ui.categories.Interface.OnCategoryItemClickListener
import com.audiospotapplication.ui.categories.adapter.CategoriesListAdapter
import com.audiospotapplication.utils.DialogUtils
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_categories.*

class CategoriesFragment(var ivArrow: ImageView) : BaseFragment(), CategoriesContract.View, OnCategoryItemClickListener {

    override fun showLoadingDialog() {
        DialogUtils.showProgressDialog(requireActivity(),"Loading ....")
    }

    override fun dismissLoadingDialog() {
        DialogUtils.dismissProgressDialog()
    }

    override fun showBooksScreen() {
        val intent = Intent(requireActivity(), BooksActivity::class.java)
        startActivity(intent)
    }

    override fun onCategoryItemClicked(categoryListData: CategoriesListData) {
        mPresenter.handleCategoryItemClicked(categoryListData)
    }

    override fun getAppContext(): Context? {
        return requireActivity().applicationContext
    }

    override fun setCategoriesList(result: CategoriesListResponse?) {
        recyclerCategories.layoutManager = LinearLayoutManager(context)
        recyclerCategories.setHasFixedSize(true)
        recyclerCategories.isNestedScrollingEnabled = false
        recyclerCategories.adapter = CategoriesListAdapter(result!!, this)
    }

    override fun showErrorMessage(message: String) {
        Snackbar.make(requireActivity().findViewById(android.R.id.content), message, Snackbar.LENGTH_SHORT).show()
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