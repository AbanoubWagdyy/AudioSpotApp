package com.audiospotapp.UI.cart

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.audiospot.DataLayer.Model.Book

import com.audiospotapp.R
import com.audiospotapp.UI.books.Interface.onBookItemClickListener
import com.audiospotapp.UI.books.adapter.BooksAdapter
import com.audiospotapp.utils.DialogUtils
import com.google.android.material.snackbar.Snackbar
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
    }

    override fun onPlayClicked(book: Book) {
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
        recyclerCartBooks.layoutManager = LinearLayoutManager(context)
        recyclerCartBooks.setHasFixedSize(true)
        recyclerCartBooks.isNestedScrollingEnabled = false
        adapter = BooksAdapter(listMyBooks, this, this)
        recyclerCartBooks.adapter = adapter
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_cart, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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
}
