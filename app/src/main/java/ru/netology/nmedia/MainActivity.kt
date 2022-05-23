package ru.netology.nmedia
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import ru.netology.nmedia.adapter.PostsAdapter
import ru.netology.nmedia.databinding.ActivityMainBinding

import ru.netology.nmedia.util.AndroidUtils
import ru.netology.nmedia.viewModel.PostViewModel

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val viewModel: PostViewModel by viewModels()
        val adapter = PostsAdapter(viewModel)

        binding.list.adapter = adapter
        viewModel.data.observe(this) { posts ->
            val newPost = posts.size > adapter.itemCount
            adapter.submitList(posts) {
                if (newPost) {
                    binding.list.smoothScrollToPosition(0)
                }
            }
            binding.group.visibility = View.INVISIBLE
        }

        binding.saveButton.setOnClickListener {
            with(binding.contentEditText) {
                val content = text.toString()
                viewModel.onSaveButtonClicked(content)
                binding.group.visibility = View.INVISIBLE
                AndroidUtils.hideKeyboard(this)
                clearFocus()
            }
        }

        binding.cancelButton.setOnClickListener {
            with(binding.contentEditText) {
                setText("")
                AndroidUtils.hideKeyboard(this)
                clearFocus()
            }
            binding.group.visibility = View.INVISIBLE
        }

        viewModel.currentPost.observe(this) { currentPost ->
            with(binding.contentEditText) {
                val content = currentPost?.content
                setText(content)
                if (content != null) {
                    requestFocus(0)
                    binding.group.visibility = View.VISIBLE
                    binding.editMessageText.text = content
                    AndroidUtils.showKeyboard(this)
                }
            }
        }
    }
}