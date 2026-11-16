package com.toddleros.ui.activities.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.automirrored.rounded.VolumeMute
import androidx.compose.material.icons.automirrored.rounded.VolumeUp
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.toddleros.core.theme.ToddlerInk
import com.toddleros.core.theme.ToddlerPaper
import com.toddleros.core.theme.ToddlerSurface

@Composable
fun ToddlerTopBar(
    title: String,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    isMuted: Boolean = false,
    onToggleMute: (() -> Unit)? = null
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(ToddlerPaper)
            .padding(horizontal = 16.dp, vertical = 10.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Large, 56dp touch-target back button for toddler fingers
        IconButton(
            onClick = onBack,
            modifier = Modifier
                .size(56.dp)
                .clip(CircleShape)
                .background(ToddlerSurface)
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                contentDescription = "Back",
                tint = ToddlerInk,
                modifier = Modifier.size(32.dp)
            )
        }

        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = ToddlerInk
        )

        if (onToggleMute != null) {
            IconButton(
                onClick = onToggleMute,
                modifier = Modifier
                    .size(56.dp)
                    .clip(CircleShape)
                    .background(ToddlerSurface)
            ) {
                Icon(
                    imageVector = if (isMuted) Icons.AutoMirrored.Rounded.VolumeMute else Icons.AutoMirrored.Rounded.VolumeUp,
                    contentDescription = if (isMuted) "Unmute" else "Mute",
                    tint = ToddlerInk,
                    modifier = Modifier.size(28.dp)
                )
            }
        } else {
            // Balance spacer
            IconButton(
                onClick = {},
                enabled = false,
                modifier = Modifier.size(56.dp)
            ) {}
        }
    }
}
