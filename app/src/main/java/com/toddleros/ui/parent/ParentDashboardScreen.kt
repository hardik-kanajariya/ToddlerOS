package com.toddleros.ui.parent

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.toddleros.core.theme.ToddlerInk
import com.toddleros.core.theme.ToddlerPaper
import com.toddleros.core.theme.ToddlerPrimary
import com.toddleros.core.theme.ToddlerSurface
import com.toddleros.domain.repository.MediaRepository
import com.toddleros.domain.repository.ProgressRepository
import com.toddleros.domain.repository.SettingsRepository
import com.toddleros.kiosk.KioskController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ParentDashboardScreen(
    mediaRepository: MediaRepository,
    progressRepository: ProgressRepository,
    settingsRepository: SettingsRepository,
    kioskController: KioskController,
    onBackToToddler: () -> Unit,
    onEnterChildMode: () -> Unit,
    onRestartLauncher: () -> Unit,
    modifier: Modifier = Modifier
) {
    val tabs = remember {
        listOf("Child Mode & Kiosk", "Content Library", "Learning Progress", "Launcher Settings")
    }
    var selectedTab by remember { mutableIntStateOf(0) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(ToddlerPaper)
    ) {
        TopAppBar(
            title = {
                Text(
                    text = "Vasu's Launcher Management",
                    style = MaterialTheme.typography.titleLarge,
                    color = ToddlerInk
                )
            },
            navigationIcon = {
                IconButton(onClick = onBackToToddler) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                        contentDescription = "Back to Vasu's World",
                        tint = ToddlerInk
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = ToddlerSurface
            )
        )

        ScrollableTabRow(
            selectedTabIndex = selectedTab,
            containerColor = ToddlerSurface,
            contentColor = ToddlerPrimary,
            edgePadding = 16.dp
        ) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTab == index,
                    onClick = { selectedTab = index },
                    text = { Text(title) }
                )
            }
        }

        Box(modifier = Modifier.weight(1f)) {
            when (selectedTab) {
                0 -> ChildModeKioskSection(
                    kioskController = kioskController,
                    onEnterChildMode = onEnterChildMode
                )
                1 -> ContentManagementSection(mediaRepository = mediaRepository)
                2 -> LearningProgressSection(progressRepository = progressRepository)
                3 -> LauncherSettingsSection(
                    settingsRepository = settingsRepository,
                    onRestartLauncher = onRestartLauncher
                )
            }
        }
    }
}
