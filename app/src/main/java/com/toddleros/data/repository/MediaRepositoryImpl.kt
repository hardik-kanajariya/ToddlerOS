package com.toddleros.data.repository

import com.toddleros.data.local.dao.MediaDao
import com.toddleros.data.local.entity.MediaRecordEntity
import com.toddleros.domain.model.MediaItem
import com.toddleros.domain.model.MediaType
import com.toddleros.domain.repository.MediaRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class MediaRepositoryImpl(
    private val mediaDao: MediaDao
) : MediaRepository {

    override fun getMediaItems(type: MediaType): Flow<List<MediaItem>> {
        return mediaDao.getMediaByType(type.name).map { entities ->
            entities.map { entity ->
                MediaItem(
                    id = entity.id,
                    uriString = entity.uriString,
                    title = entity.title,
                    type = type,
                    durationMs = entity.durationMs,
                    lastPositionMs = entity.lastPositionMs,
                    artistOrAuthor = entity.artistOrAuthor,
                    thumbnailUri = entity.thumbnailUri
                )
            }
        }
    }

    override suspend fun addMediaItem(item: MediaItem): Long = withContext(Dispatchers.IO) {
        val entity = MediaRecordEntity(
            uriString = item.uriString,
            title = item.title,
            mediaType = item.type.name,
            durationMs = item.durationMs,
            lastPositionMs = item.lastPositionMs,
            artistOrAuthor = item.artistOrAuthor,
            thumbnailUri = item.thumbnailUri
        )
        mediaDao.insertMedia(entity)
    }

    override suspend fun savePlaybackPosition(id: Long, positionMs: Long) = withContext(Dispatchers.IO) {
        mediaDao.updatePlaybackPosition(id, positionMs)
    }

    override suspend fun deleteMediaItem(id: Long) = withContext(Dispatchers.IO) {
        mediaDao.deleteById(id)
    }

    override suspend fun clearMedia(type: MediaType) = withContext(Dispatchers.IO) {
        mediaDao.clearByType(type.name)
    }
}
