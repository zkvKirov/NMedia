package ru.netology.nmedia.repository

import androidx.lifecycle.LiveData
import ru.netology.nmedia.post.Post

interface PostRepository {

    fun getAll(): LiveData<List<Post>>
    fun like(postId: Long)
    fun share(postId: Long)
    fun remove(postId: Long)
    fun save(post: Post)

    companion object {
        const val NEW_POST_ID = 0L
    }
}