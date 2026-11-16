package com.toddleros.ui.parent

import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.toddleros.core.theme.PastelMint
import com.toddleros.core.theme.PastelMintDark
import com.toddleros.core.theme.PastelYellow
import com.toddleros.core.theme.PastelYellowDark
import com.toddleros.core.theme.ToddlerInk
import com.toddleros.core.theme.ToddlerInkSecondary
import com.toddleros.core.theme.ToddlerPrimary
import com.toddleros.core.theme.ToddlerSurface
import com.toddleros.kiosk.KioskController

@Composable
fun ChildModeKioskSection(
    kioskController: KioskController,
    onEnterChildMode: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val activity = context as? Activity

    val isDeviceOwner = remember { kioskController.isDeviceOwner() }
    val isLockTaskPermitted = remember { kioskController.isLockTaskPermitted() }
    var isKioskActive by remember {
        mutableStateOf(activity?.let { kioskController.isKioskActive(it) } ?: false)
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Child Mode Status Banner
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(24.dp))
                .background(if (isKioskActive) PastelMint else PastelYellow)
                .padding(20.dp)
        ) {
            Column {
                Text(
                    text = if (isKioskActive) "🔒 Secure Child Mode is Active" else "🛡️ Standard Mode",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = if (isKioskActive) PastelMintDark else PastelYellowDark
                )
                Text(
                    text = if (isKioskActive) {
                        "Lock Task mode is engaged. Notification drawer, Home, and Recent apps are restricted."
                    } else {
                        "ToddlerOS is operating as a normal launcher. You can activate Secure Child Mode below."
                    },
                    style = MaterialTheme.typography.bodyMedium,
                    color = if (isKioskActive) PastelMintDark else PastelYellowDark,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }

        // Action Buttons
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
                .background(ToddlerSurface)
                .padding(18.dp)
        ) {
            Text(
                text = "Child Mode Activation",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = ToddlerInk
            )
            Text(
                text = "Starting Child Mode secures the screen with Lock Task Mode. A 6-digit parent password is required to exit.",
                style = MaterialTheme.typography.bodyMedium,
                color = ToddlerInkSecondary,
                modifier = Modifier.padding(vertical = 8.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Button(
                    onClick = {
                        if (activity != null) {
                            val started = kioskController.startChildKioskMode(activity)
                            isKioskActive = kioskController.isKioskActive(activity)
                            if (started) {
                                onEnterChildMode()
                            }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = ToddlerPrimary),
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Start Child Mode")
                }

                if (isKioskActive) {
                    OutlinedButton(
                        onClick = {
                            if (activity != null) {
                                kioskController.stopChildKioskMode(activity)
                                isKioskActive = kioskController.isKioskActive(activity)
                            }
                        },
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Exit Lock", color = ToddlerInk)
                    }
                }
            }
        }

        // Device Owner Provisioning Status
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
                .background(ToddlerSurface)
                .padding(18.dp)
        ) {
            Text(
                text = "Kiosk Provisioning Status",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = ToddlerInk
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "Device Owner Status:", style = MaterialTheme.typography.bodyMedium, color = ToddlerInkSecondary)
                Text(
                    text = if (isDeviceOwner) "Provisioned ✅" else "Not Provisioned (Screen Pinning mode)",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    color = if (isDeviceOwner) ToddlerPrimary else ToddlerInk
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "Lock Task Permitted:", style = MaterialTheme.typography.bodyMedium, color = ToddlerInkSecondary)
                Text(
                    text = if (isLockTaskPermitted) "Yes" else "Standard",
                    style = MaterialTheme.typography.bodyMedium,
                    color = ToddlerInk
                )
            }

            Text(
                text = kioskController.getProvisioningInstructions(),
                style = MaterialTheme.typography.bodySmall,
                color = ToddlerInkSecondary,
                modifier = Modifier.padding(top = 12.dp)
            )
        }
    }
}
