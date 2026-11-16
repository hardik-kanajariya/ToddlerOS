package com.toddleros.ui.play.talking

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import kotlinx.coroutines.delay

@Composable
fun TalkingCharacterView(
    expression: CharacterExpression,
    isSpeaking: Boolean,
    onTapArea: (CharacterTapArea) -> Unit,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "character_motion")

    // Idle gentle bobbing
    val bodyOffsetY by infiniteTransition.animateFloat(
        initialValue = -6f,
        targetValue = 6f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1800, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "bobbing"
    )

    // Dynamic mouth flap when speaking
    val mouthFlap by infiniteTransition.animateFloat(
        initialValue = 0.2f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 150, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "mouth_flap"
    )

    // Ear wiggle when listening
    val earWiggle by infiniteTransition.animateFloat(
        initialValue = -4f,
        targetValue = 4f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 220, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "ear_wiggle"
    )

    // Natural blink state
    var isBlinking by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        while (true) {
            delay(3200)
            isBlinking = true
            delay(160)
            isBlinking = false
        }
    }

    Box(modifier = modifier) {
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(Unit) {
                    detectTapGestures { tapOffset ->
                        val w = size.width
                        val h = size.height

                        val headY = h * 0.38f + bodyOffsetY
                        val noseY = headY + 12f
                        val bellyY = h * 0.74f

                        val earBoxTop = headY - 140f
                        val earBoxBottom = headY - 60f

                        when {
                            // Ears tap
                            tapOffset.y in earBoxTop..earBoxBottom -> {
                                onTapArea(CharacterTapArea.EARS)
                            }
                            // Nose tap
                            tapOffset.x in (w * 0.44f)..(w * 0.56f) &&
                                tapOffset.y in (noseY - 24f)..(noseY + 24f) -> {
                                onTapArea(CharacterTapArea.NOSE)
                            }
                            // Head tap
                            tapOffset.y in (headY - 60f)..(headY + 60f) -> {
                                onTapArea(CharacterTapArea.HEAD)
                            }
                            // Belly tap
                            tapOffset.y > (bellyY - 70f) -> {
                                onTapArea(CharacterTapArea.BELLY)
                            }
                            else -> {
                                onTapArea(CharacterTapArea.HEAD)
                            }
                        }
                    }
                }
        ) {
            val cx = size.width / 2f
            val headCy = size.height * 0.38f + bodyOffsetY
            val currentEarTilt = if (expression == CharacterExpression.LISTENING) earWiggle else 0f

            drawCatBody(cx = cx, headCy = headCy)
            drawCatEars(cx = cx, headCy = headCy, earTilt = currentEarTilt)
            drawCatHead(cx = cx, headCy = headCy)
            drawCatWhiskers(cx = cx, headCy = headCy)
            drawCatEyes(
                cx = cx,
                headCy = headCy,
                expression = expression,
                isBlinking = isBlinking
            )
            drawCatNose(cx = cx, headCy = headCy)
            drawCatMouth(
                cx = cx,
                headCy = headCy,
                expression = expression,
                isSpeaking = isSpeaking,
                mouthFlap = mouthFlap
            )
            drawCatCheeks(cx = cx, headCy = headCy, expression = expression)
        }
    }
}

private fun DrawScope.drawCatBody(cx: Float, headCy: Float) {
    val bodyCy = headCy + 150f
    val bodyWidth = 230f
    val bodyHeight = 170f

    // Outer fur (Warm amber cream)
    drawOval(
        color = Color(0xFFFFB74D),
        topLeft = Offset(cx - bodyWidth / 2f, bodyCy - bodyHeight / 2f),
        size = Size(bodyWidth, bodyHeight)
    )

    // Body outline
    drawOval(
        color = Color(0xFFE65100),
        topLeft = Offset(cx - bodyWidth / 2f, bodyCy - bodyHeight / 2f),
        size = Size(bodyWidth, bodyHeight),
        style = Stroke(width = 6f)
    )

    // Belly patch (Soft light cream)
    drawOval(
        color = Color(0xFFFFF3E0),
        topLeft = Offset(cx - (bodyWidth * 0.55f) / 2f, bodyCy - (bodyHeight * 0.65f) / 2f + 10f),
        size = Size(bodyWidth * 0.55f, bodyHeight * 0.65f)
    )
}

private fun DrawScope.drawCatEars(cx: Float, headCy: Float, earTilt: Float) {
    val earY = headCy - 70f
    val earSpread = 75f

    // Left Ear
    val leftPath = Path().apply {
        moveTo(cx - earSpread - 45f, earY + 20f)
        lineTo(cx - earSpread - 20f + earTilt, earY - 75f)
        lineTo(cx - earSpread + 25f, earY)
        close()
    }
    drawPath(path = leftPath, color = Color(0xFFFFB74D), style = Fill)
    drawPath(path = leftPath, color = Color(0xFFE65100), style = Stroke(width = 6f, cap = StrokeCap.Round))

    // Left Inner Ear (Pink)
    val leftInnerPath = Path().apply {
        moveTo(cx - earSpread - 35f, earY + 14f)
        lineTo(cx - earSpread - 20f + earTilt, earY - 55f)
        lineTo(cx - earSpread + 15f, earY)
        close()
    }
    drawPath(path = leftInnerPath, color = Color(0xFFFF8A80), style = Fill)

    // Right Ear
    val rightPath = Path().apply {
        moveTo(cx + earSpread - 25f, earY)
        lineTo(cx + earSpread + 20f - earTilt, earY - 75f)
        lineTo(cx + earSpread + 45f, earY + 20f)
        close()
    }
    drawPath(path = rightPath, color = Color(0xFFFFB74D), style = Fill)
    drawPath(path = rightPath, color = Color(0xFFE65100), style = Stroke(width = 6f, cap = StrokeCap.Round))

    // Right Inner Ear (Pink)
    val rightInnerPath = Path().apply {
        moveTo(cx + earSpread - 15f, earY)
        lineTo(cx + earSpread + 20f - earTilt, earY - 55f)
        lineTo(cx + earSpread + 35f, earY + 14f)
        close()
    }
    drawPath(path = rightInnerPath, color = Color(0xFFFF8A80), style = Fill)
}

private fun DrawScope.drawCatHead(cx: Float, headCy: Float) {
    val headRadius = 110f

    // Head base
    drawCircle(
        color = Color(0xFFFFB74D),
        radius = headRadius,
        center = Offset(cx, headCy)
    )

    // Head outline
    drawCircle(
        color = Color(0xFFE65100),
        radius = headRadius,
        center = Offset(cx, headCy),
        style = Stroke(width = 6f)
    )
}

private fun DrawScope.drawCatWhiskers(cx: Float, headCy: Float) {
    val whiskerColor = Color(0xFF4E342E)
    val wY = headCy + 22f

    // Left whiskers
    drawLine(
        color = whiskerColor,
        start = Offset(cx - 50f, wY - 10f),
        end = Offset(cx - 120f, wY - 20f),
        strokeWidth = 4f,
        cap = StrokeCap.Round
    )
    drawLine(
        color = whiskerColor,
        start = Offset(cx - 50f, wY + 6f),
        end = Offset(cx - 122f, wY + 10f),
        strokeWidth = 4f,
        cap = StrokeCap.Round
    )

    // Right whiskers
    drawLine(
        color = whiskerColor,
        start = Offset(cx + 50f, wY - 10f),
        end = Offset(cx + 120f, wY - 20f),
        strokeWidth = 4f,
        cap = StrokeCap.Round
    )
    drawLine(
        color = whiskerColor,
        start = Offset(cx + 50f, wY + 6f),
        end = Offset(cx + 122f, wY + 10f),
        strokeWidth = 4f,
        cap = StrokeCap.Round
    )
}

private fun DrawScope.drawCatEyes(
    cx: Float,
    headCy: Float,
    expression: CharacterExpression,
    isBlinking: Boolean
) {
    val eyeOffsetX = 44f
    val eyeY = headCy - 16f
    val eyeRadius = 18f

    if (isBlinking || expression == CharacterExpression.SLEEPY) {
        // Closed horizontal curved slits
        val strokeColor = Color(0xFF3E2723)
        val leftEyePath = Path().apply {
            moveTo(cx - eyeOffsetX - 18f, eyeY)
            quadraticTo(cx - eyeOffsetX, eyeY + 8f, cx - eyeOffsetX + 18f, eyeY)
        }
        val rightEyePath = Path().apply {
            moveTo(cx + eyeOffsetX - 18f, eyeY)
            quadraticTo(cx + eyeOffsetX, eyeY + 8f, cx + eyeOffsetX + 18f, eyeY)
        }
        drawPath(leftEyePath, strokeColor, style = Stroke(width = 6f, cap = StrokeCap.Round))
        drawPath(rightEyePath, strokeColor, style = Stroke(width = 6f, cap = StrokeCap.Round))
        return
    }

    if (expression == CharacterExpression.LAUGHING || expression == CharacterExpression.HAPPY) {
        // Happy curved arcs ^ ^
        val strokeColor = Color(0xFF3E2723)
        val leftEyePath = Path().apply {
            moveTo(cx - eyeOffsetX - 20f, eyeY + 6f)
            quadraticTo(cx - eyeOffsetX, eyeY - 14f, cx - eyeOffsetX + 20f, eyeY + 6f)
        }
        val rightEyePath = Path().apply {
            moveTo(cx + eyeOffsetX - 20f, eyeY + 6f)
            quadraticTo(cx + eyeOffsetX, eyeY - 14f, cx + eyeOffsetX + 20f, eyeY + 6f)
        }
        drawPath(leftEyePath, strokeColor, style = Stroke(width = 7f, cap = StrokeCap.Round))
        drawPath(rightEyePath, strokeColor, style = Stroke(width = 7f, cap = StrokeCap.Round))
        return
    }

    val pupilRadius = if (expression == CharacterExpression.SURPRISED) 8f else 13f
    val whiteRadius = if (expression == CharacterExpression.SURPRISED) 24f else eyeRadius

    // Left eye white
    drawCircle(Color.White, radius = whiteRadius, center = Offset(cx - eyeOffsetX, eyeY))
    drawCircle(Color(0xFF3E2723), radius = whiteRadius, center = Offset(cx - eyeOffsetX, eyeY), style = Stroke(4f))

    // Right eye white
    drawCircle(Color.White, radius = whiteRadius, center = Offset(cx + eyeOffsetX, eyeY))
    drawCircle(Color(0xFF3E2723), radius = whiteRadius, center = Offset(cx + eyeOffsetX, eyeY), style = Stroke(4f))

    // Left pupil & highlight
    drawCircle(Color(0xFF2E7D32), radius = pupilRadius, center = Offset(cx - eyeOffsetX, eyeY))
    drawCircle(Color.White, radius = pupilRadius * 0.4f, center = Offset(cx - eyeOffsetX - 4f, eyeY - 4f))

    // Right pupil & highlight
    drawCircle(Color(0xFF2E7D32), radius = pupilRadius, center = Offset(cx + eyeOffsetX, eyeY))
    drawCircle(Color.White, radius = pupilRadius * 0.4f, center = Offset(cx + eyeOffsetX - 4f, eyeY - 4f))
}

private fun DrawScope.drawCatNose(cx: Float, headCy: Float) {
    val noseY = headCy + 10f
    val nosePath = Path().apply {
        moveTo(cx - 12f, noseY)
        lineTo(cx + 12f, noseY)
        lineTo(cx, noseY + 12f)
        close()
    }
    drawPath(nosePath, color = Color(0xFFFF4081), style = Fill)
}

private fun DrawScope.drawCatMouth(
    cx: Float,
    headCy: Float,
    expression: CharacterExpression,
    isSpeaking: Boolean,
    mouthFlap: Float
) {
    val mouthY = headCy + 26f
    val mouthColor = Color(0xFF3E2723)

    if (isSpeaking) {
        val openHeight = 12f + (22f * mouthFlap)
        val openWidth = 32f

        // Mouth cavity
        drawOval(
            color = Color(0xFF880E4F),
            topLeft = Offset(cx - openWidth / 2f, mouthY),
            size = Size(openWidth, openHeight)
        )
        // Cute pink tongue
        drawOval(
            color = Color(0xFFFF4081),
            topLeft = Offset(cx - (openWidth * 0.6f) / 2f, mouthY + openHeight * 0.4f),
            size = Size(openWidth * 0.6f, openHeight * 0.6f)
        )
        // Outline
        drawOval(
            color = mouthColor,
            topLeft = Offset(cx - openWidth / 2f, mouthY),
            size = Size(openWidth, openHeight),
            style = Stroke(width = 4f)
        )
        return
    }

    when (expression) {
        CharacterExpression.LAUGHING -> {
            val smilePath = Path().apply {
                moveTo(cx - 24f, mouthY)
                quadraticTo(cx, mouthY + 28f, cx + 24f, mouthY)
                close()
            }
            drawPath(smilePath, color = Color(0xFF880E4F), style = Fill)
            drawPath(smilePath, color = mouthColor, style = Stroke(width = 5f, cap = StrokeCap.Round))
        }

        CharacterExpression.SURPRISED -> {
            drawCircle(
                color = Color(0xFF880E4F),
                radius = 12f,
                center = Offset(cx, mouthY + 8f)
            )
            drawCircle(
                color = mouthColor,
                radius = 12f,
                center = Offset(cx, mouthY + 8f),
                style = Stroke(4f)
            )
        }

        else -> {
            // Cute double-curved cat smile :3
            val leftSmile = Path().apply {
                moveTo(cx, mouthY)
                quadraticTo(cx - 12f, mouthY + 12f, cx - 22f, mouthY + 4f)
            }
            val rightSmile = Path().apply {
                moveTo(cx, mouthY)
                quadraticTo(cx + 12f, mouthY + 12f, cx + 22f, mouthY + 4f)
            }
            drawLine(mouthColor, Offset(cx, headCy + 22f), Offset(cx, mouthY), strokeWidth = 4f, cap = StrokeCap.Round)
            drawPath(leftSmile, mouthColor, style = Stroke(width = 4f, cap = StrokeCap.Round))
            drawPath(rightSmile, mouthColor, style = Stroke(width = 4f, cap = StrokeCap.Round))
        }
    }
}

private fun DrawScope.drawCatCheeks(cx: Float, headCy: Float, expression: CharacterExpression) {
    val cheekAlpha = if (expression == CharacterExpression.HAPPY || expression == CharacterExpression.LAUGHING) 0.65f else 0.35f
    val cheekColor = Color(0xFFFF5252).copy(alpha = cheekAlpha)
    val cheekY = headCy + 14f

    drawCircle(cheekColor, radius = 15f, center = Offset(cx - 62f, cheekY))
    drawCircle(cheekColor, radius = 15f, center = Offset(cx + 62f, cheekY))
}
