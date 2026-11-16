package com.toddleros.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.toddleros.data.local.dao.MediaDao
import com.toddleros.data.local.dao.ProgressDao
import com.toddleros.data.local.dao.SettingsDao
import com.toddleros.data.local.entity.LearningProgressEntity
import com.toddleros.data.local.entity.MediaRecordEntity
import com.toddleros.data.local.entity.SettingEntity

@Database(
    entities = [
        MediaRecordEntity::class,
        SettingEntity::class,
        LearningProgressEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class ToddlerDatabase : RoomDatabase() {
    abstract fun mediaDao(): MediaDao
    abstract fun settingsDao(): SettingsDao
    abstract fun progressDao(): ProgressDao

    companion object {
        @Volatile
        private var instance: ToddlerDatabase? = null

        fun getInstance(context: Context): ToddlerDatabase {
            return instance ?: synchronized(this) {
                instance ?: Room.databaseBuilder(
                    context.applicationContext,
                    ToddlerDatabase::class.java,
                    "toddleros.db"
                ).build().also { instance = it }
            }
        }
    }
}
