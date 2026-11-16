package com.toddleros.domain.model.family

import com.toddleros.core.theme.PastelCoral
import com.toddleros.core.theme.PastelCoralDark
import com.toddleros.core.theme.PastelLavender
import com.toddleros.core.theme.PastelLavenderDark
import com.toddleros.core.theme.PastelMint
import com.toddleros.core.theme.PastelMintDark
import com.toddleros.core.theme.PastelPeach
import com.toddleros.core.theme.PastelPeachDark
import com.toddleros.core.theme.PastelSky
import com.toddleros.core.theme.PastelSkyDark
import com.toddleros.core.theme.PastelTeal
import com.toddleros.core.theme.PastelTealDark
import com.toddleros.core.theme.PastelYellow
import com.toddleros.core.theme.PastelYellowDark

object FamilyPhotoRepository {

    private val colorPalettes = listOf(
        PastelCoral to PastelCoralDark,
        PastelSky to PastelSkyDark,
        PastelMint to PastelMintDark,
        PastelYellow to PastelYellowDark,
        PastelLavender to PastelLavenderDark,
        PastelPeach to PastelPeachDark,
        PastelTeal to PastelTealDark
    )

    private val coverTypes = listOf(
        FamilyCoverType.SUN,
        FamilyCoverType.CLOUD,
        FamilyCoverType.FLOWER,
        FamilyCoverType.GIFT_BOX,
        FamilyCoverType.BLANKET,
        FamilyCoverType.BUSH
    )

    private val emojis = listOf("❤️", "🌟", "🌸", "🌻", "🎈", "✨", "🧸")

    val allPhotos: List<FamilyPhoto> = listOf(
        FamilyPhoto(
            id = "family_01",
            assetPath = "file:///android_asset/family/family_photo_01.jpg",
            originalFilename = "WhatsApp Image 2026-11-16 at 9.22.29 PM (1).jpeg",
            displayName = "Family Hugs",
            subtitle = "So much warmth and love",
            spokenGreeting = "Hi Vasu! We love you so much!",
            coverType = FamilyCoverType.SUN,
            cardColor = colorPalettes[0].first,
            textColor = colorPalettes[0].second,
            emoji = emojis[0]
        ),
        FamilyPhoto(
            id = "family_02",
            assetPath = "file:///android_asset/family/family_photo_02.jpg",
            originalFilename = "WhatsApp Image 2026-11-16 at 9.22.29 PM.jpeg",
            displayName = "Sweet Smiles",
            subtitle = "Happy shining faces",
            spokenGreeting = "Hello Vasu! Big smiles for you!",
            coverType = FamilyCoverType.CLOUD,
            cardColor = colorPalettes[1].first,
            textColor = colorPalettes[1].second,
            emoji = emojis[1]
        ),
        FamilyPhoto(
            id = "family_03",
            assetPath = "file:///android_asset/family/family_photo_03.jpg",
            originalFilename = "WhatsApp Image 2026-11-16 at 9.22.30 PM.jpeg",
            displayName = "Cozy Moments",
            subtitle = "Gentle loving hugs",
            spokenGreeting = "Peekaboo Vasu! We love you!",
            coverType = FamilyCoverType.FLOWER,
            cardColor = colorPalettes[2].first,
            textColor = colorPalettes[2].second,
            emoji = emojis[2]
        ),
        FamilyPhoto(
            id = "family_04",
            assetPath = "file:///android_asset/family/family_photo_04.jpg",
            originalFilename = "WhatsApp Image 2026-11-16 at 9.22.54 PM.jpeg",
            displayName = "Warm Sunshine",
            subtitle = "Always bringing bright joy",
            spokenGreeting = "Hello sweetheart! Look at you!",
            coverType = FamilyCoverType.GIFT_BOX,
            cardColor = colorPalettes[3].first,
            textColor = colorPalettes[3].second,
            emoji = emojis[3]
        ),
        FamilyPhoto(
            id = "family_05",
            assetPath = "file:///android_asset/family/family_photo_05.jpg",
            originalFilename = "WhatsApp Image 2026-11-16 at 9.22.55 PM.jpeg",
            displayName = "Loving Family",
            subtitle = "Full of joy and sweet giggles",
            spokenGreeting = "We love our Vasu so very much!",
            coverType = FamilyCoverType.BLANKET,
            cardColor = colorPalettes[4].first,
            textColor = colorPalettes[4].second,
            emoji = emojis[4]
        ),
        FamilyPhoto(
            id = "family_06",
            assetPath = "file:///android_asset/family/family_photo_06.jpg",
            originalFilename = "WhatsApp Image 2026-11-16 at 9.22.56 PM (1).jpeg",
            displayName = "Together Always",
            subtitle = "Our favorite happy times",
            spokenGreeting = "Hi Vasu! Big warm hugs!",
            coverType = FamilyCoverType.BUSH,
            cardColor = colorPalettes[5].first,
            textColor = colorPalettes[5].second,
            emoji = emojis[5]
        ),
        FamilyPhoto(
            id = "family_07",
            assetPath = "file:///android_asset/family/family_photo_07.jpg",
            originalFilename = "WhatsApp Image 2026-11-16 at 9.22.56 PM.jpeg",
            displayName = "Happy Hearts",
            subtitle = "Gentle and caring hearts",
            spokenGreeting = "Hello handsome boy! We love you!",
            coverType = FamilyCoverType.SUN,
            cardColor = colorPalettes[6].first,
            textColor = colorPalettes[6].second,
            emoji = emojis[6]
        ),
        FamilyPhoto(
            id = "family_08",
            assetPath = "file:///android_asset/family/family_photo_08.jpg",
            originalFilename = "WhatsApp Image 2026-11-16 at 9.22.57 PM.jpeg",
            displayName = "Gentle Smiles",
            subtitle = "Warmest smiles in the world",
            spokenGreeting = "Hi Vasu! Look at those happy eyes!",
            coverType = FamilyCoverType.CLOUD,
            cardColor = colorPalettes[0].first,
            textColor = colorPalettes[0].second,
            emoji = emojis[0]
        ),
        FamilyPhoto(
            id = "family_09",
            assetPath = "file:///android_asset/family/family_photo_09.jpg",
            originalFilename = "WhatsApp Image 2026-11-16 at 9.22.58 PM.jpeg",
            displayName = "Family Joy",
            subtitle = "Celebrating every little step",
            spokenGreeting = "Peekaboo! You found us, Vasu!",
            coverType = FamilyCoverType.FLOWER,
            cardColor = colorPalettes[1].first,
            textColor = colorPalettes[1].second,
            emoji = emojis[1]
        ),
        FamilyPhoto(
            id = "family_10",
            assetPath = "file:///android_asset/family/family_photo_10.jpg",
            originalFilename = "WhatsApp Image 2026-11-16 at 9.22.59 PM (1).jpeg",
            displayName = "Cherished Moments",
            subtitle = "Special memories forever",
            spokenGreeting = "Hi Vasu! We are so proud of you!",
            coverType = FamilyCoverType.GIFT_BOX,
            cardColor = colorPalettes[2].first,
            textColor = colorPalettes[2].second,
            emoji = emojis[2]
        ),
        FamilyPhoto(
            id = "family_11",
            assetPath = "file:///android_asset/family/family_photo_11.jpg",
            originalFilename = "WhatsApp Image 2026-11-16 at 9.22.59 PM (2).jpeg",
            displayName = "Golden Giggles",
            subtitle = "Pure delight and laughter",
            spokenGreeting = "Hello Vasu! Sending lots of love!",
            coverType = FamilyCoverType.BLANKET,
            cardColor = colorPalettes[3].first,
            textColor = colorPalettes[3].second,
            emoji = emojis[3]
        ),
        FamilyPhoto(
            id = "family_12",
            assetPath = "file:///android_asset/family/family_photo_12.jpg",
            originalFilename = "WhatsApp Image 2026-11-16 at 9.22.59 PM.jpeg",
            displayName = "Sweetest Blessing",
            subtitle = "Our precious little prince",
            spokenGreeting = "We love you Vasu! You are our star!",
            coverType = FamilyCoverType.BUSH,
            cardColor = colorPalettes[4].first,
            textColor = colorPalettes[4].second,
            emoji = emojis[4]
        ),
        FamilyPhoto(
            id = "family_13",
            assetPath = "file:///android_asset/family/family_photo_13.jpg",
            originalFilename = "WhatsApp Image 2026-11-16 at 9.23.00 PM (1).jpeg",
            displayName = "Cozy Hugs",
            subtitle = "Warmest safest place",
            spokenGreeting = "Hi Vasu! Big cuddle time!",
            coverType = FamilyCoverType.SUN,
            cardColor = colorPalettes[5].first,
            textColor = colorPalettes[5].second,
            emoji = emojis[5]
        ),
        FamilyPhoto(
            id = "family_14",
            assetPath = "file:///android_asset/family/family_photo_14.jpg",
            originalFilename = "WhatsApp Image 2026-11-16 at 9.23.00 PM (2).jpeg",
            displayName = "Pure Happiness",
            subtitle = "Smiles that light up the day",
            spokenGreeting = "Peekaboo Vasu! We see your bright eyes!",
            coverType = FamilyCoverType.CLOUD,
            cardColor = colorPalettes[6].first,
            textColor = colorPalettes[6].second,
            emoji = emojis[6]
        ),
        FamilyPhoto(
            id = "family_15",
            assetPath = "file:///android_asset/family/family_photo_15.jpg",
            originalFilename = "WhatsApp Image 2026-11-16 at 9.23.00 PM.jpeg",
            displayName = "Family Love",
            subtitle = "Forever and always with you",
            spokenGreeting = "Hello sweetheart! We adore you!",
            coverType = FamilyCoverType.FLOWER,
            cardColor = colorPalettes[0].first,
            textColor = colorPalettes[0].second,
            emoji = emojis[0]
        ),
        FamilyPhoto(
            id = "family_16",
            assetPath = "file:///android_asset/family/family_photo_16.jpg",
            originalFilename = "WhatsApp Image 2026-11-16 at 9.23.01 PM (1).jpeg",
            displayName = "Sunny Days",
            subtitle = "Full of giggles and play",
            spokenGreeting = "Hi Vasu! Yay for family time!",
            coverType = FamilyCoverType.GIFT_BOX,
            cardColor = colorPalettes[1].first,
            textColor = colorPalettes[1].second,
            emoji = emojis[1]
        ),
        FamilyPhoto(
            id = "family_17",
            assetPath = "file:///android_asset/family/family_photo_17.jpg",
            originalFilename = "WhatsApp Image 2026-11-16 at 9.23.01 PM (2).jpeg",
            displayName = "Family Garden",
            subtitle = "Growing together with love",
            spokenGreeting = "Hello Vasu! Look at this sweet picture!",
            coverType = FamilyCoverType.BLANKET,
            cardColor = colorPalettes[2].first,
            textColor = colorPalettes[2].second,
            emoji = emojis[2]
        ),
        FamilyPhoto(
            id = "family_18",
            assetPath = "file:///android_asset/family/family_photo_18.jpg",
            originalFilename = "WhatsApp Image 2026-11-16 at 9.23.01 PM.jpeg",
            displayName = "Bright Smiles",
            subtitle = "Sweet laughter and warmth",
            spokenGreeting = "We love you so much, little one!",
            coverType = FamilyCoverType.BUSH,
            cardColor = colorPalettes[3].first,
            textColor = colorPalettes[3].second,
            emoji = emojis[3]
        ),
        FamilyPhoto(
            id = "family_19",
            assetPath = "file:///android_asset/family/family_photo_19.jpg",
            originalFilename = "WhatsApp Image 2026-11-16 at 9.23.02 PM (1).jpeg",
            displayName = "Warm Memories",
            subtitle = "Surrounded by endless affection",
            spokenGreeting = "Peekaboo! Hi Vasu! We love you!",
            coverType = FamilyCoverType.SUN,
            cardColor = colorPalettes[4].first,
            textColor = colorPalettes[4].second,
            emoji = emojis[4]
        ),
        FamilyPhoto(
            id = "family_20",
            assetPath = "file:///android_asset/family/family_photo_20.jpg",
            originalFilename = "WhatsApp Image 2026-11-16 at 9.23.02 PM (2).jpeg",
            displayName = "Everlasting Joy",
            subtitle = "Holding hands with loved ones",
            spokenGreeting = "Hi Vasu! Look at everyone smiling for you!",
            coverType = FamilyCoverType.CLOUD,
            cardColor = colorPalettes[5].first,
            textColor = colorPalettes[5].second,
            emoji = emojis[5]
        ),
        FamilyPhoto(
            id = "family_21",
            assetPath = "file:///android_asset/family/family_photo_21.jpg",
            originalFilename = "WhatsApp Image 2026-11-16 at 9.23.02 PM.jpeg",
            displayName = "Heart of the Family",
            subtitle = "Our greatest joy and wonder",
            spokenGreeting = "You are the sweetest, Vasu! We love you!",
            coverType = FamilyCoverType.FLOWER,
            cardColor = colorPalettes[6].first,
            textColor = colorPalettes[6].second,
            emoji = emojis[6]
        )
    )
}
