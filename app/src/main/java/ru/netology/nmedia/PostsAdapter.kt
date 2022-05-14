package ru.netology.nmedia

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.netology.nmedia.databinding.PostCardBinding
import kotlin.math.round

typealias OnLikeListener = (post: Post) -> Unit
typealias OnShareListener = (post: Post) -> Unit

class PostsAdapter(
    //private val onLikeListener: OnLikeListener
    private val onShareListener: OnShareListener
) : RecyclerView.Adapter<PostViewHolder>() {
        var list = emptyList<Post>()
            set(value) {
                field = value
                notifyDataSetChanged()
            }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = PostCardBinding.inflate(inflater, parent, false)
        return PostViewHolder(binding, onShareListener)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int = list.size
}

class PostViewHolder(
    private val binding: PostCardBinding,
    //private val onLikeListener: OnLikeListener
    private val onShareListener: OnShareListener
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(post: Post) {
        binding.apply {
            authorName.text = post.author
            datePublished.text = post.published
            content.text = post.content
            //countOfLikes.text = displayLikes(post.likes)
            countOfShare.text = post.share.toString()
            like.setImageResource(
                if (post.likedByMe) R.drawable.ic_favorite_24 else R.drawable.ic_favorite_border_24
            )
//            like.setOnClickListener {
//                onLikeListener(post)
//            }
            share.setOnClickListener {
                onShareListener(post)
            }
        }
    }

    private fun displayLikes(like: Int): String {
        val likes: Any
        val result: String = when (like) {
            in 0..999 -> "$like"
            in 1000..1099 -> {
                likes = like / 1000
                "$likes" + "K"
            }
            in 1100..9999 -> {
                likes = round((like / 1000.toDouble()) * 10.0) / 10.0
                "$likes" + "K"
            }
            in 10000..999999 -> {
                likes = like / 1000
                "$likes" + "K"
            }
            in 1000000..1099999 -> {
                likes = like / 1000000
                "$likes" + "M"
            }
            else -> {
                likes = round((like / 1000000.toDouble()) * 10.0) / 10.0
                "$likes" + "M"
            }
        }
        return result
    }
}
