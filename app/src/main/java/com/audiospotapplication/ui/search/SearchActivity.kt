package com.audiospotapplication.ui.search

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.audiospotapplication.R


class SearchActivity : AppCompatActivity(), SearchContract.View {

    override fun getActivity(): AppCompatActivity {
        return this@SearchActivity
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        mPresenter = SearchPresenter(this)
        mPresenter.start()
    }

    lateinit var mPresenter: SearchContract.Presenter
}