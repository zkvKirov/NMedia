package ru.netology.nmedia.db

import android.database.Cursor
import androidx.core.database.getStringOrNull
import ru.netology.nmedia.Post

fun Cursor.toPost() = Post(
    id = getLong(getColumnIndexOrThrow(PostsTable.Column.ID.columnName)),
    author = getString(getColumnIndexOrThrow(PostsTable.Column.AUTHOR.columnName)),
    content = getString(getColumnIndexOrThrow(PostsTable.Column.CONTENT.columnName)),
    published = getString(getColumnIndexOrThrow(PostsTable.Column.PUBLISHED.columnName)),
    likedByMe = getInt(getColumnIndexOrThrow(PostsTable.Column.LIKED_BY_ME.columnName)) != 0,
    likes = getInt(getColumnIndexOrThrow(PostsTable.Column.LIKES.columnName)),
    share = getInt(getColumnIndexOrThrow(PostsTable.Column.SHARE.columnName)),
    video = getStringOrNull(getColumnIndexOrThrow(PostsTable.Column.VIDEO.columnName))
)