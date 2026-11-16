package com.toddleros.domain.model

data class StoryPage(
    val pageNumber: Int,
    val text: String,
    val imageUri: String? = null
)

data class StoryBook(
    val id: String,
    val title: String,
    val coverImageUri: String? = null,
    val pages: List<StoryPage> = emptyList()
)
