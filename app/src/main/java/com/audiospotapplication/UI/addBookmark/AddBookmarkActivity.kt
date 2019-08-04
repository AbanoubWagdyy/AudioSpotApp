package com.audiospotapplication.UI.addBookmark

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.audiospotapplication.R
import com.audiospotapplication.UI.login.LoginActivity
import com.audiospotapplication.utils.DialogUtils
import com.audiospotapplication.utils.ImageUtils
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_add_bookmark.*
import kotlinx.android.synthetic.main.back.*
import kotlinx.android.synthetic.main.header.*

class AddBookmarkActivity : AppCompatActivity(), AddBookmarkContract.View {

    override fun showLoginPage() {
        mPresenter.resetRepo()
        val intent = Intent(this, LoginActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or
                Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)
        finish()
    }

    override fun showMessage(message: String) {
        Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_SHORT).show()
    }

    override fun finalizeView() {
        Handler().postDelayed({
            setResult(Activity.RESULT_OK)
            finish()
        }, 2000)
    }

    override fun getAppContext(): Context? {
        return applicationContext
    }

    override fun showLoading() {
        DialogUtils.showProgressDialog(this, "Adding ....")
    }

    override fun dismissLoading() {
        DialogUtils.dismissProgressDialog()
    }

    override fun setBookName(bookNameStr: String) {
        bookName.text = bookNameStr
    }

    override fun setBookImage(bookName: String) {
        ImageUtils.setImageFromUrlIntoImageViewUsingPicasso(bookName, applicationContext, ivBook, false)
    }

    override fun setChapterName(chapterNameStr: String) {
        chapterName.text = chapterNameStr
    }

    override fun setBookmarkTime(bookmarkTimeStr: String) {
        bookmarkTime.text = bookmarkTimeStr
    }

    lateinit var mPresenter: AddBookmarkContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_bookmark)

        tvTitle.text = "Add Bookmark"

        back.setOnClickListener {
            setResult(Activity.RESULT_CANCELED)
            finish()
        }

        addBookmark.setOnClickListener {
            mPresenter.addBookmark(comment.text.toString())
        }

        mPresenter = AddBookmarkPresenter(this)
        mPresenter.start()
    }
}
