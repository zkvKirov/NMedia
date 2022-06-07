package ru.netology.nmedia.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContract
import androidx.appcompat.app.AppCompatActivity
import ru.netology.nmedia.databinding.PostContentActivityBinding
import ru.netology.nmedia.viewModel.PostViewModel

class PostContentActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = PostContentActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.edit.setText(intent?.getStringExtra(RESULT_KEY))
        binding.edit.requestFocus(0)
        binding.ok.setOnClickListener {
            val intent = Intent()
            val text = binding.edit.text
            if (text.isNullOrBlank()) {
                setResult(Activity.RESULT_CANCELED, intent)
            } else {
                val content = text.toString()
                intent.putExtra(RESULT_KEY, content)
                setResult(Activity.RESULT_OK, intent)
                Toast.makeText(this, "Успех", Toast.LENGTH_SHORT).show()
            }
            finish()
        }
    }

    object ResultContract : ActivityResultContract<String?, String?>() {

        override fun createIntent(context: Context, input: String?): Intent {
            val intent = Intent(context, PostContentActivity::class.java)
            intent.putExtra(RESULT_KEY, input)
            return intent
        }

        override fun parseResult(resultCode: Int, intent: Intent?): String? = when {
            resultCode != Activity.RESULT_OK -> null
            else -> intent?.getStringExtra(RESULT_KEY)
        }
    }

    companion object {
        const val RESULT_KEY = "postNewContent"
    }
}