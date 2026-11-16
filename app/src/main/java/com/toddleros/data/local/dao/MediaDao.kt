package com.toddleros.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.toddleros.data.local.entity.MediaRecordEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MediaDao {
    @Query("SELECT * FROM media_records WHERE mediaType = :type ORDER BY title ASC")
    fun getMediaByType(type: String): Flow<List<MediaRecordEntity>>

    @Query("SELECT * FROM media_records WHERE uriString = :uri LIMIT 1")
    suspend fun getMediaByUri(uri: String): MediaRecordEntity?

    @Query("SELECT * FROM media_records WHERE id = :id LIMIT 1")
    suspend fun getMediaById(id: Long): MediaRecordEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMedia(media: MediaRecordEntity): Long

    @Query("UPDATE media_records SET lastPositionMs = :position WHERE id = :id")
    suspend fun updatePlaybackPosition(id: Long, position: Long)

    @Query("DELETE FROM media_records WHERE id = :id")
    suspend fun deleteById(id: Long)

    @Query("DELETE FROM media_records WHERE mediaType = :type")
    suspend fun clearByType(type: String)
}
