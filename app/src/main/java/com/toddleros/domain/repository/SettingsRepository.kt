package com.toddleros.domain.repository

import kotlinx.coroutines.flow.Flow

interface SettingsRepository {
    fun getString(key: String, defaultValue: String): Flow<String>
    suspend fun setString(key: String, value: String)
    fun getBoolean(key: String, defaultValue: Boolean): Flow<Boolean>
    suspend fun setBoolean(key: String, value: Boolean)
}
