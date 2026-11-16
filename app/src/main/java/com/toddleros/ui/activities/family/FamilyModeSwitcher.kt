package com.toddleros.ui.activities.family

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.toddleros.core.theme.ToddlerInk
import com.toddleros.core.theme.ToddlerPrimary
import com.toddleros.core.theme.ToddlerPrimaryOn
import com.toddleros.core.theme.ToddlerSurface

enum class FamilyMode(val title: String, val emoji: String) {
    ALBUM("Album", "🖼️"),
    PEEKABOO("Peekaboo", "🙈"),
    EXPLORER("Explorer", "🎠")
}

@Composable
fun FamilyModeSwitcher(
    selectedMode: FamilyMode,
    onModeSelected: (FamilyMode) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        FamilyMode.entries.forEach { mode ->
            val isSelected = mode == selectedMode
            val bgColor = if (isSelected) ToddlerPrimary else ToddlerSurface
            val textColor = if (isSelected) ToddlerPrimaryOn else ToddlerInk

            Box(
                modifier = Modifier
                    .weight(1f)
                    .shadow(if (isSelected) 4.dp else 1.dp, RoundedCornerShape(22.dp))
                    .clip(RoundedCornerShape(22.dp))
                    .background(bgColor)
                    .clickable { onModeSelected(mode) }
                    .padding(vertical = 10.dp),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = mode.emoji,
                        fontSize = 16.sp
                    )
                    Text(
                        text = mode.title,
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Bold,
                        color = textColor
                    )
                }
            }
        }
    }
}
