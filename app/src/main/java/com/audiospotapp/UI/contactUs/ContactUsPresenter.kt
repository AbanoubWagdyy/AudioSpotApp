package com.audiospotapp.UI.contactUs

import com.audiospotapp.DataLayer.DataRepository
import com.visionvalley.letuno.DataLayer.RepositorySource

class ContactUsPresenter(val mView: ContactUsContract.View) : ContactUsContract.Presenter {

    override fun contactUs(email: String, subject: String, message: String) {

    }

    override fun start() {
        mRepositorySource = DataRepository.getInstance(mView.getAppContext())
    }

    lateinit var mRepositorySource: RepositorySource
}