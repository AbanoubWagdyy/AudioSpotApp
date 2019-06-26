package com.audiospotapplication.UI.books.Interface

import com.audiospot.DataLayer.Model.Book

interface onBookItemClickListener {

    fun onItemClicked(book: Book)
    fun onPlayClicked(book: Book)

    interface onCartBookDeleteClickListener {
        fun onItemDeleted(book: Book)
    }
}