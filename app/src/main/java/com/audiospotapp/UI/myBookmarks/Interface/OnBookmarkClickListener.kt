package com.audiospotapp.UI.myBookmarks.Interface

import com.audiospot.DataLayer.Model.CategoriesListData
import com.audiospotapp.DataLayer.Model.Bookmark

interface OnBookmarkClickListener {

    fun onBookmarkClicked(bookmark: Bookmark)
}