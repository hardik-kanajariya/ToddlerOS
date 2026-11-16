package com.toddleros.launcher

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.toddleros.ToddlerApp
import com.toddleros.core.theme.ToddlerOSTheme
import com.toddleros.core.theme.ToddlerPaper
import com.toddleros.ui.activities.animals.AnimalSoundsScreen
import com.toddleros.ui.activities.balloons.MagicBalloonsScreen
import com.toddleros.ui.activities.bubbles.BubblePopScreen
import com.toddleros.ui.activities.dancing.DancingCharactersScreen
import com.toddleros.ui.activities.family.FamilyExperienceScreen
import com.toddleros.ui.activities.fruits.FruitFriendsScreen
import com.toddleros.ui.activities.peekaboo.PeekabooScreen
import com.toddleros.ui.activities.piano.BabyPianoScreen
import com.toddleros.ui.activities.talking.TalkingCharacterScreen
import com.toddleros.ui.content.images.PhotoGalleryScreen
import com.toddleros.ui.content.music.MusicLibraryScreen
import com.toddleros.ui.content.stories.StoriesScreen
import com.toddleros.ui.content.videos.VideoTheaterScreen
import com.toddleros.ui.home.HomeScreen
import com.toddleros.ui.learning.AlphabetAdventureScreen
import com.toddleros.ui.learning.AnimalWorldScreen
import com.toddleros.ui.learning.ColorShapeScreen
import com.toddleros.ui.learning.CreativeStudioScreen
import com.toddleros.ui.learning.EducationalExplorerScreen
import com.toddleros.ui.learning.NumberGardenScreen
import com.toddleros.ui.learning.VehiclesScreen

enum class LauncherScreen {
    HOME,
    ANIMAL_SOUNDS,
    FRUIT_FRIENDS,
    BUBBLE_POP,
    PEEKABOO,
    MAGIC_BALLOONS,
    BABY_PIANO,
    DANCING_CHARACTERS,
    TALKING_CHARACTER,
    MY_FAMILY,
    ALPHABET_ADVENTURE,
    NUMBER_GARDEN,
    COLOR_SHAPE,
    VEHICLES,
    MUSIC_LIBRARY,
    CREATIVE_STUDIO,
    STORIES,
    PHOTO_GALLERY,
    ANIMAL_WORLD,
    EDUCATIONAL_EXPLORER,
    VIDEO_THEATER
}

class MainActivity : ComponentActivity() {

    private var isExiting = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val app = application as ToddlerApp

        setContent {
            ToddlerOSTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = ToddlerPaper
                ) {
                    var currentScreen by rememberSaveable { mutableStateOf(LauncherScreen.HOME) }
                    var isChildModeActive by rememberSaveable {
                        mutableStateOf(app.kioskController.isKioskActive(this@MainActivity))
                    }

                    // Automatically engage screen pinning / Lock Task mode on application start
                    LaunchedEffect(Unit) {
                        if (!isExiting && !app.kioskController.isKioskActive(this@MainActivity)) {
                            app.kioskController.startChildKioskMode(this@MainActivity)
                            isChildModeActive = app.kioskController.isKioskActive(this@MainActivity)
                        }
                    }

                    // Back press: returns to HOME if inside any activity
                    BackHandler(enabled = currentScreen != LauncherScreen.HOME) {
                        currentScreen = LauncherScreen.HOME
                    }

                    when (currentScreen) {
                        LauncherScreen.HOME -> {
                            HomeScreen(
                                isChildModeActive = isChildModeActive,
                                onExitChildMode = {
                                    isExiting = true
                                    if (app.kioskController.isKioskActive(this@MainActivity)) {
                                        app.kioskController.stopChildKioskMode(this@MainActivity)
                                        isChildModeActive = false
                                    }
                                    finish()
                                },
                                onOpenAnimalSounds = { currentScreen = LauncherScreen.ANIMAL_SOUNDS },
                                onOpenFruitFriends = { currentScreen = LauncherScreen.FRUIT_FRIENDS },
                                onOpenBubblePop = { currentScreen = LauncherScreen.BUBBLE_POP },
                                onOpenPeekaboo = { currentScreen = LauncherScreen.PEEKABOO },
                                onOpenMagicBalloons = { currentScreen = LauncherScreen.MAGIC_BALLOONS },
                                onOpenBabyPiano = { currentScreen = LauncherScreen.BABY_PIANO },
                                onOpenDancingCharacters = { currentScreen = LauncherScreen.DANCING_CHARACTERS },
                                onOpenTalkingCharacter = { currentScreen = LauncherScreen.TALKING_CHARACTER },
                                onOpenMyFamily = { currentScreen = LauncherScreen.MY_FAMILY },
                                onOpenColorsShapes = { currentScreen = LauncherScreen.COLOR_SHAPE },
                                onOpenAlphabet = { currentScreen = LauncherScreen.ALPHABET_ADVENTURE },
                                onOpenNumbers = { currentScreen = LauncherScreen.NUMBER_GARDEN },
                                onOpenVehicles = { currentScreen = LauncherScreen.VEHICLES },
                                onOpenMusic = { currentScreen = LauncherScreen.MUSIC_LIBRARY },
                                onOpenCreativeStudio = { currentScreen = LauncherScreen.CREATIVE_STUDIO },
                                onOpenStories = { currentScreen = LauncherScreen.STORIES },
                                onOpenPhotos = { currentScreen = LauncherScreen.PHOTO_GALLERY },
                                onOpenAnimalWorld = { currentScreen = LauncherScreen.ANIMAL_WORLD },
                                onOpenEducationalExplorer = { currentScreen = LauncherScreen.EDUCATIONAL_EXPLORER },
                                onOpenVideoTheater = { currentScreen = LauncherScreen.VIDEO_THEATER }
                            )
                        }

                        LauncherScreen.ANIMAL_SOUNDS -> {
                            AnimalSoundsScreen(
                                onBack = { currentScreen = LauncherScreen.HOME }
                            )
                        }

                        LauncherScreen.FRUIT_FRIENDS -> {
                            FruitFriendsScreen(
                                onBack = { currentScreen = LauncherScreen.HOME }
                            )
                        }

                        LauncherScreen.BUBBLE_POP -> {
                            BubblePopScreen(
                                onBack = { currentScreen = LauncherScreen.HOME }
                            )
                        }

                        LauncherScreen.PEEKABOO -> {
                            PeekabooScreen(
                                onBack = { currentScreen = LauncherScreen.HOME }
                            )
                        }

                        LauncherScreen.MAGIC_BALLOONS -> {
                            MagicBalloonsScreen(
                                onBack = { currentScreen = LauncherScreen.HOME }
                            )
                        }

                        LauncherScreen.BABY_PIANO -> {
                            BabyPianoScreen(
                                onBack = { currentScreen = LauncherScreen.HOME }
                            )
                        }

                        LauncherScreen.DANCING_CHARACTERS -> {
                            DancingCharactersScreen(
                                onBack = { currentScreen = LauncherScreen.HOME }
                            )
                        }

                        LauncherScreen.TALKING_CHARACTER -> {
                            TalkingCharacterScreen(
                                onBack = { currentScreen = LauncherScreen.HOME }
                            )
                        }

                        LauncherScreen.MY_FAMILY -> {
                            FamilyExperienceScreen(
                                onBack = { currentScreen = LauncherScreen.HOME }
                            )
                        }

                        LauncherScreen.ALPHABET_ADVENTURE -> {
                            AlphabetAdventureScreen(
                                onBack = { currentScreen = LauncherScreen.HOME }
                            )
                        }

                        LauncherScreen.NUMBER_GARDEN -> {
                            NumberGardenScreen(
                                onBack = { currentScreen = LauncherScreen.HOME }
                            )
                        }

                        LauncherScreen.COLOR_SHAPE -> {
                            ColorShapeScreen(
                                onBack = { currentScreen = LauncherScreen.HOME }
                            )
                        }

                        LauncherScreen.VEHICLES -> {
                            VehiclesScreen(
                                onBack = { currentScreen = LauncherScreen.HOME }
                            )
                        }

                        LauncherScreen.MUSIC_LIBRARY -> {
                            MusicLibraryScreen(
                                onBack = { currentScreen = LauncherScreen.HOME }
                            )
                        }

                        LauncherScreen.CREATIVE_STUDIO -> {
                            CreativeStudioScreen(
                                onBack = { currentScreen = LauncherScreen.HOME }
                            )
                        }

                        LauncherScreen.STORIES -> {
                            StoriesScreen(
                                onBack = { currentScreen = LauncherScreen.HOME }
                            )
                        }

                        LauncherScreen.PHOTO_GALLERY -> {
                            PhotoGalleryScreen(
                                onBack = { currentScreen = LauncherScreen.HOME }
                            )
                        }

                        LauncherScreen.ANIMAL_WORLD -> {
                            AnimalWorldScreen(
                                onBack = { currentScreen = LauncherScreen.HOME }
                            )
                        }

                        LauncherScreen.EDUCATIONAL_EXPLORER -> {
                            EducationalExplorerScreen(
                                onBack = { currentScreen = LauncherScreen.HOME }
                            )
                        }

                        LauncherScreen.VIDEO_THEATER -> {
                            VideoTheaterScreen(
                                onBack = { currentScreen = LauncherScreen.HOME }
                            )
                        }
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (!isExiting) {
            val app = application as ToddlerApp
            if (!app.kioskController.isKioskActive(this)) {
                app.kioskController.startChildKioskMode(this)
            }
        }
    }
}
