package com.audiospotapp.UI

import dm.audiostreamer.MediaMetaData

interface onItemPlayClickListener {
    fun OnItemPlayed(mediaData: MediaMetaData)
}