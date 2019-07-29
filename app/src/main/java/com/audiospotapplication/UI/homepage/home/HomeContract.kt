package com.audiospotapplication.UI.homepage.home

import androidx.appcompat.app.AppCompatActivity
import com.audiospot.DataLayer.Model.Book
import com.audiospot.DataLayer.Model.HomepageRepsonse
import com.audiospotapplication.BaseView

interface HomeContract {

    interface Presenter {
        fun start()
        fun saveBook(book: Book)
    }

    interface View : BaseView{
        fun getContainingActivity() : AppCompatActivity
        fun showErrorMessage(message: String)
        fun showDialog()
        fun hideDialog()
        fun setHomeResponse(result: HomepageRepsonse?)
        fun showBookDetailsScreen()
        fun setCartNumber(size: Int)
    }
}