package ru.netology.nmedia.post

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class Post (
    @SerializedName("id")
    val id: Long,
    @SerializedName("author")
    val author: String,
    @SerializedName("content")
    val content: String?,
    @SerializedName("published")
    val published: String,
    @SerializedName("likes")
    val likes: Int = 999,
    @SerializedName("likedByMe")
    val likedByMe: Boolean = false,
    @SerializedName("share")
    val share: Int = 0,
    @SerializedName("video")
    val video: String? = null
)