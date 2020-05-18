package com.audiospotapplication.ui.search

import com.audiospotapplication.data.DataRepository
import com.audiospotapplication.data.RepositorySource

class SearchPresenter(val mView: SearchContract.View) : SearchContract.Presenter {

    lateinit var mRepository: RepositorySource

    override fun start() {
        mRepository = DataRepository.getInstance(mView.getActivity().applicationContext)
    }
}