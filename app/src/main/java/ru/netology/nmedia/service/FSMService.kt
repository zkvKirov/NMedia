package ru.netology.nmedia.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.google.gson.Gson
import ru.netology.nmedia.Post
import ru.netology.nmedia.R
import kotlin.random.Random

class FSMService : FirebaseMessagingService() {

    private val gson = Gson()

    // в данном методе происходит регистрация канала для показа уведомлений
    override fun onCreate() {
        super.onCreate()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = getString(R.string.channel_remote_name)
            val descriptionText = getString(R.string.channel_remote_description)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channel)
        }
    }

    override fun onMessageReceived(message: RemoteMessage) {
        val data = message.data
        val serializedAction = data[Action.KEY] ?: return

        when(Action.values().find { it.key == serializedAction }) {
            Action.Like -> handleLikeAction(data[CONTENT_KEY] ?: return)
            Action.NewPost -> handleNewPostAction(data[CONTENT_KEY] ?: return)
            else -> Log.d("onMessageReceived", gson.toJson(message))
        }
    }

    override fun onNewToken(token: String) {
        Log.d("onNewToken", token)
        println(token)
        // faYbegiQSvuCQsQRD6lk86:APA91bGZ24EGhuAQUNElNlKMSIFocDCL7MDsOMwsY-wfVyYYCU3l1CgxzYZiYppoKse3ZHeMtScxDOxKnuy5MEy5RZWCvjwThEpHPB5IfzSEkaMIq3SnX3UNlUOXjNGQLsjut6QwCnqO
    }

    private fun handleLikeAction(content: String) {
        val likeContent = gson.fromJson(content, Like::class.java)
        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle(
                getString(
                    R.string.notification_user_liked,
                    likeContent.userName,
                    likeContent.postAuthor
                )
            )
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()

        NotificationManagerCompat.from(this)
            .notify(Random.nextInt(100_000), notification)
    }

    private fun handleNewPostAction(content: String) {
        val postContent = gson.fromJson(content, Post::class.java)
        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle(
                getString(
                    R.string.notification_new_post,
                    postContent.author
                )
            )
            .setContentText(postContent.content)
            .setStyle(NotificationCompat.BigTextStyle()
                .bigText(postContent.content))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()

        NotificationManagerCompat.from(this)
            .notify(Random.nextInt(100_000), notification)
    }

    companion object {
        const val CONTENT_KEY = "content"
        const val CHANNEL_ID = "remote"
    }
}