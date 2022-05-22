package ru.netology.nmedia.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.netology.nmedia.Post
import ru.netology.nmedia.adapter.PostInteractionListener
import ru.netology.nmedia.repository.PostRepository
import ru.netology.nmedia.repository.PostRepositoryInMemoryImpl

class PostViewModel : ViewModel(), PostInteractionListener {

    private val repository: PostRepository = PostRepositoryInMemoryImpl()

    val data = repository.getAll()

    val currentPost = MutableLiveData<Post?> (null)

    fun onSaveButtonClicked(content: String) {
        if (content.isBlank()) return
        val newPost = currentPost.value?.copy(
            content = content
        ) ?: Post(
            id = PostRepository.NEW_POST_ID,
            author = "I",
            content = content,
            published = "Today"
        )
        repository.save(newPost)
        currentPost.value = null
    }

    // region PostInteractionListener

    override fun onLikeClicked(post: Post) = repository.like(post.id)

    override fun onShareClicked(post: Post) = repository.share(post.id)

    override fun onRemoveClicked(post: Post) = repository.remove(post.id)

    override fun onEditClicked(post: Post) {
        currentPost.value = post
    }

    // endregion PostInteractionListener

}