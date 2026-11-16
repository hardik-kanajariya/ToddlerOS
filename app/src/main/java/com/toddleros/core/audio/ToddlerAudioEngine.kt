package com.toddleros.core.audio

import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioTrack
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlin.math.PI
import kotlin.math.exp
import kotlin.math.sin
import kotlin.random.Random

/**
 * Lightweight, zero-latency on-device synthesized audio engine for ToddlerOS.
 * Provides instant sound feedback for touches, musical notes, and toy interactions
 * completely offline without external network dependencies.
 */
class ToddlerAudioEngine {

    private val scope = CoroutineScope(Dispatchers.Default + SupervisorJob())
    private val sampleRate = 44100
    private var isMuted = false

    fun setMuted(muted: Boolean) {
        isMuted = muted
    }

    fun isMuted(): Boolean = isMuted

    /**
     * Plays a musical note at a given frequency with soft attack and decay (bell/piano envelope).
     */
    fun playPianoNote(frequency: Float, durationMs: Int = 500) {
        if (isMuted) return
        scope.launch {
            val numSamples = (sampleRate * (durationMs / 1000.0)).toInt()
            val buffer = ShortArray(numSamples)

            for (i in 0 until numSamples) {
                val t = i.toDouble() / sampleRate
                val progress = i.toDouble() / numSamples
                // Envelope: quick attack (5ms), exponential decay
                val envelope = when {
                    progress < 0.02 -> progress / 0.02
                    else -> exp(-progress * 3.5)
                }

                // Fundamental + soft harmonics for warm acoustic tone
                val tone1 = sin(2.0 * PI * frequency * t)
                val tone2 = 0.35 * sin(2.0 * PI * (frequency * 2) * t)
                val tone3 = 0.15 * sin(2.0 * PI * (frequency * 3) * t)

                val sample = ((tone1 + tone2 + tone3) * envelope * 24000.0).toInt()
                buffer[i] = sample.coerceIn(-32768, 32767).toShort()
            }

            playPcmBuffer(buffer)
        }
    }

    /**
     * Bubble pop sound: quick downward frequency sweep with crisp click.
     */
    fun playBubblePop() {
        if (isMuted) return
        scope.launch {
            val durationMs = 80
            val numSamples = (sampleRate * (durationMs / 1000.0)).toInt()
            val buffer = ShortArray(numSamples)

            for (i in 0 until numSamples) {
                val progress = i.toDouble() / numSamples
                val currentFreq = 950.0 - (progress * 650.0)
                val envelope = (1.0 - progress) * (1.0 - progress)
                val sample = (sin(2.0 * PI * currentFreq * (i.toDouble() / sampleRate)) * envelope * 28000.0).toInt()
                buffer[i] = sample.coerceIn(-32768, 32767).toShort()
            }

            playPcmBuffer(buffer)
        }
    }

    /**
     * Balloon pop sound: short snappy burst with gentle decay.
     */
    fun playBalloonPop() {
        if (isMuted) return
        scope.launch {
            val durationMs = 90
            val numSamples = (sampleRate * (durationMs / 1000.0)).toInt()
            val buffer = ShortArray(numSamples)

            for (i in 0 until numSamples) {
                val progress = i.toDouble() / numSamples
                val envelope = exp(-progress * 9.0)
                val noise = (Random.nextDouble() * 2.0 - 1.0)
                val tone = sin(2.0 * PI * 180.0 * (i.toDouble() / sampleRate))
                val sample = ((noise * 0.7 + tone * 0.3) * envelope * 30000.0).toInt()
                buffer[i] = sample.coerceIn(-32768, 32767).toShort()
            }

            playPcmBuffer(buffer)
        }
    }

    /**
     * Peekaboo chime: happy ascending arpeggio (C5 -> E5 -> G5).
     */
    fun playPeekabooChime() {
        if (isMuted) return
        scope.launch {
            val durationMs = 280
            val numSamples = (sampleRate * (durationMs / 1000.0)).toInt()
            val buffer = ShortArray(numSamples)

            val notes = listOf(523.25, 659.25, 783.99)
            val segmentSamples = numSamples / 3

            for (i in 0 until numSamples) {
                val noteIndex = (i / segmentSamples).coerceAtMost(2)
                val freq = notes[noteIndex]
                val localProgress = (i % segmentSamples).toDouble() / segmentSamples
                val envelope = exp(-localProgress * 2.5)

                val tone = sin(2.0 * PI * freq * (i.toDouble() / sampleRate))
                val sample = (tone * envelope * 24000.0).toInt()
                buffer[i] = sample.coerceIn(-32768, 32767).toShort()
            }

            playPcmBuffer(buffer)
        }
    }

    /**
     * Cheerful percussion: bass kick, snare click, or shaker sound.
     */
    fun playPercussion(type: PercussionType) {
        if (isMuted) return
        scope.launch {
            val durationMs = when (type) {
                PercussionType.KICK -> 160
                PercussionType.CLAP -> 120
                PercussionType.SHAKER -> 90
            }
            val numSamples = (sampleRate * (durationMs / 1000.0)).toInt()
            val buffer = ShortArray(numSamples)

            for (i in 0 until numSamples) {
                val progress = i.toDouble() / numSamples
                val envelope = exp(-progress * 7.0)

                val sample = when (type) {
                    PercussionType.KICK -> {
                        val freq = 140.0 - (progress * 90.0)
                        (sin(2.0 * PI * freq * (i.toDouble() / sampleRate)) * envelope * 30000.0).toInt()
                    }
                    PercussionType.CLAP -> {
                        val noise = (Random.nextDouble() * 2.0 - 1.0)
                        (noise * envelope * 26000.0).toInt()
                    }
                    PercussionType.SHAKER -> {
                        val noise = (Random.nextDouble() * 2.0 - 1.0)
                        val highFreq = sin(2.0 * PI * 3200.0 * (i.toDouble() / sampleRate))
                        ((noise * 0.8 + highFreq * 0.2) * envelope * 20000.0).toInt()
                    }
                }
                buffer[i] = sample.coerceIn(-32768, 32767).toShort()
            }

            playPcmBuffer(buffer)
        }
    }

    /**
     * Synthesized acoustic animal sound motifs for instant offline feedback.
     */
    fun playAnimalAcousticSound(animalId: String) {
        if (isMuted) return
        scope.launch {
            when (animalId.lowercase()) {
                "dog" -> playDogBark()
                "cat" -> playCatMeow()
                "cow" -> playCowMoo()
                "lion" -> playLionRoar()
                "duck" -> playDuckQuack()
                "elephant" -> playElephantTrumpet()
                "sheep" -> playSheepBaa()
                "monkey" -> playMonkeyChatter()
                else -> playBubblePop()
            }
        }
    }

    private fun playDogBark() {
        val numSamples = (sampleRate * 0.28).toInt()
        val buffer = ShortArray(numSamples)
        for (i in 0 until numSamples) {
            val progress = i.toDouble() / numSamples
            val envelope = if (progress < 0.1) progress / 0.1 else exp(-progress * 6.0)
            val freq = 260.0 - (progress * 70.0)
            val tone = sin(2.0 * PI * freq * (i.toDouble() / sampleRate))
            val noise = (Random.nextDouble() * 2.0 - 1.0) * 0.3
            val sample = ((tone + noise) * envelope * 28000.0).toInt()
            buffer[i] = sample.coerceIn(-32768, 32767).toShort()
        }
        playPcmBuffer(buffer)
    }

    private fun playCatMeow() {
        val numSamples = (sampleRate * 0.45).toInt()
        val buffer = ShortArray(numSamples)
        for (i in 0 until numSamples) {
            val progress = i.toDouble() / numSamples
            val envelope = sin(progress * PI)
            // Pitch rises then falls: 480 -> 680 -> 420
            val freq = 480.0 + (sin(progress * PI) * 200.0)
            val tone = sin(2.0 * PI * freq * (i.toDouble() / sampleRate))
            val overtone = 0.3 * sin(4.0 * PI * freq * (i.toDouble() / sampleRate))
            val sample = ((tone + overtone) * envelope * 24000.0).toInt()
            buffer[i] = sample.coerceIn(-32768, 32767).toShort()
        }
        playPcmBuffer(buffer)
    }

    private fun playCowMoo() {
        val numSamples = (sampleRate * 0.65).toInt()
        val buffer = ShortArray(numSamples)
        for (i in 0 until numSamples) {
            val progress = i.toDouble() / numSamples
            val envelope = sin(progress * PI)
            val freq = 135.0 - (progress * 25.0)
            val tone = sin(2.0 * PI * freq * (i.toDouble() / sampleRate))
            val overtone = 0.4 * sin(4.0 * PI * freq * (i.toDouble() / sampleRate))
            val sample = ((tone + overtone) * envelope * 27000.0).toInt()
            buffer[i] = sample.coerceIn(-32768, 32767).toShort()
        }
        playPcmBuffer(buffer)
    }

    private fun playLionRoar() {
        val numSamples = (sampleRate * 0.55).toInt()
        val buffer = ShortArray(numSamples)
        for (i in 0 until numSamples) {
            val progress = i.toDouble() / numSamples
            val envelope = sin(progress * PI)
            val freq = 110.0 + (sin(progress * 15.0) * 15.0)
            val tone = sin(2.0 * PI * freq * (i.toDouble() / sampleRate))
            val noise = (Random.nextDouble() * 2.0 - 1.0) * 0.4
            val sample = ((tone + noise) * envelope * 29000.0).toInt()
            buffer[i] = sample.coerceIn(-32768, 32767).toShort()
        }
        playPcmBuffer(buffer)
    }

    private fun playDuckQuack() {
        val numSamples = (sampleRate * 0.3).toInt()
        val buffer = ShortArray(numSamples)
        for (i in 0 until numSamples) {
            val progress = i.toDouble() / numSamples
            val envelope = exp(-progress * 4.0)
            val freq = 310.0 + (sin(progress * 28.0) * 45.0)
            val tone = sin(2.0 * PI * freq * (i.toDouble() / sampleRate))
            val sample = (tone * envelope * 26000.0).toInt()
            buffer[i] = sample.coerceIn(-32768, 32767).toShort()
        }
        playPcmBuffer(buffer)
    }

    private fun playElephantTrumpet() {
        val numSamples = (sampleRate * 0.48).toInt()
        val buffer = ShortArray(numSamples)
        for (i in 0 until numSamples) {
            val progress = i.toDouble() / numSamples
            val envelope = sin(progress * PI)
            val freq = 320.0 + (progress * 280.0) + (sin(progress * 30.0) * 20.0)
            val tone = sin(2.0 * PI * freq * (i.toDouble() / sampleRate))
            val overtone = 0.5 * sin(4.0 * PI * freq * (i.toDouble() / sampleRate))
            val sample = ((tone + overtone) * envelope * 28000.0).toInt()
            buffer[i] = sample.coerceIn(-32768, 32767).toShort()
        }
        playPcmBuffer(buffer)
    }

    private fun playSheepBaa() {
        val numSamples = (sampleRate * 0.45).toInt()
        val buffer = ShortArray(numSamples)
        for (i in 0 until numSamples) {
            val progress = i.toDouble() / numSamples
            val envelope = sin(progress * PI)
            val vibrato = sin(progress * 24.0 * PI) * 20.0
            val freq = 240.0 + vibrato
            val tone = sin(2.0 * PI * freq * (i.toDouble() / sampleRate))
            val overtone = 0.3 * sin(4.0 * PI * freq * (i.toDouble() / sampleRate))
            val sample = ((tone + overtone) * envelope * 26000.0).toInt()
            buffer[i] = sample.coerceIn(-32768, 32767).toShort()
        }
        playPcmBuffer(buffer)
    }

    private fun playMonkeyChatter() {
        val numSamples = (sampleRate * 0.35).toInt()
        val buffer = ShortArray(numSamples)
        for (i in 0 until numSamples) {
            val progress = i.toDouble() / numSamples
            val pulse = sin(progress * 8.0 * PI).let { if (it > 0) 1.0 else 0.2 }
            val freq = 620.0 + (sin(progress * 12.0) * 120.0)
            val tone = sin(2.0 * PI * freq * (i.toDouble() / sampleRate))
            val sample = (tone * pulse * 25000.0).toInt()
            buffer[i] = sample.coerceIn(-32768, 32767).toShort()
        }
        playPcmBuffer(buffer)
    }

    private fun playPcmBuffer(buffer: ShortArray) {
        try {
            val minBufSize = AudioTrack.getMinBufferSize(
                sampleRate,
                AudioFormat.CHANNEL_OUT_MONO,
                AudioFormat.ENCODING_PCM_16BIT
            )
            val bufferSize = maxOf(buffer.size * 2, minBufSize)

            val track = AudioTrack.Builder()
                .setAudioAttributes(
                    AudioAttributes.Builder()
                        .setUsage(AudioAttributes.USAGE_GAME)
                        .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                        .build()
                )
                .setAudioFormat(
                    AudioFormat.Builder()
                        .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
                        .setSampleRate(sampleRate)
                        .setChannelMask(AudioFormat.CHANNEL_OUT_MONO)
                        .build()
                )
                .setBufferSizeInBytes(bufferSize)
                .setTransferMode(AudioTrack.MODE_STATIC)
                .build()

            track.write(buffer, 0, buffer.size)
            track.play()

            // Release after playback finishes
            track.setNotificationMarkerPosition(buffer.size)
            track.setPlaybackPositionUpdateListener(object : AudioTrack.OnPlaybackPositionUpdateListener {
                override fun onMarkerReached(t: AudioTrack?) {
                    t?.stop()
                    t?.release()
                }

                override fun onPeriodicNotification(t: AudioTrack?) {}
            })
        } catch (_: Exception) {
            // Audio hardware safety
        }
    }

    fun release() {
        scope.cancel()
    }
}

enum class PercussionType {
    KICK,
    CLAP,
    SHAKER
}
