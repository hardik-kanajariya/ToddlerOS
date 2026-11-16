package com.toddleros.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "learning_progress")
data class LearningProgressEntity(
    @PrimaryKey val activityId: String,
    val starsEarned: Int = 0,
    val lastPlayedTimestamp: Long = System.currentTimeMillis()
)
