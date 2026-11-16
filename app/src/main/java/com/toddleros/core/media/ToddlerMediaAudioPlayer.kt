package com.toddleros.core.media

import android.content.Context
import android.content.res.AssetFileDescriptor
import android.media.AudioAttributes
import android.media.MediaPlayer
import java.io.IOException

class ToddlerMediaAudioPlayer(private val context: Context) {

    private var mediaPlayer: MediaPlayer? = null
    private var isMuted: Boolean = false
    private var currentPlayingPath: String? = null
    var onCompletionListener: (() -> Unit)? = null

    fun setMuted(muted: Boolean) {
        isMuted = muted
        if (muted) {
            stop()
        }
    }

    fun isMuted(): Boolean = isMuted

    fun isPlaying(): Boolean = mediaPlayer?.isPlaying == true

    fun getCurrentPath(): String? = currentPlayingPath

    fun playAsset(assetPath: String, loop: Boolean = false, onComplete: (() -> Unit)? = null) {
        if (isMuted) return

        stop()

        try {
            val cleanPath = assetPath.removePrefix("file:///android_asset/").removePrefix("/")
            val afd: AssetFileDescriptor = context.assets.openFd(cleanPath)

            mediaPlayer = MediaPlayer().apply {
                setAudioAttributes(
                    AudioAttributes.Builder()
                        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                        .setUsage(AudioAttributes.USAGE_MEDIA)
                        .build()
                )
                setDataSource(afd.fileDescriptor, afd.startOffset, afd.length)
                afd.close()

                isLooping = loop
                setOnCompletionListener {
                    currentPlayingPath = null
                    onComplete?.invoke()
                    onCompletionListener?.invoke()
                }
                prepare()
                start()
            }
            currentPlayingPath = assetPath
        } catch (_: Exception) {
            // Graceful fallback for non-seekable compressed streams
            try {
                val cleanPath = assetPath.removePrefix("file:///android_asset/").removePrefix("/")
                val pfd = context.assets.open(cleanPath)
                pfd.close()
            } catch (_: IOException) {
                // Asset missing or not found
            }
        }
    }

    fun pause() {
        try {
            if (mediaPlayer?.isPlaying == true) {
                mediaPlayer?.pause()
            }
        } catch (_: Exception) {}
    }

    fun resume() {
        if (isMuted) return
        try {
            mediaPlayer?.start()
        } catch (_: Exception) {}
    }

    fun stop() {
        try {
            mediaPlayer?.stop()
            mediaPlayer?.reset()
            mediaPlayer?.release()
        } catch (_: Exception) {
        } finally {
            mediaPlayer = null
            currentPlayingPath = null
        }
    }

    fun release() {
        stop()
    }
}
