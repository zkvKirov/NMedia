package ru.netology.nmedia.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.netology.nmedia.Post

class PostRepositoryInMemoryImpl : PostRepository {

    private var posts = List(10) { index ->
        Post(
            id = index + 1L,
            author = "ZKV",
            content = "Some random content $index",
            published = "14.05.2022",
//            likes = 0,
//            likedByMe = false,
//            share = 0
        )
    }

    private val data = MutableLiveData(posts)

    override fun getAll(): LiveData<List<Post>> = data

    override fun like(postId: Long) {
        posts = posts.map {
            if (it.id != postId) it else it.copy(likedByMe = !it.likedByMe)
        }
        posts = posts.map {
            if (it.likedByMe)
                it.copy(likes = it.likes + 1)
            else
                it.copy(likes = it.likes - 1)
        }
        data.value = posts
    }

    override fun share(postId: Long) {
        data.value = posts.map {
            it.copy(share = it.share + 1)
        }
    }
}