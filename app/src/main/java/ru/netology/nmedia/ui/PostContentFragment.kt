package ru.netology.nmedia.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import ru.netology.nmedia.databinding.PostContentFragmentBinding

class PostContentFragment : Fragment() {

    private val args by navArgs<PostContentFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = PostContentFragmentBinding.inflate(layoutInflater, container, false).also { binding ->
        binding.editContent.setText(args.initialContent?.newContent)
        binding.editUrl.setText(args.initialContent?.newVideoUrl)
        binding.editContent.requestFocus(0)
        binding.ok.setOnClickListener {
            onOkButtonClicked(binding)
        }
        val callback = requireActivity().onBackPressedDispatcher.addCallback(this) {
            val draft = Bundle(2)
            draft.putString(NEW_CONTENT, binding.editContent.text.toString())
            draft.putString(NEW_VIDEO_URL, binding.editUrl.text.toString())
            setFragmentResult(DRAFT_KEY, draft)
            Toast.makeText(context, "черновик сообщения сохранён", Toast.LENGTH_SHORT).show()
            findNavController().popBackStack()
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
        findNavController().popBackStack()
    }

    companion object {
        const val REQUEST_KEY = "createPost"
        const val DRAFT_KEY = "draftPost"
        const val NEW_CONTENT = "newContent"
        const val NEW_VIDEO_URL = "newVideoUrl"
    }
}