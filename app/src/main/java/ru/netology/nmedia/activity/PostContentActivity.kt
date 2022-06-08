package ru.netology.nmedia.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContract
import androidx.appcompat.app.AppCompatActivity
import ru.netology.nmedia.EditPostResult
import ru.netology.nmedia.databinding.PostContentActivityBinding
import ru.netology.nmedia.viewModel.PostViewModel

class PostContentActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = PostContentActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.editContent.setText(intent?.getStringExtra("NewContent"))
        binding.editUrl.setText(intent?.getStringExtra("newVideoUrl"))
        binding.editContent.requestFocus(0)
        binding.ok.setOnClickListener {
            val intent = Intent()
            val text = binding.editContent.text
            if (text.isNullOrBlank()) {
                setResult(Activity.RESULT_CANCELED, intent)
            } else {
                val content = text.toString()
                intent.apply {
                    putExtra("NewContent", content)
                    putExtra("newVideoUrl", binding.editUrl.text.toString())
                }
                setResult(Activity.RESULT_OK, intent)
                Toast.makeText(this, "Успех", Toast.LENGTH_SHORT).show()
            }
            finish()
        }
    }

    object ResultContract : ActivityResultContract<EditPostResult?, EditPostResult?>() {

        override fun createIntent(context: Context, input: EditPostResult?): Intent {
            val intent = Intent(context, PostContentActivity::class.java)
            intent.apply {
                putExtra("NewContent", input)
                putExtra("newVideoUrl", input)
            }
            return intent
        }

        override fun parseResult(resultCode: Int, intent: Intent?): EditPostResult? = when {
            resultCode != Activity.RESULT_OK -> null
            else -> EditPostResult(
                newContent = intent?.getStringExtra("NewContent"),
                newVideoUrl = intent?.getStringExtra("newVideoUrl")
            )
        }
    }

//    companion object {
//        const val RESULT_KEY = "postNewContent"
//    }
}