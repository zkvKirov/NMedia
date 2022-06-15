package ru.netology.nmedia.repository.impl

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.netology.nmedia.Post
import ru.netology.nmedia.repository.PostRepository

class InMemoryPostRepository : PostRepository {

    companion object {
        const val GENERATED_POST_AMOUNT = 10
    }

    private var nextId = GENERATED_POST_AMOUNT.toLong()

    private val data = MutableLiveData(
        List(GENERATED_POST_AMOUNT) { index ->
            Post(
                id = index + 1L,
                author = "ZKV",
                content = "Some random content $index",
                published = "14.05.2022"
            )
        }.reversed()
    )

    private val posts
        get() = checkNotNull(data.value) {
            "Data value should not be null"
        }

    override fun getAll(): LiveData<List<Post>> = data

    override fun like(postId: Long) {
        data.value = posts.map {
            if (it.id != postId) it else it.copy(likedByMe = !it.likedByMe)
        }
        data.value = posts.map {
            if (it.id != postId) {
                it
            } else {
                if (it.likedByMe)
                    it.copy(likes = it.likes + 1)
                else
                    it.copy(likes = it.likes - 1)
            }
        }
    }

    override fun share(postId: Long) {
        data.value = posts.map {
            if (it.id != postId) it else it.copy(share = it.share + 1)
        }
    }

    override fun remove(postId: Long) {
        data.value = posts.filter {
            it.id != postId
        }
    }

    override fun save(post: Post) {
        if (post.id == PostRepository.NEW_POST_ID) insert(post) else update(post)
    }

    private fun insert(post: Post) {
        data.value = listOf(
            post.copy(id = ++nextId)
        ) + posts
    }

    private fun update(post: Post) {
        data.value = posts.map {
            if (it.id != post.id) it else it.copy(
                content = post.content,
                video = post.video
            )
        }
    }
}