package com.toddleros.domain.repository

import kotlinx.coroutines.flow.Flow

interface ProgressRepository {
    fun getTotalStars(): Flow<Int>
    suspend fun recordCompletion(activityId: String, stars: Int = 1)
}
