package com.toddleros.ui.activities.family

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.toddleros.domain.model.family.FamilyCoverType
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun FamilyPeekCover(
    coverType: FamilyCoverType,
    modifier: Modifier = Modifier,
    size: Dp = 100.dp
) {
    Canvas(modifier = modifier.size(size)) {
        val w = this.size.width
        val h = this.size.height

        when (coverType) {
            FamilyCoverType.SUN -> {
                val sunYellow = Color(0xFFFBBF24)
                val rayYellow = Color(0xFFF59E0B)
                val cx = w * 0.5f
                val cy = h * 0.5f
                val r = w * 0.3f

                // Sun rays
                for (i in 0 until 8) {
                    val angle = (i * Math.PI / 4).toFloat()
                    val startX = cx + (r + 4f) * cos(angle)
                    val startY = cy + (r + 4f) * sin(angle)
                    val endX = cx + (r + 14f) * cos(angle)
                    val endY = cy + (r + 14f) * sin(angle)
                    drawLine(
                        color = rayYellow,
                        start = Offset(startX, startY),
                        end = Offset(endX, endY),
                        strokeWidth = 6f
                    )
                }

                // Sun disk
                drawCircle(sunYellow, radius = r, center = Offset(cx, cy))
                // Cheerful eyes
                drawCircle(Color(0xFF1E293B), radius = 3.5f, center = Offset(cx - 10f, cy - 4f))
                drawCircle(Color(0xFF1E293B), radius = 3.5f, center = Offset(cx + 10f, cy - 4f))
                // Rosy cheeks
                drawCircle(Color(0xFFF87171).copy(alpha = 0.6f), radius = 4f, center = Offset(cx - 16f, cy + 6f))
                drawCircle(Color(0xFFF87171).copy(alpha = 0.6f), radius = 4f, center = Offset(cx + 16f, cy + 6f))
            }

            FamilyCoverType.CLOUD -> {
                val cloudWhite = Color.White
                val shadowColor = Color(0xFFE2E8F0)

                drawCircle(shadowColor, radius = w * 0.22f, center = Offset(w * 0.32f, h * 0.53f))
                drawCircle(shadowColor, radius = w * 0.28f, center = Offset(w * 0.5f, h * 0.43f))
                drawCircle(shadowColor, radius = w * 0.22f, center = Offset(w * 0.68f, h * 0.53f))

                drawCircle(cloudWhite, radius = w * 0.22f, center = Offset(w * 0.32f, h * 0.5f))
                drawCircle(cloudWhite, radius = w * 0.28f, center = Offset(w * 0.5f, h * 0.4f))
                drawCircle(cloudWhite, radius = w * 0.22f, center = Offset(w * 0.68f, h * 0.5f))
                drawRoundRect(
                    cloudWhite,
                    topLeft = Offset(w * 0.22f, h * 0.5f),
                    size = Size(w * 0.56f, h * 0.22f),
                    cornerRadius = CornerRadius(20f)
                )

                // Cute eyes on cloud
                drawCircle(Color(0xFF334155), radius = 3f, center = Offset(w * 0.44f, h * 0.48f))
                drawCircle(Color(0xFF334155), radius = 3f, center = Offset(w * 0.56f, h * 0.48f))
            }

            FamilyCoverType.FLOWER -> {
                val petalColor = Color(0xFFF472B6)
                val centerYellow = Color(0xFFFBBF24)
                val cx = w * 0.5f
                val cy = h * 0.5f
                val pr = w * 0.16f

                // 6 Petals
                for (i in 0 until 6) {
                    val angle = (i * Math.PI / 3).toFloat()
                    val px = cx + (w * 0.22f) * cos(angle)
                    val py = cy + (w * 0.22f) * sin(angle)
                    drawCircle(petalColor, radius = pr, center = Offset(px, py))
                }
                // Center
                drawCircle(centerYellow, radius = w * 0.18f, center = Offset(cx, cy))
                // Eyes
                drawCircle(Color(0xFF1E293B), radius = 3f, center = Offset(cx - 7f, cy - 2f))
                drawCircle(Color(0xFF1E293B), radius = 3f, center = Offset(cx + 7f, cy - 2f))
            }

            FamilyCoverType.GIFT_BOX -> {
                val boxRed = Color(0xFFEF4444)
                val ribbonYellow = Color(0xFFFBBF24)

                drawRoundRect(
                    boxRed,
                    topLeft = Offset(w * 0.2f, h * 0.32f),
                    size = Size(w * 0.6f, h * 0.5f),
                    cornerRadius = CornerRadius(14f)
                )
                // Ribbons
                drawRect(
                    ribbonYellow,
                    topLeft = Offset(w * 0.44f, h * 0.32f),
                    size = Size(w * 0.12f, h * 0.5f)
                )
                drawRect(
                    ribbonYellow,
                    topLeft = Offset(w * 0.2f, h * 0.52f),
                    size = Size(w * 0.6f, h * 0.1f)
                )
                // Bow loops
                drawCircle(ribbonYellow, radius = w * 0.09f, center = Offset(w * 0.41f, h * 0.27f))
                drawCircle(ribbonYellow, radius = w * 0.09f, center = Offset(w * 0.59f, h * 0.27f))
            }

            FamilyCoverType.BLANKET -> {
                val blanketPink = Color(0xFFEC4899)
                val dotYellow = Color(0xFFFDE047)

                drawRoundRect(
                    blanketPink,
                    topLeft = Offset(w * 0.16f, h * 0.28f),
                    size = Size(w * 0.68f, h * 0.54f),
                    cornerRadius = CornerRadius(22f)
                )
                // Cheerful polka dots
                drawCircle(dotYellow, radius = w * 0.05f, center = Offset(w * 0.34f, h * 0.42f))
                drawCircle(dotYellow, radius = w * 0.05f, center = Offset(w * 0.66f, h * 0.42f))
                drawCircle(dotYellow, radius = w * 0.05f, center = Offset(w * 0.5f, h * 0.62f))
            }

            FamilyCoverType.BUSH -> {
                val darkGreen = Color(0xFF15803D)
                val lightGreen = Color(0xFF22C55E)
                val berryRed = Color(0xFFEF4444)

                drawCircle(darkGreen, radius = w * 0.32f, center = Offset(w * 0.34f, h * 0.5f))
                drawCircle(darkGreen, radius = w * 0.32f, center = Offset(w * 0.66f, h * 0.5f))
                drawCircle(lightGreen, radius = w * 0.34f, center = Offset(w * 0.5f, h * 0.38f))

                // Berries
                drawCircle(berryRed, radius = w * 0.05f, center = Offset(w * 0.4f, h * 0.46f))
                drawCircle(berryRed, radius = w * 0.05f, center = Offset(w * 0.6f, h * 0.44f))
            }
        }
    }
}
