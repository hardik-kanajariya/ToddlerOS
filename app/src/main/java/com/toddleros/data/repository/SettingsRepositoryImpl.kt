package com.toddleros.data.repository

import com.toddleros.data.local.dao.SettingsDao
import com.toddleros.data.local.entity.SettingEntity
import com.toddleros.domain.repository.SettingsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class SettingsRepositoryImpl(
    private val settingsDao: SettingsDao
) : SettingsRepository {

    override fun getString(key: String, defaultValue: String): Flow<String> {
        return settingsDao.getSettingFlow(key).map { it ?: defaultValue }
    }

    override suspend fun setString(key: String, value: String) = withContext(Dispatchers.IO) {
        settingsDao.setSetting(SettingEntity(key, value))
    }

    override fun getBoolean(key: String, defaultValue: Boolean): Flow<Boolean> {
        return settingsDao.getSettingFlow(key).map { it?.toBooleanStrictOrNull() ?: defaultValue }
    }

    override suspend fun setBoolean(key: String, value: Boolean) = withContext(Dispatchers.IO) {
        settingsDao.setSetting(SettingEntity(key, value.toString()))
    }
}
