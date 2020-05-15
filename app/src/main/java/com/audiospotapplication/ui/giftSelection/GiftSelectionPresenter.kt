package com.audiospotapplication.ui.giftSelection

import com.audiospot.DataLayer.Model.Book
import com.audiospotapplication.data.DataRepository
import com.visionvalley.letuno.DataLayer.RepositorySource

class GiftSelectionPresenter(val mView: GiftSelectionContract.View) : GiftSelectionContract.Presenter {

    override fun handleSubmitClicked(giftSelection: GiftSelection, quantity: Int) {
        mRepositorySource.submitGiftProperities(giftSelection, quantity)
        when (giftSelection) {
            GiftSelection.AUDIOSPOT_ACCOUNT -> {
                mView.showGiftSCreen()
            }

            GiftSelection.VOUCHER -> {
                mView.showPayment(mRepositorySource.getSavedBook()!!.id, quantity)
            }
        }
    }

    override fun start() {
        mRepositorySource = DataRepository.getInstance(mView.getAppContext()!!)
        var bookDetailsData = mRepositorySource.getSavedBook()
        mView.bindResponse(bookDetailsData!!)
    }

    lateinit var mRepositorySource: RepositorySource
    lateinit var bookDetails: Book
}