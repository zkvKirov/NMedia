package ru.netology.nmedia.util

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import kotlin.math.round

object AndroidUtils {
    fun hideKeyboard(view: View) {
        val imm = view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, /* flags = */0)
    }

    fun showKeyboard(view: View) {
        val imm = view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(view, /* flags = */0)
    }

}

internal fun displayLikes(like: Int): String {
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