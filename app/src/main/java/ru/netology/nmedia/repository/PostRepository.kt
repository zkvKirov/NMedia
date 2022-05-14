package ru.netology.nmedia.repository

import androidx.lifecycle.LiveData
import ru.netology.nmedia.Post

interface PostRepository {

    fun getAll(): LiveData<List<Post>>
    fun like(postId: Long)
    fun share(postId: Long)
}