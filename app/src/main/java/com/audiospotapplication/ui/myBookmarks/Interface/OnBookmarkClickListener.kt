package com.audiospotapplication.ui.myBookmarks.Interface

import com.audiospotapplication.data.model.Bookmark

interface OnBookmarkClickListener {

    fun onBookmarkClicked(bookmark: Bookmark)
}