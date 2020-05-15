package com.audiospotapplication.UI.cart

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.audiospot.DataLayer.Model.Book
import com.audiospotapplication.UI.BaseFragment

import com.audiospotapplication.R
import com.audiospotapplication.UI.BaseActivity
import com.audiospotapplication.UI.bookDetails.BookDetailsActivity
import com.audiospotapplication.UI.books.Interface.onBookItemClickListener
import com.audiospotapplication.UI.books.adapter.BooksAdapter
import com.audiospotapplication.UI.payment.PaymentActivity
import com.audiospotapplication.UI.promoCode.PromoCodeActivity
import com.audiospotapplication.utils.DialogUtils
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_cart.*

class CartFragment : BaseFragment(), CartContract.View, onBookItemClickListener,
    onBookItemClickListener.onCartBookDeleteClickListener {

    override fun showPaymentScreen() {
        val intent = Intent(requireActivity(), PaymentActivity::class.java)
        startActivity(intent)
        requireActivity().finish()
    }

    override fun setCartCount(size: Int) {
        (activity as BaseActivity).setCartNumber(size)
    }

    override fun showBookDetailsScreen() {
        val intent = Intent(requireActivity(), BookDetailsActivity::class.java)
        startActivity(intent)
    }

    override fun showMessage(message: String) {
        if (activity != null)
            Snackbar.make(
                requireActivity().findViewById(android.R.id.content),
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
        if (playBookSample(book) == R.drawable.ic_pause) {
            handlePlayPause()
        }
    }

    override fun getAppContext(): Context? {
        return requireActivity().applicationContext
    }

    override fun showErrorMessage() {
        if (activity != null)
            Snackbar.make(
                requireActivity().findViewById(android.R.id.content),
                requireActivity().applicationContext.getString(R.string.try_again), Snackbar.LENGTH_SHORT
            ).show()
    }

    override fun showLoading() {
        DialogUtils.showProgressDialog(requireActivity(), "Loading ...")
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

            adapter = BooksAdapter(
                listMyBooks, this,
                getPlaylistIdObserver().value!!, isPlaying,this
            )
            recyclerCartBooks.adapter = adapter

            getPlaylistIdObserver().observe(this, Observer {
                if (adapter != null && !it.equals(""))
                    adapter!!.updatePlaylistId(it, isPlaying)
            })

            getPlayingObserver().observe(this, Observer {
                if (adapter != null)
                    adapter!!.updatePlaylistId(getPlaylistIdObserver().value, it)
            })
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
            val intent = Intent(requireActivity(), PromoCodeActivity::class.java)
            startActivity(intent)
        }

        payNow.setOnClickListener {
            mPresenter.handlePayNowPressed()
        }

        mPresenter = CartPresenter(this)
        mPresenter.start()
    }

    fun onEditCartClicked() {
        adapter?.showDelete()
    }

    companion object {
        @JvmStatic
        fun newInstance(): CartFragment {
            return CartFragment()
        }
    }

    private lateinit var listMyBooks: List<Book>
    private var adapter: BooksAdapter? = null
    lateinit var mPresenter: CartContract.Presenter
}