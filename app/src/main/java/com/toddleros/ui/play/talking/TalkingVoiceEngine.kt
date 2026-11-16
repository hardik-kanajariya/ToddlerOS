package com.toddleros.ui.play.talking

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import java.util.Locale

class TalkingVoiceEngine(
    private val context: Context,
    private val onExpressionChange: (CharacterExpression) -> Unit,
    private val onSpeakingStateChange: (Boolean) -> Unit,
    private val onStatusMessageChange: (String) -> Unit
) {

    private var textToSpeech: TextToSpeech? = null
    private var speechRecognizer: SpeechRecognizer? = null
    private var isTtsReady: Boolean = false
    private var isListening: Boolean = false

    init {
        initTts()
    }

    private fun initTts() {
        textToSpeech = TextToSpeech(context) { status ->
            if (status == TextToSpeech.SUCCESS) {
                textToSpeech?.let { tts ->
                    tts.language = Locale.getDefault()
                    tts.setPitch(1.65f)
                    tts.setSpeechRate(1.15f)
                    tts.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
                        override fun onStart(utteranceId: String?) {
                            onSpeakingStateChange(true)
                            onExpressionChange(CharacterExpression.SPEAKING)
                        }

                        override fun onDone(utteranceId: String?) {
                            onSpeakingStateChange(false)
                            onExpressionChange(CharacterExpression.IDLE)
                        }

                        @Deprecated("Deprecated in Java")
                        override fun onError(utteranceId: String?) {
                            onSpeakingStateChange(false)
                            onExpressionChange(CharacterExpression.IDLE)
                        }

                        override fun onError(utteranceId: String?, errorCode: Int) {
                            onSpeakingStateChange(false)
                            onExpressionChange(CharacterExpression.IDLE)
                        }
                    })
                    isTtsReady = true
                }
            }
        }
    }

    fun startListening() {
        if (isListening) return

        stopSpeaking()

        val hasRecognizer = SpeechRecognizer.isRecognitionAvailable(context)
        if (!hasRecognizer) {
            onStatusMessageChange("Offline recognizer unavailable")
            speakReaction("I'm ready to play! Tap me to hear funny sounds!", CharacterExpression.HAPPY)
            return
        }

        try {
            speechRecognizer?.destroy()
            speechRecognizer = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S &&
                SpeechRecognizer.isOnDeviceRecognitionAvailable(context)
            ) {
                SpeechRecognizer.createOnDeviceSpeechRecognizer(context)
            } else {
                SpeechRecognizer.createSpeechRecognizer(context)
            }

            val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
                putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
                putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
                putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, false)
                putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1)
                putExtra(RecognizerIntent.EXTRA_PREFER_OFFLINE, true)
            }

            speechRecognizer?.setRecognitionListener(object : RecognitionListener {
                override fun onReadyForSpeech(params: Bundle?) {
                    isListening = true
                    onExpressionChange(CharacterExpression.LISTENING)
                    onStatusMessageChange("Listening to you, Vasu...")
                }

                override fun onBeginningOfSpeech() {
                    onStatusMessageChange("Hearing you...")
                }

                override fun onRmsChanged(rmsdB: Float) {}

                override fun onBufferReceived(buffer: ByteArray?) {}

                override fun onEndOfSpeech() {
                    isListening = false
                    onStatusMessageChange("Thinking...")
                }

                override fun onError(error: Int) {
                    isListening = false
                    handleSpeechError(error)
                }

                override fun onResults(results: Bundle?) {
                    isListening = false
                    val matches = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                    val spokenText = matches?.firstOrNull()?.trim()

                    if (!spokenText.isNullOrEmpty()) {
                        processSpokenInput(spokenText)
                    } else {
                        handleSpeechError(SpeechRecognizer.ERROR_NO_MATCH)
                    }
                }

                override fun onPartialResults(partialResults: Bundle?) {}

                override fun onEvent(eventType: Int, params: Bundle?) {}
            })

            speechRecognizer?.startListening(intent)
        } catch (_: Exception) {
            isListening = false
            handleSpeechError(SpeechRecognizer.ERROR_CLIENT)
        }
    }

    fun stopListening() {
        if (!isListening) return
        isListening = false
        try {
            speechRecognizer?.stopListening()
        } catch (_: Exception) {
        }
        onExpressionChange(CharacterExpression.IDLE)
        onStatusMessageChange("Tap mic to speak!")
    }

    private fun processSpokenInput(spokenText: String) {
        val lower = spokenText.lowercase(Locale.ROOT)

        when {
            lower.contains("hello") || lower.contains("hi") -> {
                speakReaction("Hello Vasu! I am so happy to play with you!", CharacterExpression.HAPPY)
            }
            lower.contains("love") -> {
                speakReaction("I love you too, Vasu! Purrrr!", CharacterExpression.HAPPY)
            }
            lower.contains("sing") || lower.contains("song") -> {
                speakReaction("La la la! Tra la la! We are making happy music!", CharacterExpression.HAPPY)
            }
            lower.contains("sleep") || lower.contains("night") || lower.contains("bed") -> {
                speakReaction("Yaaawn... Good night and sweet dreams, Vasu! Zzz...", CharacterExpression.SLEEPY)
            }
            lower.contains("funny") || lower.contains("silly") || lower.contains("joke") -> {
                speakReaction("Hahaha! You are so silly, Vasu!", CharacterExpression.LAUGHING)
            }
            lower.contains("dance") -> {
                speakReaction("Boing boing! Look at me wiggle and dance!", CharacterExpression.HAPPY)
            }
            else -> {
                // Repeat exactly what Vasu said in the funny child voice
                speakReaction(spokenText, CharacterExpression.SPEAKING)
            }
        }
    }

    private fun handleSpeechError(errorCode: Int) {
        when (errorCode) {
            SpeechRecognizer.ERROR_SPEECH_TIMEOUT,
            SpeechRecognizer.ERROR_NO_MATCH -> {
                onStatusMessageChange("I didn't hear anything!")
                speakReaction("Hmm, it's so quiet! Say something, Vasu!", CharacterExpression.CONFUSED)
            }
            else -> {
                onStatusMessageChange("Tap mic or tap me to play!")
                speakReaction("Hehe, that tickled my ears! Say it again?", CharacterExpression.CONFUSED)
            }
        }
    }

    fun onCharacterTapped(area: CharacterTapArea) {
        stopListening()
        when (area) {
            CharacterTapArea.HEAD -> {
                speakReaction("Purrrrr! Oh, that feels so nice, Vasu!", CharacterExpression.HAPPY)
            }
            CharacterTapArea.BELLY -> {
                speakReaction("Hahaha! Stop tickling my tummy, that tickles!", CharacterExpression.LAUGHING)
            }
            CharacterTapArea.NOSE -> {
                speakReaction("Achoo! Boop! You touched my nose!", CharacterExpression.SURPRISED)
            }
            CharacterTapArea.EARS -> {
                speakReaction("Boing! My ears are wiggling!", CharacterExpression.SURPRISED)
            }
        }
    }

    fun speakReaction(phrase: String, expression: CharacterExpression) {
        if (!isTtsReady) return
        stopListening()
        onStatusMessageChange(phrase)
        onExpressionChange(expression)
        textToSpeech?.speak(phrase, TextToSpeech.QUEUE_FLUSH, null, "talking_buddy_utterance")
    }

    fun stopSpeaking() {
        textToSpeech?.stop()
        onSpeakingStateChange(false)
    }

    fun release() {
        stopListening()
        stopSpeaking()
        try {
            speechRecognizer?.destroy()
        } catch (_: Exception) {
        }
        speechRecognizer = null
        textToSpeech?.shutdown()
        textToSpeech = null
        isTtsReady = false
    }
}
