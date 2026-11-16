package com.toddleros.ui.parent

import android.content.Intent
import android.provider.Settings
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.toddleros.core.theme.ToddlerInk
import com.toddleros.core.theme.ToddlerInkSecondary
import com.toddleros.core.theme.ToddlerPrimary
import com.toddleros.core.theme.ToddlerSurface
import com.toddleros.domain.repository.SettingsRepository
import kotlinx.coroutines.launch

@Composable
fun LauncherSettingsSection(
    settingsRepository: SettingsRepository,
    onRestartLauncher: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val narrationEnabled by settingsRepository.getBoolean("narration_enabled", true).collectAsState(initial = true)

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
                .background(ToddlerSurface)
                .padding(18.dp)
        ) {
            Text(
                text = "ToddlerOS Audio Feedback",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = ToddlerInk
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Spoken Narration & Sounds",
                        style = MaterialTheme.typography.bodyMedium,
                        color = ToddlerInk
                    )
                    Text(
                        text = "Speaks letters, numbers, and cheerful praises in activities.",
                        style = MaterialTheme.typography.bodySmall,
                        color = ToddlerInkSecondary
                    )
                }
                Switch(
                    checked = narrationEnabled,
                    onCheckedChange = { enabled ->
                        coroutineScope.launch {
                            settingsRepository.setBoolean("narration_enabled", enabled)
                        }
                    }
                )
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
                .background(ToddlerSurface)
                .padding(18.dp)
        ) {
            Text(
                text = "Android Home App Selection",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = ToddlerInk
            )
            Text(
                text = "Android manages default launcher assignments. Use the system setting below to select ToddlerOS or switch back to your device's original launcher.",
                style = MaterialTheme.typography.bodyMedium,
                color = ToddlerInkSecondary,
                modifier = Modifier.padding(vertical = 8.dp)
            )

            Button(
                onClick = {
                    val defaultAppsIntent = Intent(Settings.ACTION_MANAGE_DEFAULT_APPS_SETTINGS)
                    try {
                        context.startActivity(defaultAppsIntent)
                    } catch (_: Exception) {
                        try {
                            context.startActivity(Intent(Settings.ACTION_HOME_SETTINGS))
                        } catch (_: Exception) {
                            context.startActivity(Intent(Settings.ACTION_SETTINGS))
                        }
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = ToddlerPrimary),
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier.fillMaxWidth().padding(top = 4.dp)
            ) {
                Text("Open Android Default Apps Settings")
            }

            OutlinedButton(
                onClick = onRestartLauncher,
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
            ) {
                Text("Restart ToddlerOS Interface", color = ToddlerInk)
            }
        }
    }
}
