package com.toddleros

import android.app.Application
import coil.ImageLoader
import coil.ImageLoaderFactory
import coil.decode.SvgDecoder
import coil.decode.VideoFrameDecoder
import com.toddleros.kiosk.KioskController

class ToddlerApp : Application(), ImageLoaderFactory {

    lateinit var kioskController: KioskController
        private set

    override fun onCreate() {
        super.onCreate()
        instance = this

        kioskController = KioskController(this)
    }

    override fun newImageLoader(): ImageLoader {
        return ImageLoader.Builder(this)
            .components {
                add(SvgDecoder.Factory())
                add(VideoFrameDecoder.Factory())
            }
            .crossfade(true)
            .build()
    }

    companion object {
        lateinit var instance: ToddlerApp
            private set
    }
}
