package com.audiospotapplication.UI

import com.ps.pexoplayer.model.PexoMediaMetadata

interface onItemPlayClickListener {
    fun OnItemPlayed(mediaData: PexoMediaMetadata)
}