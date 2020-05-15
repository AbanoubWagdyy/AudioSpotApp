package com.audiospotapplication.ui

import com.ps.pexoplayer.model.PexoMediaMetadata

interface onItemPlayClickListener {
    fun OnItemPlayed(mediaData: PexoMediaMetadata)
}