package com.toddleros

import android.app.Application
import com.toddleros.data.local.ToddlerDatabase
import com.toddleros.data.repository.MediaRepositoryImpl
import com.toddleros.data.repository.ProgressRepositoryImpl
import com.toddleros.data.repository.SettingsRepositoryImpl
import com.toddleros.domain.repository.MediaRepository
import com.toddleros.domain.repository.ProgressRepository
import com.toddleros.domain.repository.SettingsRepository
import com.toddleros.kiosk.KioskController

class ToddlerApp : Application() {

    lateinit var database: ToddlerDatabase
        private set

    lateinit var mediaRepository: MediaRepository
        private set

    lateinit var settingsRepository: SettingsRepository
        private set

    lateinit var progressRepository: ProgressRepository
        private set

    lateinit var kioskController: KioskController
        private set

    override fun onCreate() {
        super.onCreate()
        instance = this

        database = ToddlerDatabase.getInstance(this)
        mediaRepository = MediaRepositoryImpl(database.mediaDao())
        settingsRepository = SettingsRepositoryImpl(database.settingsDao())
        progressRepository = ProgressRepositoryImpl(database.progressDao())
        kioskController = KioskController(this)
    }

    companion object {
        lateinit var instance: ToddlerApp
            private set
    }
}
