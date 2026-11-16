package com.toddleros.ui.home

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.toddleros.core.theme.PastelCoral
import com.toddleros.core.theme.PastelCoralDark
import com.toddleros.core.theme.PastelLavender
import com.toddleros.core.theme.PastelLavenderDark
import com.toddleros.core.theme.PastelMint
import com.toddleros.core.theme.PastelMintDark
import com.toddleros.core.theme.PastelPeach
import com.toddleros.core.theme.PastelPeachDark
import com.toddleros.core.theme.PastelSky
import com.toddleros.core.theme.PastelSkyDark
import com.toddleros.core.theme.PastelTeal
import com.toddleros.core.theme.PastelTealDark
import com.toddleros.core.theme.PastelYellow
import com.toddleros.core.theme.PastelYellowDark
import com.toddleros.core.theme.ToddlerPaper
import com.toddleros.ui.activities.animals.AnimalArtwork
import com.toddleros.ui.activities.fruits.FruitArtwork
import com.toddleros.ui.parent.ParentPasswordDialog

@Composable
fun HomeScreen(
    isChildModeActive: Boolean,
    onExitChildMode: () -> Unit,
    // My Family
    onOpenMyFamily: () -> Unit,
    // Media & Learning Activities
    onOpenAnimalWorld: () -> Unit,
    onOpenMusic: () -> Unit,
    onOpenStories: () -> Unit,
    onOpenEducationalExplorer: () -> Unit,
    onOpenVideoTheater: () -> Unit,
    // Play & Create
    onOpenBubblePop: () -> Unit,
    onOpenPeekaboo: () -> Unit,
    onOpenMagicBalloons: () -> Unit,
    onOpenBabyPiano: () -> Unit,
    onOpenDancingCharacters: () -> Unit,
    onOpenCreativeStudio: () -> Unit,
    onOpenPhotos: () -> Unit,
    onOpenTalkingCharacter: () -> Unit,
    // Basic Learning
    onOpenAnimalSounds: () -> Unit,
    onOpenFruitFriends: () -> Unit,
    onOpenColorsShapes: () -> Unit,
    onOpenAlphabet: () -> Unit,
    onOpenNumbers: () -> Unit,
    onOpenVehicles: () -> Unit,
    modifier: Modifier = Modifier
) {
    var showPasswordDialog by remember { mutableStateOf(false) }
    val listState = rememberLazyListState()

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(ToddlerPaper)
    ) {
        LazyColumn(
            state = listState,
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 36.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // 1. Welcome Banner
            item {
                HomeWelcomeBanner(
                    onParentExit = { showPasswordDialog = true }
                )
            }

            // 2. My Family
            item {
                HomeFamilyBanner(
                    onOpenMyFamily = onOpenMyFamily
                )
            }

            // 3. ANIMAL WORLD
            item {
                HomeSectionHeader(
                    title = "Animal World",
                    emoji = "🦁",
                    subtitle = "Hear authentic calls of 31 farm, wild & pet animals!"
                )
            }

            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    HomeActivityTile(
                        title = "Animal World 🐾",
                        subtitle = "Authentic animal recordings",
                        bgColor = PastelYellow,
                        textColor = PastelYellowDark,
                        onClick = onOpenAnimalWorld,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        AnimalArtwork(animalId = "dog", size = 68.dp)
                    }
                }
            }

            // 4. MUSIC & NURSERY RHYMES
            item {
                HomeSectionHeader(
                    title = "Music & Nursery Rhymes",
                    emoji = "🎵",
                    subtitle = "Sing along with 30 classic recordings and songs!"
                )
            }

            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    HomeActivityTile(
                        title = "Tunes & Rhymes 🎶",
                        subtitle = "Classic rhymes & lullabies",
                        bgColor = PastelSky,
                        textColor = PastelSkyDark,
                        onClick = onOpenMusic,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(text = "🎵", fontSize = 42.sp)
                    }

                    HomeActivityTile(
                        title = "Baby Piano",
                        subtitle = "Touch musical keys",
                        bgColor = PastelCoral,
                        textColor = PastelCoralDark,
                        onClick = onOpenBabyPiano,
                        modifier = Modifier.weight(1f)
                    ) {
                        Canvas(modifier = Modifier.size(64.dp)) {
                            drawRoundRect(Color(0xFFEF4444), topLeft = Offset(4f, 12f), size = Size(16f, 40f), cornerRadius = CornerRadius(6f))
                            drawRoundRect(Color(0xFFF59E0B), topLeft = Offset(24f, 12f), size = Size(16f, 40f), cornerRadius = CornerRadius(6f))
                            drawRoundRect(Color(0xFF10B981), topLeft = Offset(44f, 12f), size = Size(16f, 40f), cornerRadius = CornerRadius(6f))
                        }
                    }
                }
            }

            // 5. STORY TIME
            item {
                HomeSectionHeader(
                    title = "Story Time",
                    emoji = "📚",
                    subtitle = "25 picture books with friendly read-aloud!"
                )
            }

            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    HomeActivityTile(
                        title = "Storybooks 📖",
                        subtitle = "Fables & Vasu's garden story",
                        bgColor = PastelPeach,
                        textColor = PastelPeachDark,
                        onClick = onOpenStories,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(text = "📚", fontSize = 44.sp)
                    }
                }
            }

            // 6. DISCOVER & LEARN
            item {
                HomeSectionHeader(
                    title = "Discover & Learn",
                    emoji = "🌟",
                    subtitle = "Explore 96 colorful pictures across 8 learning themes!"
                )
            }

            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    HomeActivityTile(
                        title = "Picture Explorer 🎨",
                        subtitle = "Animals, fruit, letters & numbers",
                        bgColor = PastelTeal,
                        textColor = PastelTealDark,
                        onClick = onOpenEducationalExplorer,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(text = "🌟", fontSize = 42.sp)
                    }
                }
            }

            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    HomeActivityTile(
                        title = "Colors & Shapes",
                        subtitle = "Circles, stars & squares",
                        bgColor = PastelLavender,
                        textColor = PastelLavenderDark,
                        onClick = onOpenColorsShapes,
                        modifier = Modifier.weight(1f)
                    ) {
                        Canvas(modifier = Modifier.size(64.dp)) {
                            drawCircle(Color(0xFFEF4444), radius = 16f, center = Offset(20f, 24f))
                            drawRoundRect(Color(0xFF10B981), topLeft = Offset(36f, 10f), size = Size(26f, 26f), cornerRadius = CornerRadius(6f))
                            drawCircle(Color(0xFFFBBF24), radius = 14f, center = Offset(32f, 48f))
                        }
                    }

                    HomeActivityTile(
                        title = "Fruit Friends",
                        subtitle = "Meet sweet colorful fruit",
                        bgColor = PastelCoral,
                        textColor = PastelCoralDark,
                        onClick = onOpenFruitFriends,
                        modifier = Modifier.weight(1f)
                    ) {
                        FruitArtwork(fruitId = "apple", size = 68.dp)
                    }
                }
            }

            // 7. WATCH & EXPLORE (VIDEOS)
            item {
                HomeSectionHeader(
                    title = "Watch & Explore",
                    emoji = "🎬",
                    subtitle = "Watch 8 offline animated rhyme videos!"
                )
            }

            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    HomeActivityTile(
                        title = "Video Theater 🍿",
                        subtitle = "Twinkle Star, Itsy Spider & more",
                        bgColor = PastelSky,
                        textColor = PastelSkyDark,
                        onClick = onOpenVideoTheater,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(text = "🎬", fontSize = 44.sp)
                    }
                }
            }

            // 8. LET'S PLAY & CREATE
            item {
                HomeSectionHeader(
                    title = "Let's Play & Create",
                    emoji = "🎈",
                    subtitle = "Bubbles, balloons, dancing & finger paint!"
                )
            }

            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    HomeActivityTile(
                        title = "Bubble Pop",
                        subtitle = "Pop floating bubbles",
                        bgColor = PastelSky,
                        textColor = PastelSkyDark,
                        onClick = onOpenBubblePop,
                        modifier = Modifier.weight(1f)
                    ) {
                        Canvas(modifier = Modifier.size(64.dp)) {
                            drawCircle(Color(0xFF60A5FA).copy(alpha = 0.5f), radius = 24f, center = Offset(22f, 28f))
                            drawCircle(Color(0xFF38BDF8).copy(alpha = 0.65f), radius = 34f, center = Offset(46f, 38f))
                            drawCircle(Color.White.copy(alpha = 0.8f), radius = 7f, center = Offset(40f, 30f))
                        }
                    }

                    HomeActivityTile(
                        title = "Peekaboo!",
                        subtitle = "Find surprise buddies",
                        bgColor = PastelMint,
                        textColor = PastelMintDark,
                        onClick = onOpenPeekaboo,
                        modifier = Modifier.weight(1f)
                    ) {
                        Canvas(modifier = Modifier.size(64.dp)) {
                            drawCircle(Color(0xFF16A34A), radius = 28f, center = Offset(32f, 36f))
                            drawCircle(Color(0xFF22C55E), radius = 22f, center = Offset(46f, 30f))
                            drawCircle(Color(0xFF1E293B), radius = 4f, center = Offset(28f, 32f))
                            drawCircle(Color(0xFF1E293B), radius = 4f, center = Offset(40f, 32f))
                        }
                    }
                }
            }

            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    HomeActivityTile(
                        title = "Magic Balloons",
                        subtitle = "Floating balloons",
                        bgColor = PastelLavender,
                        textColor = PastelLavenderDark,
                        onClick = onOpenMagicBalloons,
                        modifier = Modifier.weight(1f)
                    ) {
                        Canvas(modifier = Modifier.size(64.dp)) {
                            drawOval(Color(0xFFF472B6), topLeft = Offset(14f, 8f), size = Size(36f, 46f))
                            drawOval(Color.White.copy(alpha = 0.5f), topLeft = Offset(20f, 14f), size = Size(9f, 14f))
                            drawLine(Color(0xFF94A3B8), start = Offset(32f, 54f), end = Offset(32f, 64f), strokeWidth = 3f)
                        }
                    }

                    HomeActivityTile(
                        title = "Drawing Canvas",
                        subtitle = "Finger paint studio",
                        bgColor = PastelYellow,
                        textColor = PastelYellowDark,
                        onClick = onOpenCreativeStudio,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(text = "🎨", fontSize = 42.sp)
                    }
                }
            }

            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    HomeActivityTile(
                        title = "Dancing Friends",
                        subtitle = "Groove & jump",
                        bgColor = PastelPeach,
                        textColor = PastelPeachDark,
                        onClick = onOpenDancingCharacters,
                        modifier = Modifier.weight(1f)
                    ) {
                        Canvas(modifier = Modifier.size(64.dp)) {
                            drawCircle(Color(0xFFB45309), radius = 22f, center = Offset(32f, 32f))
                            drawCircle(Color(0xFFFDE68A), radius = 7f, center = Offset(16f, 18f))
                            drawCircle(Color(0xFFFDE68A), radius = 7f, center = Offset(48f, 18f))
                            drawCircle(Color(0xFF1E293B), radius = 3f, center = Offset(25f, 28f))
                            drawCircle(Color(0xFF1E293B), radius = 3f, center = Offset(39f, 28f))
                        }
                    }

                    HomeActivityTile(
                        title = "Talking Mimi",
                        subtitle = "Echoes & giggles",
                        bgColor = PastelLavender,
                        textColor = PastelLavenderDark,
                        onClick = onOpenTalkingCharacter,
                        modifier = Modifier.weight(1f)
                    ) {
                        Canvas(modifier = Modifier.size(64.dp)) {
                            val orange = Color(0xFFFB923C)
                            val ear1 = Path().apply {
                                moveTo(16f, 26f); lineTo(20f, 8f); lineTo(32f, 18f); close()
                            }
                            val ear2 = Path().apply {
                                moveTo(48f, 26f); lineTo(44f, 8f); lineTo(32f, 18f); close()
                            }
                            drawPath(ear1, orange)
                            drawPath(ear2, orange)
                            drawCircle(orange, radius = 22f, center = Offset(32f, 34f))
                            drawCircle(Color(0xFFFEF3C7), radius = 10f, center = Offset(32f, 38f))
                            drawCircle(Color(0xFF1E293B), radius = 3f, center = Offset(25f, 32f))
                            drawCircle(Color(0xFF1E293B), radius = 3f, center = Offset(39f, 32f))
                        }
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(28.dp))
            }
        }

        // Parent Protected Exit Dialog
        if (showPasswordDialog) {
            ParentPasswordDialog(
                title = if (isChildModeActive) "Exit Child Mode" else "Parent Exit",
                subtitle = "Enter 6-digit parent password to exit",
                onDismiss = { showPasswordDialog = false },
                onSuccess = {
                    showPasswordDialog = false
                    onExitChildMode()
                }
            )
        }
    }
}
