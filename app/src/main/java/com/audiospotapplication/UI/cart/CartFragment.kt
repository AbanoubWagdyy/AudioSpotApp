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
import androidx.recyclerview.widget.RecyclerView
import com.audiospot.DataLayer.Model.Book
import com.audiospotapplication.BaseFragment
import com.audiospotapplication.DataLayer.Retrofit.GlobalKeys

import com.audiospotapplication.R
import com.audiospotapplication.UI.BaseActivity
import com.audiospotapplication.UI.bookDetails.BookDetailsActivity
import com.audiospotapplication.UI.books.Interface.onBookItemClickListener
import com.audiospotapplication.UI.books.adapter.BooksAdapter
import com.audiospotapplication.UI.homepage.HomepageActivity
import com.audiospotapplication.UI.homepage.home.adapter.HomepageAdapter
import com.audiospotapplication.UI.payment.PaymentActivity
import com.audiospotapplication.UI.promoCode.PromoCodeActivity
import com.audiospotapplication.utils.BookDataConversion
import com.audiospotapplication.utils.DialogUtils
import com.example.jean.jcplayer.JcPlayerManager
import com.example.jean.jcplayer.JcPlayerManagerListener
import com.example.jean.jcplayer.general.JcStatus
import com.example.jean.jcplayer.model.JcAudio
import com.google.android.material.snackbar.Snackbar
import dm.audiostreamer.MediaMetaData
import kotlinx.android.synthetic.main.fragment_cart.*

class CartFragment : BaseFragment(), CartContract.View, onBookItemClickListener,
    onBookItemClickListener.onCartBookDeleteClickListener, JcPlayerManagerListener {

    override fun showPaymentScreen() {
        val intent = Intent(activity!!, PaymentActivity::class.java)
        startActivity(intent)
        activity!!.finish()
    }

    override fun setCartCount(size: Int) {
        (activity as BaseActivity).setCartNumber(size)
    }

    override fun showBookDetailsScreen() {
        val intent = Intent(activity!!, BookDetailsActivity::class.java)
        startActivity(intent)
    }

    override fun onPreparedAudio(status: JcStatus) {

    }

    override fun onCompletedAudio() {

    }

    override fun onPaused(status: JcStatus) {

    }

    override fun onContinueAudio(status: JcStatus) {

    }

    override fun onPlaying(status: JcStatus) {

    }

    override fun onTimeChanged(status: JcStatus) {

    }

    override fun onStopped(status: JcStatus) {

    }

    override fun onJcpError(throwable: Throwable) {

    }

    override fun showMessage(message: String) {
        if (activity != null)
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
        mPresenter.saveBook(book)
    }

    override fun onPlayClicked(book: Book) {
        var currentAudio = jcPlayerManager.currentAudio
        if (jcPlayerManager.isPlaying()) {
            if (currentAudio?.path.equals(book.sample)) {
                jcPlayerManager.pauseAudio()
                adapter = BooksAdapter(listMyBooks, this, this)
                recyclerCartBooks.adapter = adapter
                return
            } else {
                playAudio(book)
            }
        } else {
            if (currentAudio != null && currentAudio?.path.equals(book.sample)) {
                jcPlayerManager.continueAudio()
                adapter = BooksAdapter(
                    listMyBooks, this, this,
                    jcPlayerManager.currentAudio
                )
                recyclerCartBooks.adapter = adapter
            } else {
                playAudio(book)
            }
        }
    }

    private fun playAudio(book: Book) {

        if (book == null || book.sample == null || book.sample.equals("")) {
            if (activity != null)
                Snackbar.make(
                    activity!!.findViewById(android.R.id.content), "Audio is not available right now ," +
                            "please check again later", Snackbar.LENGTH_LONG
                ).show()

            adapter = BooksAdapter(listMyBooks, this, this)
            recyclerCartBooks.adapter = adapter
            return
        }

        jcPlayerManager.kill()

        var audio = JcAudio.createFromURL(
            book.id, book.title,
            book.sample, null
        )
        var playlist = ArrayList<JcAudio>()
        playlist.add(audio)

        jcPlayerManager.playlist = playlist
        jcPlayerManager.jcPlayerManagerListener = this

        jcPlayerManager.playAudio(audio)
        jcPlayerManager.createNewNotification(R.mipmap.ic_launcher)
        adapter = BooksAdapter(
            listMyBooks, this, this,
            jcPlayerManager.currentAudio
        )
        recyclerCartBooks.adapter = adapter
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
        if (activity != null)
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
        this.listMyBooks = listMyBooks
        if (listMyBooks.isEmpty()) {
            promoCode.visibility = View.GONE
            payNow.visibility = View.GONE
            recyclerCartBooks.visibility = View.GONE
        } else {
            recyclerCartBooks.layoutManager = LinearLayoutManager(context)
            recyclerCartBooks.setHasFixedSize(true)
            recyclerCartBooks.isNestedScrollingEnabled = false
            adapter = if (jcPlayerManager.isPlaying()) {
                BooksAdapter(
                    listMyBooks, this, this,
                    jcPlayerManager.currentAudio
                )
            } else {
                BooksAdapter(listMyBooks, this, this)
            }
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
            mPresenter.handlePayNowPressed()
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

    private lateinit var listMyBooks: List<Book>
    private lateinit var adapter: BooksAdapter
    lateinit var mPresenter: CartContract.Presenter
    private val jcPlayerManager: JcPlayerManager by lazy {
        JcPlayerManager.getInstance(activity!!.applicationContext).get()!!
    }
}
