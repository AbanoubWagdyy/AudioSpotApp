package com.audiospotapplication.UI.myBookmarks.Interface

import com.audiospotapplication.DataLayer.Model.Bookmark

interface OnBookmarkClickListener {

    fun onBookmarkClicked(bookmark: Bookmark)
}