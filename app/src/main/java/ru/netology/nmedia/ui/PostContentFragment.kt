package ru.netology.nmedia.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import ru.netology.nmedia.EditPostResult
import ru.netology.nmedia.databinding.PostContentFragmentBinding

class PostContentFragment (
    private val initialContent: EditPostResult?
        ) : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = PostContentFragmentBinding.inflate(layoutInflater, container, false).also { binding ->
        binding.editContent.setText(initialContent?.newContent)
        binding.editUrl.setText(initialContent?.newVideoUrl)
        binding.editContent.requestFocus(0)
        binding.ok.setOnClickListener {
            onOkButtonClicked(binding)
        }
    }.root

    private fun onOkButtonClicked(binding: PostContentFragmentBinding) {
        if (!binding.editContent.text.isNullOrBlank()) {
            val resultBundle = Bundle(2)
            resultBundle.putString(NEW_CONTENT, binding.editContent.text.toString())
            resultBundle.putString(NEW_VIDEO_URL, binding.editUrl.text.toString())
            setFragmentResult(REQUEST_KEY, resultBundle)
            Toast.makeText(context, "Успех", Toast.LENGTH_SHORT).show()
        }
        parentFragmentManager.popBackStack()
    }

    companion object {
        const val REQUEST_KEY = "createPost"
        const val NEW_CONTENT = "newContent"
        const val NEW_VIDEO_URL = "newVideoUrl"
    }
}