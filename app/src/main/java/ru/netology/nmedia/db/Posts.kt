package ru.netology.nmedia.db

import ru.netology.nmedia.post.Post

internal fun PostEntity.toModel() = Post(
    id = id,
    author = author,
    content = content,
    published = published,
    likedByMe = likedByMe,
    likes = likes,
    share = share,
    video = video
)

internal fun Post.toEntity() = PostEntity(
    id = id,
    author = author,
    content = content,
    published = published,
    likedByMe = likedByMe,
    likes = likes,
    share = share,
    video = video
)