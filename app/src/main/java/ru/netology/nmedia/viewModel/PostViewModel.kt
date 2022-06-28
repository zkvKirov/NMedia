package ru.netology.nmedia.viewModel

import android.app.AlertDialog
import android.app.Application
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import ru.netology.nmedia.R
import ru.netology.nmedia.post.EditPostResult
import ru.netology.nmedia.post.Post
import ru.netology.nmedia.adapter.PostInteractionListener
import ru.netology.nmedia.repository.PostRepository
import ru.netology.nmedia.repository.impl.FilePostRepository
import ru.netology.nmedia.util.SingleLiveEvent
import kotlin.properties.Delegates

class PostViewModel(
    application: Application
) : AndroidViewModel(application), PostInteractionListener {

    private val repository: PostRepository = FilePostRepository(application)

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

    fun onAddButtonClicked() {
        navigateToPostContentScreenEvent.call()
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

//    override fun onRemoveClicked(post: Post) {
//        val builder = AlertDialog.Builder(getApplication())
//        builder.setTitle("Delete post")
//            .setIcon(R.drawable.ic_clear_24)
//            .setMessage("Are you really want delete post?")
//            .setPositiveButton("OK") { _, _ ->
//                repository.remove(post.id)
//                Toast.makeText(getApplication(), "Post was deleted", Toast.LENGTH_SHORT).show()
//            }
//            .setNegativeButton("Cancel") {dialog, _ ->
//                dialog.cancel()
//            }
//        val alertDialog: AlertDialog = builder.create()
//        alertDialog.setCancelable(false)
//        alertDialog.show()
//    }

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
        currentPost.value = post
        postId = post.id
    }

    // endregion PostInteractionListener
}