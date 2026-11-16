package com.toddleros.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "media_records")
data class MediaRecordEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val uriString: String,
    val title: String,
    val mediaType: String,
    val durationMs: Long = 0L,
    val lastPositionMs: Long = 0L,
    val artistOrAuthor: String? = null,
    val thumbnailUri: String? = null,
    val addedTimestamp: Long = System.currentTimeMillis()
)
