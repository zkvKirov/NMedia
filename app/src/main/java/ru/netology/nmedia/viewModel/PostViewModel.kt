package ru.netology.nmedia.viewModel

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import ru.netology.nmedia.post.EditPostResult
import ru.netology.nmedia.post.Post
import ru.netology.nmedia.adapter.PostInteractionListener
import ru.netology.nmedia.db.AppDb
import ru.netology.nmedia.repository.PostRepository
import ru.netology.nmedia.repository.impl.PostRepositoryImpl
import ru.netology.nmedia.util.SingleLiveEvent
import kotlin.properties.Delegates

class PostViewModel(
    application: Application
) : AndroidViewModel(application), PostInteractionListener {

    private val repository: PostRepository = PostRepositoryImpl(
            dao = AppDb.getInstance(
                context = application
            ).postDao
        )

    val data = repository.getAll()
    var postId by Delegates.notNull<Long>()

    val sharePostContent = SingleLiveEvent<String?>()
    val navigateToPostContentScreenEvent = SingleLiveEvent<EditPostResult?>()
    val playVideo = SingleLiveEvent<String>()
    val navigateToOnePost = SingleLiveEvent<Long>()
    val navigateToFeedFragment = SingleLiveEvent<Unit>()
    private val currentPost = MutableLiveData<Post?> (null)

    fun onSaveButtonClicked(postContent: EditPostResult) {
        if (postContent.equals(null)) return
        val newPost = currentPost.value?.copy(
            content = postContent.newContent,
            video = postContent.newVideoUrl
        ) ?: Post(
            id = PostRepository.NEW_POST_ID,
            author = "I",
            content = postContent.newContent,
            published = "Today",
            video = postContent.newVideoUrl
            //video = "https://www.youtube.com/watch?v=SW_UCzFO7X0"
        )
        repository.save(newPost)
        currentPost.value = null
    }

    fun onAddButtonClicked(postContent: EditPostResult?) {
        navigateToPostContentScreenEvent.value = postContent
    }

    // region PostInteractionListener

    override fun onLikeClicked(post: Post) = repository.like(post.id)

    override fun onShareClicked(post: Post) {
        repository.share(post.id)
        sharePostContent.value = post.content
    }

    override fun onRemoveClicked(post: Post) {
        repository.remove(post.id)
        Toast.makeText(getApplication(), "Post was deleted", Toast.LENGTH_SHORT).show()
    }

    override fun onEditClicked(post: Post) {
        navigateToPostContentScreenEvent.value = EditPostResult(post.content, post.video)
        currentPost.value = post
    }

    override fun onPlayVideoClicked(post: Post) {
        val url: String = requireNotNull(post.video) {
            "URL is absent"
        }
        playVideo.value = url
    }

    override fun onPostClicked(post: Post) {
        navigateToOnePost.value = post.id
        postId = post.id
    }

    // endregion PostInteractionListener
}