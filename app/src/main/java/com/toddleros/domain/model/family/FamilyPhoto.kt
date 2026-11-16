package com.toddleros.domain.model.family

import androidx.compose.ui.graphics.Color

enum class FamilyCoverType {
    SUN,
    CLOUD,
    FLOWER,
    GIFT_BOX,
    BLANKET,
    BUSH
}

data class FamilyPhoto(
    val id: String,
    val assetPath: String,
    val originalFilename: String,
    val displayName: String,
    val subtitle: String,
    val spokenGreeting: String,
    val coverType: FamilyCoverType,
    val cardColor: Color,
    val textColor: Color,
    val emoji: String
)
