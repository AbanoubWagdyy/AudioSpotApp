package com.audiospotapplication.data.model

data class ChaptersData(
    val id: Int,
    val title: String,
    val duration: Long,
    val duration_str: String,
    val sound_file: String,
    val paragraphs: List<Paragraph>
)