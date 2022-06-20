package ru.netology.nmedia.post

import kotlinx.serialization.Serializable

@Serializable
data class Post (
    val id: Long,
    val author: String,
    val content: String?,
    val published: String,
    val likes: Int = 999,
    val likedByMe: Boolean = false,
    val share: Int = 0,
    val video: String? = null
    )