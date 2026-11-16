package com.toddleros.domain.repository

import com.toddleros.domain.model.MediaItem
import com.toddleros.domain.model.MediaType
import kotlinx.coroutines.flow.Flow

interface MediaRepository {
    fun getMediaItems(type: MediaType): Flow<List<MediaItem>>
    suspend fun addMediaItem(item: MediaItem): Long
    suspend fun savePlaybackPosition(id: Long, positionMs: Long)
    suspend fun deleteMediaItem(id: Long)
    suspend fun clearMedia(type: MediaType)
}
