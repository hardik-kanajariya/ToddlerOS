package com.toddleros.domain.model

data class MediaItem(
    val id: Long = 0,
    val uriString: String,
    val title: String,
    val type: MediaType,
    val durationMs: Long = 0L,
    val lastPositionMs: Long = 0L,
    val artistOrAuthor: String? = null,
    val thumbnailUri: String? = null
)
