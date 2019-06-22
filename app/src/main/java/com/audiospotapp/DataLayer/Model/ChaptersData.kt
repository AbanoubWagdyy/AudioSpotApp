package com.audiospotapp.DataLayer.Model

data class ChaptersData(
    val id: Int,
    val title: String,
    val duration: Long,
    val sound_file: String,
    val paragraphs: List<Paragraph>
)