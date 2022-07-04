package ru.netology.nmedia.post

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class EditPostResult(
    val newContent: String?,
    val newVideoUrl: String?
) : Parcelable