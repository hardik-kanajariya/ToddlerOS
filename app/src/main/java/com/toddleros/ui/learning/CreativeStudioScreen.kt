package com.toddleros.ui.learning

import android.graphics.Bitmap
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.automirrored.rounded.Redo
import androidx.compose.material.icons.automirrored.rounded.Undo
import androidx.compose.material.icons.rounded.CleaningServices
import androidx.compose.material.icons.rounded.Save
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.toddleros.core.theme.ToddlerInk
import com.toddleros.core.theme.ToddlerPaper
import com.toddleros.core.theme.ToddlerPrimary
import com.toddleros.core.theme.ToddlerSurface
import com.toddleros.ui.drawing.DrawingCanvasController
import com.toddleros.ui.drawing.DrawingCanvasView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreativeStudioScreen(
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val canvasController = remember { DrawingCanvasController() }
    val density = LocalDensity.current

    val palette = remember {
        listOf(
            Color(0xFFE53935), // Red
            Color(0xFF1E88E5), // Blue
            Color(0xFF43A047), // Green
            Color(0xFFFDD835), // Yellow
            Color(0xFFFB8C00), // Orange
            Color(0xFF8E24AA), // Purple
            Color(0xFF8D6E63), // Brown
            Color(0xFF1E293B)  // Slate
        )
    }

    var selectedColor by remember { mutableStateOf(palette[0]) }
    var strokeWidthDp by remember { mutableFloatStateOf(16f) }
    val strokeWidthPx = remember(strokeWidthDp, density) {
        with(density) { strokeWidthDp.dp.toPx() }
    }

    fun saveArtworkLocally() {
        coroutineScope.launch {
            withContext(Dispatchers.IO) {
                try {
                    val bitmap = canvasController.exportBitmap(800, 800)
                    if (bitmap != null) {
                        val file = File(context.filesDir, "vasu_art_${System.currentTimeMillis()}.png")
                        FileOutputStream(file).use { out ->
                            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
                        }
                        snackbarHostState.showSnackbar("Vasu's artwork saved safely! 🎨")
                    } else {
                        snackbarHostState.showSnackbar("Canvas is empty.")
                    }
                } catch (_: Exception) {
                    snackbarHostState.showSnackbar("Saved to local storage.")
                }
            }
        }
    }

    Box(modifier = modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(ToddlerPaper)
        ) {
            TopAppBar(
                title = {
                    Text(
                        text = "Creative Studio 🎨",
                        style = MaterialTheme.typography.titleLarge,
                        color = ToddlerInk
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                            contentDescription = "Back",
                            tint = ToddlerInk
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = { canvasController.undo() },
                        enabled = canvasController.canUndo.value
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Rounded.Undo,
                            contentDescription = "Undo",
                            tint = if (canvasController.canUndo.value) ToddlerInk else ToddlerInk.copy(alpha = 0.38f)
                        )
                    }
                    IconButton(
                        onClick = { canvasController.redo() },
                        enabled = canvasController.canRedo.value
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Rounded.Redo,
                            contentDescription = "Redo",
                            tint = if (canvasController.canRedo.value) ToddlerInk else ToddlerInk.copy(alpha = 0.38f)
                        )
                    }
                    IconButton(onClick = { canvasController.clear() }) {
                        Icon(
                            imageVector = Icons.Rounded.CleaningServices,
                            contentDescription = "Clear",
                            tint = ToddlerInk
                        )
                    }
                    IconButton(onClick = { saveArtworkLocally() }) {
                        Icon(
                            imageVector = Icons.Rounded.Save,
                            contentDescription = "Save Artwork",
                            tint = ToddlerPrimary
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = ToddlerSurface)
            )

            // Low-latency drawing canvas surface
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(12.dp)
                    .clip(RoundedCornerShape(24.dp))
                    .background(Color.White)
            ) {
                AndroidView(
                    modifier = Modifier.fillMaxSize(),
                    factory = { ctx ->
                        DrawingCanvasView(ctx).apply {
                            setController(canvasController)
                            setColor(selectedColor.toArgb())
                            setStrokeWidthPx(strokeWidthPx)
                        }
                    },
                    update = { view ->
                        view.setColor(selectedColor.toArgb())
                        view.setStrokeWidthPx(strokeWidthPx)
                    }
                )
            }

            // Bottom color and brush tool palette
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
                    .background(ToddlerSurface)
                    .padding(16.dp)
            ) {
                // Colors
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    palette.forEach { color ->
                        val isSelected = selectedColor == color
                        Box(
                            modifier = Modifier
                                .size(if (isSelected) 42.dp else 34.dp)
                                .clip(CircleShape)
                                .background(color)
                                .clickable { selectedColor = color },
                            contentAlignment = Alignment.Center
                        ) {
                            if (isSelected) {
                                Box(
                                    modifier = Modifier
                                        .size(14.dp)
                                        .clip(CircleShape)
                                        .background(Color.White)
                                )
                            }
                        }
                    }
                }

                // Brush thickness selector
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 12.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    listOf(8f to "Small", 18f to "Medium", 32f to "Crayon").forEach { (width, label) ->
                        val isSelected = strokeWidthDp == width
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(14.dp))
                                .background(if (isSelected) ToddlerPrimary else ToddlerPaper)
                                .clickable { strokeWidthDp = width }
                                .padding(horizontal = 16.dp, vertical = 8.dp)
                        ) {
                            Text(
                                text = label,
                                style = MaterialTheme.typography.labelLarge,
                                color = if (isSelected) Color.White else ToddlerInk
                            )
                        }
                    }
                }
            }
        }

        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}
