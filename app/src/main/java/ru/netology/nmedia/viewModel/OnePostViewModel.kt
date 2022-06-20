package ru.netology.nmedia.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import ru.netology.nmedia.adapter.OnePostInteractionListener
import ru.netology.nmedia.post.EditPostResult
import ru.netology.nmedia.post.Post
import ru.netology.nmedia.repository.PostRepository
import ru.netology.nmedia.repository.impl.FilePostRepository
import ru.netology.nmedia.util.SingleLiveEvent

class OnePostViewModel(
    application: Application
) : AndroidViewModel(application), OnePostInteractionListener {

    private val repository: PostRepository = FilePostRepository(application)

    val data = repository.getAll()

    val sharePostContent = SingleLiveEvent<String?>()
    val navigateToPostContentScreenEvent = SingleLiveEvent<EditPostResult?>()
    val playVideo = SingleLiveEvent<String>()
    val navigateToFeedFragment = SingleLiveEvent<Post>()
    private val currentPost = MutableLiveData<Post?> (null)

    fun onSaveButtonClicked(postContent: EditPostResult) {
        if (postContent.equals(null)) return
        val newPost = currentPost.value?.copy(
            content = postContent.newContent,
            video = postContent.newVideoUrl
        )
        if (newPost != null) {
            repository.save(newPost)
        }
        currentPost.value = null
    }

    // region PostInteractionListener

    override fun onLikeClicked(): Unit = currentPost.value.let {
        if (it != null) {
            repository.like(it.id)
        }
    }

    override fun onShareClicked() {
        currentPost.value?.let { repository.share(it.id) }
        sharePostContent.value = currentPost.value?.content
    }

    override fun onRemoveClicked() {
        currentPost.value?.let { repository.remove(it.id) }
    }

    override fun onEditClicked() {
        val post = currentPost.value
        if (post != null) {
            navigateToPostContentScreenEvent.value = EditPostResult(post.content, post.video)
        }
        currentPost.value = post
    }

    override fun onPlayVideoClicked() {
        val url: String = requireNotNull(currentPost.value?.video) {
            "URL is absent"
        }
        playVideo.value = url
    }

    // endregion PostInteractionListener
}