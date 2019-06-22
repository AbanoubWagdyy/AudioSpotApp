package com.audiospotapp.UI.bookChapters.Interface

import com.audiospotapp.DataLayer.Model.ChaptersData

interface OnChapterCLickListener {

    fun onChapterClicked(data: ChaptersData)
}