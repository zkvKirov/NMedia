package ru.netology.nmedia

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import ru.netology.nmedia.databinding.ActivityMainBinding
import kotlin.math.round

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        val likeButton = findViewById<ImageButton>(R.id.like) // старый способ
//        likeButton.setOnClickListener {
//            println("Like clicked")
//        }

        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val post = Post(
            id = 1,
            author = "Костян",
            content = "Привет, это моё новое приложение, жми чтобы скачать → http://netolo.gy/fyb",
            published = "9 мая в 16:55",
            likes = 999
        )

        // код с вебинара
//        binding.render(post)
//        binding.like.setOnClickListener { // новый способ
//            post.likedByMe = !post.likedByMe//
//            binding.like.setImageResource(getLikeIcon(post.likedByMe))
//        }

        // код из презентации (другой вариант реализации кода с вебинара)
        with(binding) {
            authorName.text = post.author
            datePublished.text = post.published
            content.text = post.content
            countOfLikes.text = post.likes.toString()
            if (post.likedByMe) like.setImageResource(R.drawable.ic_favorite_24)

            like.setOnClickListener {
                post.likedByMe = !post.likedByMe
                like.setImageResource(
                    if (post.likedByMe) R.drawable.ic_favorite_24 else R.drawable.ic_favorite_border_24
                )
                if (post.likedByMe) post.likes++ else post.likes--
                countOfLikes.text = displayLikes(post.likes)
            }

            share.setOnClickListener {
                post.share++
                countOfShare.text = post.share.toString()
            }


        }
    }

    // код с вебинара
//    private fun ActivityMainBinding.render (post: Post) {
//        authorName.text = post.author
//        datePublished.text = post.published
//        content.text = post.content
//        like.setImageResource (getLikeIcon(post.likedByMe))
//    }
//
//    @DrawableRes
//    private fun getLikeIcon (liked: Boolean) =
//        if (liked) R.drawable.ic_favorite_24 else R.drawable.ic_favorite_border_24

    private fun displayLikes (like: Int): String {
        val likes: Any
        val result: String = if (like in 0..999) {
            "$like"
        } else if (like in 1000..1099) {
            likes = like / 1000
            "$likes" + "K"
        } else if (like in 1100..9999) {
            likes = round((like / 1000.toDouble())* 10.0) / 10.0
            "$likes" + "K"
        } else if (like in 10000..999999) {
            likes = like / 1000
            "$likes" + "K"
        } else if (like in 1000000..1099999) {
            likes = like / 1000000
            "$likes" + "M"
        } else {
            likes = round((like / 1000000.toDouble())* 10.0) / 10.0
            "$likes" + "M"
        }
        return result
    }

}