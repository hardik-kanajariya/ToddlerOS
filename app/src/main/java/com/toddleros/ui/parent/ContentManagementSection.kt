package com.toddleros.ui.parent

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.OpenableColumns
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.toddleros.core.theme.ToddlerInk
import com.toddleros.core.theme.ToddlerInkSecondary
import com.toddleros.core.theme.ToddlerPrimary
import com.toddleros.core.theme.ToddlerSurface
import com.toddleros.domain.model.MediaItem
import com.toddleros.domain.model.MediaType
import com.toddleros.domain.repository.MediaRepository
import kotlinx.coroutines.launch

@Composable
fun ContentManagementSection(
    mediaRepository: MediaRepository,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    var selectedMediaType by remember { mutableStateOf(MediaType.VIDEO) }

    val mediaItems by mediaRepository.getMediaItems(selectedMediaType).collectAsState(initial = emptyList())

    val mimeType = when (selectedMediaType) {
        MediaType.VIDEO -> "video/*"
        MediaType.AUDIO -> "audio/*"
        MediaType.PHOTO -> "image/*"
        MediaType.STORY -> "image/*"
    }

    val openDocumentLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenMultipleDocuments()
    ) { uris: List<Uri> ->
        coroutineScope.launch {
            for (uri in uris) {
                try {
                    context.contentResolver.takePersistableUriPermission(
                        uri,
                        Intent.FLAG_GRANT_READ_URI_PERMISSION
                    )
                } catch (_: Exception) {
                    // Ignore if permission take is not supported
                }

                val title = queryFileName(context, uri) ?: "Media Item"
                mediaRepository.addMediaItem(
                    MediaItem(
                        uriString = uri.toString(),
                        title = title,
                        type = selectedMediaType
                    )
                )
            }
        }
    }

    Column(modifier = modifier.fillMaxSize().padding(16.dp)) {
        TabRow(
            selectedTabIndex = selectedMediaType.ordinal,
            containerColor = ToddlerSurface,
            contentColor = ToddlerPrimary
        ) {
            MediaType.entries.forEach { type ->
                Tab(
                    selected = selectedMediaType == type,
                    onClick = { selectedMediaType = type },
                    text = { Text(type.name.lowercase().replaceFirstChar { it.uppercase() }) }
                )
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "${mediaItems.size} items added",
                style = MaterialTheme.typography.bodyMedium,
                color = ToddlerInkSecondary
            )

            Button(
                onClick = { openDocumentLauncher.launch(arrayOf(mimeType)) },
                colors = ButtonDefaults.buttonColors(containerColor = ToddlerPrimary),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("+ Add Local ${selectedMediaType.name.lowercase().replaceFirstChar { it.uppercase() }}")
            }
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(
                items = mediaItems,
                key = { it.id }
            ) { item ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .background(ToddlerSurface)
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = item.title,
                            style = MaterialTheme.typography.titleMedium,
                            color = ToddlerInk
                        )
                        Text(
                            text = item.uriString,
                            style = MaterialTheme.typography.bodySmall,
                            color = ToddlerInkSecondary,
                            maxLines = 1
                        )
                    }

                    IconButton(
                        onClick = {
                            coroutineScope.launch {
                                mediaRepository.deleteMediaItem(item.id)
                            }
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Delete,
                            contentDescription = "Delete",
                            tint = MaterialTheme.colorScheme.error
                        )
                    }
                }
            }
        }
    }
}

private fun queryFileName(context: Context, uri: Uri): String? {
    return context.contentResolver.query(uri, null, null, null, null)?.use { cursor ->
        val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
        if (cursor.moveToFirst() && nameIndex != -1) {
            cursor.getString(nameIndex)
        } else null
    }
}
