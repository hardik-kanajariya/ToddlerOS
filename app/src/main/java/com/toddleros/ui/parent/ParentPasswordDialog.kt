package com.toddleros.ui.parent

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.Backspace
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.toddleros.core.theme.ToddlerBorder
import com.toddleros.core.theme.ToddlerInk
import com.toddleros.core.theme.ToddlerInkSecondary
import com.toddleros.core.theme.ToddlerPaper
import com.toddleros.core.theme.ToddlerPrimary

// Centralized development constant (will be replaced by parent-configurable credential in future)
private const val DEFAULT_PARENT_PASSWORD = "123456"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ParentPasswordDialog(
    title: String = "Parent Verification",
    subtitle: String = "Enter 6-digit parent password",
    onDismiss: () -> Unit,
    onSuccess: () -> Unit
) {
    var enteredPin by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }

    fun onDigitPress(digit: String) {
        if (enteredPin.length < 6) {
            val newPin = enteredPin + digit
            enteredPin = newPin
            errorMessage = ""

            if (newPin.length == 6) {
                if (newPin == DEFAULT_PARENT_PASSWORD) {
                    onSuccess()
                } else {
                    errorMessage = "Incorrect password. Please try again!"
                    enteredPin = ""
                }
            }
        }
    }

    fun onBackspace() {
        if (enteredPin.isNotEmpty()) {
            enteredPin = enteredPin.dropLast(1)
            errorMessage = ""
        }
    }

    fun onClear() {
        enteredPin = ""
        errorMessage = ""
    }

    BasicAlertDialog(
        onDismissRequest = onDismiss
    ) {
        Surface(
            shape = RoundedCornerShape(28.dp),
            color = MaterialTheme.colorScheme.surface,
            modifier = Modifier.fillMaxWidth().padding(16.dp)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = ToddlerInk
                    )
                    IconButton(onClick = onDismiss, modifier = Modifier.size(32.dp)) {
                        Icon(
                            imageVector = Icons.Rounded.Close,
                            contentDescription = "Cancel",
                            tint = ToddlerInkSecondary
                        )
                    }
                }

                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodyMedium,
                    color = ToddlerInkSecondary,
                    modifier = Modifier.padding(top = 4.dp)
                )

                // 6 Pin indicator dots
                Row(
                    horizontalArrangement = Arrangement.spacedBy(14.dp),
                    modifier = Modifier.padding(vertical = 20.dp)
                ) {
                    for (i in 0 until 6) {
                        val isFilled = i < enteredPin.length
                        Box(
                            modifier = Modifier
                                .size(18.dp)
                                .clip(CircleShape)
                                .background(if (isFilled) ToddlerPrimary else ToddlerBorder)
                        )
                    }
                }

                if (errorMessage.isNotEmpty()) {
                    Text(
                        text = errorMessage,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.error,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                }

                // Custom Numeric Keypad
                val keys = listOf(
                    "1", "2", "3",
                    "4", "5", "6",
                    "7", "8", "9",
                    "C", "0", "⌫"
                )

                LazyVerticalGrid(
                    columns = GridCells.Fixed(3),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
                ) {
                    items(keys.size) { index ->
                        val key = keys[index]
                        Box(
                            modifier = Modifier
                                .height(54.dp)
                                .clip(RoundedCornerShape(16.dp))
                                .background(ToddlerPaper)
                                .clickable {
                                    when (key) {
                                        "C" -> onClear()
                                        "⌫" -> onBackspace()
                                        else -> onDigitPress(key)
                                    }
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            if (key == "⌫") {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Rounded.Backspace,
                                    contentDescription = "Backspace",
                                    tint = ToddlerInk,
                                    modifier = Modifier.size(22.dp)
                                )
                            } else {
                                Text(
                                    text = key,
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = ToddlerInk
                                )
                            }
                        }
                    }
                }

                TextButton(
                    onClick = onDismiss,
                    modifier = Modifier.padding(top = 16.dp)
                ) {
                    Text("Cancel", color = ToddlerInkSecondary)
                }
            }
        }
    }
}
