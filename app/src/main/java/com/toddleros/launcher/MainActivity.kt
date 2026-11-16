package com.toddleros.launcher

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.addCallback
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.toddleros.ToddlerApp
import com.toddleros.core.theme.ToddlerOSTheme
import com.toddleros.core.theme.ToddlerPaper
import com.toddleros.ui.content.images.PhotoGalleryScreen
import com.toddleros.ui.content.music.MusicLibraryScreen
import com.toddleros.ui.content.stories.StoriesScreen
import com.toddleros.ui.content.video.VideoLibraryScreen
import com.toddleros.ui.home.HomeScreen
import com.toddleros.ui.learning.AlphabetAdventureScreen
import com.toddleros.ui.learning.AnimalWorldScreen
import com.toddleros.ui.learning.ColorShapeScreen
import com.toddleros.ui.learning.CreativeStudioScreen
import com.toddleros.ui.learning.FruitsVegetablesScreen
import com.toddleros.ui.learning.MemoryMatchScreen
import com.toddleros.ui.learning.NumberGardenScreen
import com.toddleros.ui.learning.ShapePuzzleScreen
import com.toddleros.ui.learning.VehiclesScreen
import com.toddleros.ui.parent.ParentDashboardScreen

enum class LauncherScreen {
    HOME,
    ALPHABET,
    NUMBERS,
    ANIMALS,
    COLORS_SHAPES,
    FRUITS_VEGGIES,
    VEHICLES,
    MEMORY_MATCH,
    SHAPE_PUZZLE,
    CREATIVE_STUDIO,
    PHOTOS,
    VIDEOS,
    MUSIC,
    STORIES,
    PARENT_DASHBOARD
}

class MainActivity : ComponentActivity() {

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

                    // Back press: returns to HOME if inside a learning activity; Android handles HOME screen navigation
                    onBackPressedDispatcher.addCallback(this, enabled = currentScreen != LauncherScreen.HOME) {
                        currentScreen = LauncherScreen.HOME
                    }

                    when (currentScreen) {
                        LauncherScreen.HOME -> {
                            HomeScreen(
                                progressRepository = app.progressRepository,
                                isChildModeActive = isChildModeActive,
                                onOpenParentDashboard = { currentScreen = LauncherScreen.PARENT_DASHBOARD },
                                onExitChildMode = {
                                    app.kioskController.stopChildKioskMode(this@MainActivity)
                                    isChildModeActive = app.kioskController.isKioskActive(this@MainActivity)
                                },
                                onOpenAlphabet = { currentScreen = LauncherScreen.ALPHABET },
                                onOpenNumbers = { currentScreen = LauncherScreen.NUMBERS },
                                onOpenAnimals = { currentScreen = LauncherScreen.ANIMALS },
                                onOpenColorsShapes = { currentScreen = LauncherScreen.COLORS_SHAPES },
                                onOpenFruitsVeggies = { currentScreen = LauncherScreen.FRUITS_VEGGIES },
                                onOpenVehicles = { currentScreen = LauncherScreen.VEHICLES },
                                onOpenMemoryMatch = { currentScreen = LauncherScreen.MEMORY_MATCH },
                                onOpenShapePuzzle = { currentScreen = LauncherScreen.SHAPE_PUZZLE },
                                onOpenCreativeStudio = { currentScreen = LauncherScreen.CREATIVE_STUDIO },
                                onOpenPhotos = { currentScreen = LauncherScreen.PHOTOS },
                                onOpenVideos = { currentScreen = LauncherScreen.VIDEOS },
                                onOpenMusic = { currentScreen = LauncherScreen.MUSIC },
                                onOpenStories = { currentScreen = LauncherScreen.STORIES }
                            )
                        }

                        LauncherScreen.ALPHABET -> {
                            AlphabetAdventureScreen(
                                progressRepository = app.progressRepository,
                                onBack = { currentScreen = LauncherScreen.HOME }
                            )
                        }

                        LauncherScreen.NUMBERS -> {
                            NumberGardenScreen(
                                progressRepository = app.progressRepository,
                                onBack = { currentScreen = LauncherScreen.HOME }
                            )
                        }

                        LauncherScreen.ANIMALS -> {
                            AnimalWorldScreen(
                                progressRepository = app.progressRepository,
                                onBack = { currentScreen = LauncherScreen.HOME }
                            )
                        }

                        LauncherScreen.COLORS_SHAPES -> {
                            ColorShapeScreen(
                                progressRepository = app.progressRepository,
                                onBack = { currentScreen = LauncherScreen.HOME }
                            )
                        }

                        LauncherScreen.FRUITS_VEGGIES -> {
                            FruitsVegetablesScreen(
                                progressRepository = app.progressRepository,
                                onBack = { currentScreen = LauncherScreen.HOME }
                            )
                        }

                        LauncherScreen.VEHICLES -> {
                            VehiclesScreen(
                                progressRepository = app.progressRepository,
                                onBack = { currentScreen = LauncherScreen.HOME }
                            )
                        }

                        LauncherScreen.MEMORY_MATCH -> {
                            MemoryMatchScreen(
                                progressRepository = app.progressRepository,
                                onBack = { currentScreen = LauncherScreen.HOME }
                            )
                        }

                        LauncherScreen.SHAPE_PUZZLE -> {
                            ShapePuzzleScreen(
                                progressRepository = app.progressRepository,
                                onBack = { currentScreen = LauncherScreen.HOME }
                            )
                        }

                        LauncherScreen.CREATIVE_STUDIO -> {
                            CreativeStudioScreen(
                                progressRepository = app.progressRepository,
                                onBack = { currentScreen = LauncherScreen.HOME }
                            )
                        }

                        LauncherScreen.PHOTOS -> {
                            PhotoGalleryScreen(
                                mediaRepository = app.mediaRepository,
                                onBack = { currentScreen = LauncherScreen.HOME }
                            )
                        }

                        LauncherScreen.VIDEOS -> {
                            VideoLibraryScreen(
                                mediaRepository = app.mediaRepository,
                                onBack = { currentScreen = LauncherScreen.HOME }
                            )
                        }

                        LauncherScreen.MUSIC -> {
                            MusicLibraryScreen(
                                mediaRepository = app.mediaRepository,
                                onBack = { currentScreen = LauncherScreen.HOME }
                            )
                        }

                        LauncherScreen.STORIES -> {
                            StoriesScreen(
                                mediaRepository = app.mediaRepository,
                                onBack = { currentScreen = LauncherScreen.HOME }
                            )
                        }

                        LauncherScreen.PARENT_DASHBOARD -> {
                            ParentDashboardScreen(
                                mediaRepository = app.mediaRepository,
                                progressRepository = app.progressRepository,
                                settingsRepository = app.settingsRepository,
                                kioskController = app.kioskController,
                                onBackToToddler = { currentScreen = LauncherScreen.HOME },
                                onEnterChildMode = {
                                    isChildModeActive = app.kioskController.isKioskActive(this@MainActivity)
                                    currentScreen = LauncherScreen.HOME
                                },
                                onRestartLauncher = { recreate() }
                            )
                        }
                    }
                }
            }
        }
    }
}
