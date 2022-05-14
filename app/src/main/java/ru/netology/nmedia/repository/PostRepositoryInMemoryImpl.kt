package ru.netology.nmedia.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.netology.nmedia.Post
import ru.netology.nmedia.R

class PostRepositoryInMemoryImpl : PostRepository {

    private var post = Post(
        id = 1,
        author = "Костян",
        content = "Привет, это моё новое приложение, жми чтобы скачать → http://netolo.gy/fyb",
        published = "9 мая в 16:55",
        likes = 999
    )

    private val data = MutableLiveData(post)

    override fun get(): LiveData<Post> = data

    override fun like() {
        post = post.copy(likedByMe = !post.likedByMe)
        post = if (post.likedByMe)
            post.copy(likes = post.likes + 1)
        else
            post.copy(likes = post.likes - 1)
        data.value = post
    }

    override fun share() {
        post = post.copy(share = post.share + 1)
        data.value = post
    }
}