package ru.netology.nmedia.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.netology.nmedia.Post
import ru.netology.nmedia.adapter.PostInteractionListener
import ru.netology.nmedia.repository.PostRepository
import ru.netology.nmedia.repository.PostRepositoryInMemoryImpl
import ru.netology.nmedia.util.SingleLiveEvent

class PostViewModel : ViewModel(), PostInteractionListener {

    private val repository: PostRepository = PostRepositoryInMemoryImpl()

    val data = repository.getAll()

    val sharePostContent = SingleLiveEvent<String>()
    val navigateToPostContentScreenEvent = SingleLiveEvent<String?>()
    val playVideo = SingleLiveEvent<String>()
    private val currentPost = MutableLiveData<Post?> (null)

    fun onSaveButtonClicked(content: String) {
        if (content.isBlank()) return
        val newPost = currentPost.value?.copy(
            content = content
        ) ?: Post(
            id = PostRepository.NEW_POST_ID,
            author = "I",
            content = content,
            published = "Today",
            video = "https://www.youtube.com/watch?v=uYmzLRXjcAE&list=PL0lO_mIqDDFW13-lP3IgK9lZoM1M-oPl4&index=18"
        )
        repository.save(newPost)
    }

    fun onAddButtonClicked() {
        navigateToPostContentScreenEvent.call()
    }

    // region PostInteractionListener

    override fun onLikeClicked(post: Post) = repository.like(post.id)

    override fun onShareClicked(post: Post) {
        repository.share(post.id)
        sharePostContent.value = post.content
    }

    override fun onRemoveClicked(post: Post) = repository.remove(post.id)

    override fun onEditClicked(post: Post) {
        navigateToPostContentScreenEvent.value = post.content
        currentPost.value = post
    }

    override fun onPlayVideoClicked(post: Post) {
        val url: String = requireNotNull(post.video) {
            "URL is absent"
        }
        playVideo.value = url
    }

    // endregion PostInteractionListener

}