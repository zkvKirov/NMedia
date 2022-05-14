package ru.netology.nmedia

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import ru.netology.nmedia.databinding.ActivityMainBinding
import ru.netology.nmedia.viewModel.PostViewModel
import kotlin.math.round

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val viewModel: PostViewModel by viewModels()
        viewModel.data.observe(this) { post ->
            with(binding) {
                authorName.text = post.author
                datePublished.text = post.published
                content.text = post.content
                countOfLikes.text = displayLikes(post.likes)
                countOfShare.text = post.share.toString()
                like.setImageResource(
                    if (post.likedByMe) R.drawable.ic_favorite_24 else R.drawable.ic_favorite_border_24
                )
            }
        }

        binding.like.setOnClickListener {
            viewModel.onLikeClicked()
        }

        binding.share.setOnClickListener {
            viewModel.onShareClicked()
        }

    }

    private fun displayLikes (like: Int): String {
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
                likes = round((like / 1000000.toDouble())* 10.0) / 10.0
                "$likes" + "M"
            }
        }
        return result
    }
}