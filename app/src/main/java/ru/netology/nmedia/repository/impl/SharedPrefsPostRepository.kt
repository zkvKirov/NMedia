package ru.netology.nmedia.repository.impl

import android.app.Application
import android.content.Context
import androidx.core.content.edit
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.serialization.*
import kotlinx.serialization.json.Json
import ru.netology.nmedia.Post
import ru.netology.nmedia.repository.PostRepository
import kotlin.properties.Delegates

class SharedPrefsPostRepository(
    application: Application
) : PostRepository {

    private val prefs = application.getSharedPreferences(
        "repository", Context.MODE_PRIVATE
    )

    private var nextId: Long by Delegates.observable(
        prefs.getLong(NEXT_ID_PREFS_KEY, 0L)
    ) {_, _, newValue ->
        prefs.edit { putLong(NEXT_ID_PREFS_KEY, newValue) }
    }

    private val data: MutableLiveData<List<Post>>

    init {
        val serializedPosts = prefs.getString(POST_PREFS_KEY, null)
        val posts: List<Post> = if (serializedPosts != null) {
            Json.decodeFromString(serializedPosts)
        } else emptyList()
        data = MutableLiveData(posts)
    }

    private var posts
        get() = checkNotNull(data.value) {
            "Data value should not be null"
        }
        set(value) {
            prefs.edit {
                val serializedPosts = Json.encodeToString(value)
                putString(POST_PREFS_KEY, serializedPosts)
            }
            data.value = value
        }

    override fun getAll(): LiveData<List<Post>> = data

    override fun like(postId: Long) {
        posts = posts.map {
            if (it.id != postId) it
            else it.copy(
                likedByMe = !it.likedByMe,
                likes = it.likes + if (it.likedByMe) - 1 else + 1
            )
        }
    }

    override fun share(postId: Long) {
        posts = posts.map {
            if (it.id != postId) it else it.copy(share = it.share + 1)
        }
    }

    override fun remove(postId: Long) {
        posts = posts.filter {
            it.id != postId
        }
    }

    override fun save(post: Post) {
        if (post.id == PostRepository.NEW_POST_ID) insert(post) else update(post)
    }

    private fun insert(post: Post) {
        posts = listOf(
            post.copy(id = ++nextId)
        ) + posts
    }

    private fun update(post: Post) {
        posts = posts.map {
            if (it.id != post.id) it else it.copy(
                content = post.content,
                video = post.video
            )
        }
    }

    companion object {
        const val POST_PREFS_KEY = "posts"
        const val NEXT_ID_PREFS_KEY = "nextId"
    }
}