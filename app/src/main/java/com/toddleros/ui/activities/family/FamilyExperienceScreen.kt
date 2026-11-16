package com.toddleros.ui.activities.family

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.toddleros.core.audio.ToddlerAudioEngine
import com.toddleros.core.audio.ToddlerTtsManager
import com.toddleros.core.theme.ToddlerPaper
import com.toddleros.domain.model.family.FamilyPhoto
import com.toddleros.domain.model.family.FamilyPhotoRepository
import com.toddleros.ui.activities.common.ToddlerTopBar
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun FamilyExperienceScreen(
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    val audioEngine = remember { ToddlerAudioEngine() }
    val ttsManager = remember { ToddlerTtsManager(context) }
    var isMuted by remember { mutableStateOf(false) }

    var selectedMode by remember { mutableStateOf(FamilyMode.ALBUM) }
    var spotlightPhoto by remember { mutableStateOf<FamilyPhoto?>(null) }

    val photos = remember { FamilyPhotoRepository.allPhotos }

    DisposableEffect(Unit) {
        onDispose {
            audioEngine.release()
            ttsManager.release()
        }
    }

    fun playGreeting(photo: FamilyPhoto) {
        audioEngine.playPianoNote(587.33f, 400) // D5 bell chime
        coroutineScope.launch {
            delay(150L)
            ttsManager.speakPlayful(photo.spokenGreeting)
        }
    }

    fun onPeekabooSpotRevealed(photo: FamilyPhoto) {
        audioEngine.playPeekabooChime()
        coroutineScope.launch {
            delay(200L)
            ttsManager.speakPlayful("Peekaboo! ${photo.spokenGreeting}")
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(ToddlerPaper)
    ) {
        // Top Navigation Bar
        ToddlerTopBar(
            title = "My Family ❤️",
            onBack = onBack,
            isMuted = isMuted,
            onToggleMute = {
                isMuted = !isMuted
                audioEngine.setMuted(isMuted)
                ttsManager.setMuted(isMuted)
            }
        )

        // Mode Switcher (Album, Peekaboo, Explorer)
        FamilyModeSwitcher(
            selectedMode = selectedMode,
            onModeSelected = { selectedMode = it }
        )

        // Active Mode Content
        Box(modifier = Modifier.weight(1f)) {
            when (selectedMode) {
                FamilyMode.ALBUM -> {
                    FamilyAlbumView(
                        photos = photos,
                        onPhotoClick = { photo ->
                            spotlightPhoto = photo
                            playGreeting(photo)
                        }
                    )
                }

                FamilyMode.PEEKABOO -> {
                    FamilyPeekabooView(
                        photos = photos,
                        onSpotRevealed = { photo ->
                            onPeekabooSpotRevealed(photo)
                        }
                    )
                }

                FamilyMode.EXPLORER -> {
                    FamilyPhotoExplorerView(
                        photos = photos,
                        onPhotoClick = { photo ->
                            spotlightPhoto = photo
                            playGreeting(photo)
                        },
                        onPhotoChanged = { photo ->
                            playGreeting(photo)
                        }
                    )
                }
            }
        }
    }

    // Prominent Spotlight Overlay on Photo Tap
    spotlightPhoto?.let { photo ->
        FamilySpotlightDialog(
            photo = photo,
            onDismiss = { spotlightPhoto = null },
            onReplayAudio = { playGreeting(photo) }
        )
    }
}
