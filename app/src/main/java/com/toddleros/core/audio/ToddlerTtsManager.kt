package com.toddleros.core.audio

import android.content.Context
import android.speech.tts.TextToSpeech
import java.util.Locale

/**
 * Manages offline Text-To-Speech with toddler-tailored audio parameters (friendly pitch & pacing).
 */
class ToddlerTtsManager(context: Context, onReady: (() -> Unit)? = null) {

    private var tts: TextToSpeech? = null
    private var isReady = false
    private var isMuted = false

    init {
        tts = TextToSpeech(context.applicationContext) { status ->
            if (status == TextToSpeech.SUCCESS) {
                tts?.apply {
                    language = Locale.getDefault()
                    // Cheerful, child-friendly high pitch and clear cadence
                    setPitch(1.3f)
                    setSpeechRate(0.9f)
                }
                isReady = true
                onReady?.invoke()
            }
        }
    }

    fun setMuted(muted: Boolean) {
        isMuted = muted
        if (muted) {
            stop()
        }
    }

    fun speak(text: String, queueMode: Int = TextToSpeech.QUEUE_FLUSH) {
        if (isMuted || !isReady) return
        tts?.speak(text, queueMode, null, "toddler_voice_${System.currentTimeMillis()}")
    }

    fun speakPlayful(text: String) {
        if (isMuted || !isReady) return
        tts?.setPitch(1.45f)
        tts?.speak(text, TextToSpeech.QUEUE_FLUSH, null, "playful_${System.currentTimeMillis()}")
        tts?.setPitch(1.3f)
    }

    fun stop() {
        tts?.stop()
    }

    fun release() {
        tts?.stop()
        tts?.shutdown()
        tts = null
        isReady = false
    }
}
