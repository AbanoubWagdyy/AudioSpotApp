package com.audiospotapplication.UI.cart

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.audiospot.DataLayer.Model.Book

import com.audiospotapplication.R
import com.audiospotapplication.UI.books.Interface.onBookItemClickListener
import com.audiospotapplication.UI.books.adapter.BooksAdapter
import com.audiospotapplication.UI.homepage.HomepageActivity
import com.audiospotapplication.UI.payment.PaymentActivity
import com.audiospotapplication.UI.promoCode.PromoCodeActivity
import com.audiospotapplication.utils.BookMediaDataConversion
import com.audiospotapplication.utils.DialogUtils
import com.google.android.material.snackbar.Snackbar
import dm.audiostreamer.AudioStreamingManager
import dm.audiostreamer.MediaMetaData
import kotlinx.android.synthetic.main.fragment_cart.*

class CartFragment : Fragment(), CartContract.View, onBookItemClickListener,
    onBookItemClickListener.onCartBookDeleteClickListener {

    override fun showMessage(message: String) {
        Snackbar.make(
            activity!!.findViewById(android.R.id.content),
            message,
            Snackbar.LENGTH_SHORT
        ).show()
    }

    override fun onItemDeleted(book: Book) {
        mPresenter.deleteBookFromCart(book)
    }

    override fun onItemClicked(book: Book) {
        var mediaMetaData = BookMediaDataConversion.convertBookToMediaMetaData(book)
        listOfSongs.add(mediaMetaData)
        playSong(mediaMetaData)
    }

    private fun playSong(mediaMetaData: MediaMetaData) {
        if (streamingManager != null) {
            streamingManager!!.onPlay(mediaMetaData)
        }
    }

    override fun onPlayClicked(book: Book) {
        configAudioStreamer()
    }

    private fun configAudioStreamer() {
        streamingManager = AudioStreamingManager.getInstance(activity!!.applicationContext)
        //Set PlayMultiple 'true' if want to playing sequentially one by one songs
        // and provide the list of songs else set it 'false'
        streamingManager!!.isPlayMultiple = mPresenter.getAuthResponse() != null

        streamingManager!!.setMediaList(listOfSongs)

        //If you want to show the Player Notification then set ShowPlayerNotification as true
        //and provide the pending intent so that after click on notification it will redirect to an activity

        streamingManager!!.setShowPlayerNotification(true)
        streamingManager!!.setPendingIntentAct(getNotificationPendingIntent())
    }

    private fun getNotificationPendingIntent(): PendingIntent {
        val intent = Intent(activity!!.applicationContext, HomepageActivity::class.java)
        intent.setAction("openplayer")
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        return PendingIntent.getActivity(activity!!.applicationContext, 0, intent, 0)
    }

    override fun getAppContext(): Context? {
        return activity!!.applicationContext
    }

    override fun showErrorMessage() {
        Snackbar.make(
            activity!!.findViewById(android.R.id.content),
            activity!!.applicationContext.getString(R.string.try_again), Snackbar.LENGTH_SHORT
        ).show()
    }

    override fun showLoading() {
        DialogUtils.showProgressDialog(activity!!, "Loading ...")
    }

    override fun dismissLoading() {
        DialogUtils.dismissProgressDialog()
    }

    override fun setBookList(listMyBooks: List<Book>) {
        if (listMyBooks.isEmpty()) {
            promoCode.visibility = View.GONE
            payNow.visibility = View.GONE
            recyclerCartBooks.visibility = View.GONE
        } else {
            recyclerCartBooks.layoutManager = LinearLayoutManager(context)
            recyclerCartBooks.setHasFixedSize(true)
            recyclerCartBooks.isNestedScrollingEnabled = false
            adapter = BooksAdapter(listMyBooks, this, this)
            recyclerCartBooks.adapter = adapter
        }

        var priceTotal = 0
        for (book in listMyBooks) {
            priceTotal += book.price
        }

        totalPayment.text = "Total payment: $priceTotal LE"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_cart, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        promoCode.setOnClickListener {
            val intent = Intent(activity!!, PromoCodeActivity::class.java)
            startActivity(intent)
        }

        payNow.setOnClickListener {
            val intent = Intent(activity!!, PaymentActivity::class.java)
            startActivity(intent)
            activity!!.finish()
        }

        mPresenter = CartPresenter(this)
        mPresenter.start()
    }

    fun onEditCartClicked() {
        adapter.showDeleteIcon()
    }

    companion object {
        @JvmStatic
        fun newInstance(): CartFragment {
            return CartFragment()
        }
    }

    private lateinit var adapter: BooksAdapter
    lateinit var mPresenter: CartContract.Presenter
    private var streamingManager: AudioStreamingManager? = null
    private var listOfSongs: MutableList<MediaMetaData> = ArrayList()
}
