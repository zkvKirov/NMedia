package ru.netology.nmedia.repository.impl

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.netology.nmedia.Post
import ru.netology.nmedia.db.PostDao
import ru.netology.nmedia.repository.PostRepository

class SQLiteRepository(
    private val dao: PostDao
) : PostRepository {

    private val data = MutableLiveData(dao.getAll())

    private val posts
        get() = checkNotNull(data.value) {
            "Data value should not be null"
        }

    override fun getAll(): LiveData<List<Post>> = data

    override fun like(postId: Long) {
        dao.likeById(postId)
        data.value = posts.map {
            if (it.id != postId) it
            else it.copy(
                likedByMe = !it.likedByMe,
                likes = it.likes + if (it.likedByMe) - 1 else + 1
            )
        }
    }

    override fun share(postId: Long) {
        dao.shareById(postId)
        data.value = posts.map {
            if (it.id != postId) it else it.copy(share = it.share + 1)
        }
    }

    override fun remove(postId: Long) {
        dao.removeById(postId)
        data.value = posts.filter {
            it.id != postId
        }
    }

    override fun save(post: Post) {
        val id = post.id
        val saved = dao.save(post)
        data.value = if (id == 0L) {
            listOf(saved) + posts
        } else posts.map {
            if (it.id != id) it else saved
        }
    }
}