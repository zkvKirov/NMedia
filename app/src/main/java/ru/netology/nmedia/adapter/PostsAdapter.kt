package ru.netology.nmedia.adapter

import android.app.AlertDialog
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.netology.nmedia.post.Post
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.PostCardBinding
import ru.netology.nmedia.util.displayLikes

class PostsAdapter(
    private val interactionListener: PostInteractionListener
) : ListAdapter<Post, PostViewHolder>(PostDiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = PostCardBinding.inflate(inflater, parent, false)
        return PostViewHolder(binding, interactionListener)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    private object PostDiffCallback : DiffUtil.ItemCallback<Post>() {
        override fun areItemsTheSame(oldItem: Post, newItem: Post) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Post, newItem: Post) =
            oldItem == newItem
    }
}

class PostViewHolder(
    private val binding: PostCardBinding,
    listener: PostInteractionListener
) : RecyclerView.ViewHolder(binding.root) {

    private lateinit var post: Post

    private val popupMenu by lazy {
        PopupMenu(itemView.context, binding.menu).apply {
            inflate(R.menu.option_post)
            setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.remove -> {
                        val builder = AlertDialog.Builder(itemView.context)
                        builder.setTitle("Delete post")
                            .setIcon(R.drawable.ic_clear_24)
                            .setMessage("Are you really want delete post?")
                            .setPositiveButton("OK") { _, _ ->
                                listener.onRemoveClicked(post)
                                Toast.makeText(itemView.context, "Post was deleted", Toast.LENGTH_SHORT).show()
                            }
                            .setNegativeButton("Cancel") {dialog, _ ->
                                dialog.cancel()
                            }
                        val alertDialog: AlertDialog = builder.create()
                        alertDialog.setCancelable(false)
                        alertDialog.show()
                        true
                    }
                    R.id.edit -> {
                        listener.onEditClicked(post)
                        true
                    }
                    else -> false
                }
            }
        }
    }

    init {
        binding.menu.setOnClickListener {
            popupMenu.show()
        }
        binding.videoContent.setOnClickListener {
            listener.onPlayVideoClicked(post)
        }
        binding.playButton.setOnClickListener {
            listener.onPlayVideoClicked(post)
        }
        binding.like.setOnClickListener {
            listener.onLikeClicked(post)
        }
        binding.share.setOnClickListener {
            listener.onShareClicked(post)
        }
        binding.groupPost.setOnClickListener {
            listener.onPostClicked(post)
        }
    }

    fun bind(post: Post) {
        this.post = post
        with(binding) {
            authorName.text = post.author
            datePublished.text = post.published
            content.text = post.content
            like.apply {
                text = displayLikes(post.likes)
                isChecked = post.likedByMe
            }
            share.text = post.share.toString()
            groupVideo.isVisible = post.video != null && post.video.isNotBlank()
        }
    }
}