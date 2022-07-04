package ru.netology.nmedia.repository.impl

import androidx.lifecycle.Transformations
import ru.netology.nmedia.db.PostDao
import ru.netology.nmedia.db.toEntity
import ru.netology.nmedia.db.toModel
import ru.netology.nmedia.post.Post
import ru.netology.nmedia.repository.PostRepository

class PostRepositoryImpl(
    private val dao: PostDao
) : PostRepository {

    override fun getAll() = Transformations.map(dao.getAll()) { entities ->
        entities.map {
            it.toModel()
        }
    }

    override fun save(post: Post) {
        if (post.id == PostRepository.NEW_POST_ID) dao.insert(post.toEntity())
        else dao.updateContentById(post.id, post.content, post.video)
    }

    override fun remove(postId: Long) = dao.removeById(postId)

    override fun like(postId: Long) = dao.likeById(postId)

    override fun share(postId: Long) = dao.shareById(postId)

}