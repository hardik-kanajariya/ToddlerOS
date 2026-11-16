package com.toddleros.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.toddleros.core.theme.ToddlerPaper
import com.toddleros.domain.model.HubCategory
import com.toddleros.domain.repository.ProgressRepository
import com.toddleros.ui.parent.ParentPasswordDialog

@Composable
fun HomeScreen(
    progressRepository: ProgressRepository,
    isChildModeActive: Boolean,
    onOpenParentDashboard: () -> Unit,
    onExitChildMode: () -> Unit,
    onOpenAlphabet: () -> Unit,
    onOpenNumbers: () -> Unit,
    onOpenAnimals: () -> Unit,
    onOpenColorsShapes: () -> Unit,
    onOpenFruitsVeggies: () -> Unit,
    onOpenVehicles: () -> Unit,
    onOpenMemoryMatch: () -> Unit,
    onOpenShapePuzzle: () -> Unit,
    onOpenCreativeStudio: () -> Unit,
    onOpenPhotos: () -> Unit,
    onOpenVideos: () -> Unit,
    onOpenMusic: () -> Unit,
    onOpenStories: () -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedHub by remember { mutableStateOf(HubCategory.LEARN) }
    var showPasswordDialog by remember { mutableStateOf(false) }

    val totalStars by progressRepository.getTotalStars().collectAsState(initial = 0)

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(ToddlerPaper)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            HomeHeader(
                totalStars = totalStars,
                onParentClick = { showPasswordDialog = true }
            )

            HubTabRow(
                selectedHub = selectedHub,
                onHubSelected = { selectedHub = it }
            )

            Box(modifier = Modifier.weight(1f)) {
                when (selectedHub) {
                    HubCategory.LEARN -> {
                        ExploreLearnSection(
                            onOpenAlphabet = onOpenAlphabet,
                            onOpenNumbers = onOpenNumbers,
                            onOpenAnimals = onOpenAnimals,
                            onOpenColorsShapes = onOpenColorsShapes,
                            onOpenFruitsVeggies = onOpenFruitsVeggies,
                            onOpenVehicles = onOpenVehicles
                        )
                    }

                    HubCategory.PLAY -> {
                        PlayCreateSection(
                            onOpenMemoryMatch = onOpenMemoryMatch,
                            onOpenShapePuzzle = onOpenShapePuzzle
                        )
                    }

                    HubCategory.CREATE -> {
                        CreateSection(
                            onOpenCreativeStudio = onOpenCreativeStudio,
                            onOpenPhotos = onOpenPhotos
                        )
                    }

                    HubCategory.DISCOVER -> {
                        ListenDiscoverSection(
                            onOpenMusic = onOpenMusic,
                            onOpenVideos = onOpenVideos,
                            onOpenStories = onOpenStories
                        )
                    }
                }
            }
        }

        if (showPasswordDialog) {
            ParentPasswordDialog(
                title = if (isChildModeActive) "Exit Child Mode" else "Parent Verification",
                subtitle = "Enter 6-digit parent password to continue",
                onDismiss = { showPasswordDialog = false },
                onSuccess = {
                    showPasswordDialog = false
                    if (isChildModeActive) {
                        onExitChildMode()
                    } else {
                        onOpenParentDashboard()
                    }
                }
            )
        }
    }
}
