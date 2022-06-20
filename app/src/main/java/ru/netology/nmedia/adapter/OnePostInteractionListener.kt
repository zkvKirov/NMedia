package ru.netology.nmedia.adapter

interface OnePostInteractionListener {
    fun onLikeClicked()
    fun onShareClicked()
    fun onRemoveClicked()
    fun onEditClicked()
    fun onPlayVideoClicked()
}