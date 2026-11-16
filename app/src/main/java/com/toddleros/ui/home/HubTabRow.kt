package com.toddleros.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.toddleros.core.theme.ToddlerInk
import com.toddleros.core.theme.ToddlerPrimary
import com.toddleros.core.theme.ToddlerPrimaryOn
import com.toddleros.core.theme.ToddlerSurface
import com.toddleros.domain.model.HubCategory

@Composable
fun HubTabRow(
    selectedHub: HubCategory,
    onHubSelected: (HubCategory) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyRow(
        modifier = modifier.fillMaxWidth(),
        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 6.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        items(HubCategory.entries.toTypedArray()) { hub ->
            val isSelected = hub == selectedHub
            val bgColor = if (isSelected) ToddlerPrimary else ToddlerSurface
            val textColor = if (isSelected) ToddlerPrimaryOn else ToddlerInk

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(24.dp))
                    .background(bgColor)
                    .clickable { onHubSelected(hub) }
                    .padding(horizontal = 18.dp, vertical = 12.dp),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = hub.emoji,
                        fontSize = 18.sp
                    )
                    Text(
                        text = hub.title,
                        style = MaterialTheme.typography.labelLarge,
                        color = textColor
                    )
                }
            }
        }
    }
}
