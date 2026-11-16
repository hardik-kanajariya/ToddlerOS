package com.toddleros.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.toddleros.data.local.entity.LearningProgressEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ProgressDao {
    @Query("SELECT * FROM learning_progress")
    fun getAllProgress(): Flow<List<LearningProgressEntity>>

    @Query("SELECT SUM(starsEarned) FROM learning_progress")
    fun getTotalStarsFlow(): Flow<Int?>

    @Query("SELECT * FROM learning_progress WHERE activityId = :activityId LIMIT 1")
    suspend fun getProgress(activityId: String): LearningProgressEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdate(progress: LearningProgressEntity)

    @Query("UPDATE learning_progress SET starsEarned = starsEarned + :stars, lastPlayedTimestamp = :timestamp WHERE activityId = :activityId")
    suspend fun addStars(activityId: String, stars: Int, timestamp: Long)
}
