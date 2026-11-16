package com.toddleros.data.repository

import com.toddleros.data.local.dao.ProgressDao
import com.toddleros.data.local.entity.LearningProgressEntity
import com.toddleros.domain.repository.ProgressRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class ProgressRepositoryImpl(
    private val progressDao: ProgressDao
) : ProgressRepository {

    override fun getTotalStars(): Flow<Int> {
        return progressDao.getTotalStarsFlow().map { it ?: 0 }
    }

    override suspend fun recordCompletion(activityId: String, stars: Int) = withContext(Dispatchers.IO) {
        val existing = progressDao.getProgress(activityId)
        if (existing != null) {
            progressDao.addStars(activityId, stars, System.currentTimeMillis())
        } else {
            progressDao.insertOrUpdate(
                LearningProgressEntity(
                    activityId = activityId,
                    starsEarned = stars,
                    lastPlayedTimestamp = System.currentTimeMillis()
                )
            )
        }
    }
}
