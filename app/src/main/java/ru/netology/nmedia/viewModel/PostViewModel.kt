package ru.netology.nmedia.viewModel

import androidx.lifecycle.ViewModel
import ru.netology.nmedia.Post
import ru.netology.nmedia.repository.PostRepository
import ru.netology.nmedia.repository.PostRepositoryInMemoryImpl

class PostViewModel : ViewModel() {

    private val repository: PostRepository = PostRepositoryInMemoryImpl()

    val data = repository.getAll()
    fun onLikeClicked(id: Long) = repository.like(id)
    fun onShareClicked(id: Long) = repository.share(id)
}