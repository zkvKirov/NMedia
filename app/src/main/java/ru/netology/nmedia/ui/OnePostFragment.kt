package ru.netology.nmedia.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import ru.netology.nmedia.R
import ru.netology.nmedia.adapter.PostViewHolder
import ru.netology.nmedia.adapter.PostsAdapter
import ru.netology.nmedia.databinding.OnePostFragmentBinding
import ru.netology.nmedia.post.EditPostResult
import ru.netology.nmedia.viewModel.PostViewModel

class OnePostFragment : Fragment() {

    private val viewModel: PostViewModel by viewModels(
        ownerProducer = ::requireParentFragment
    )

    private val args by navArgs<OnePostFragmentArgs>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.sharePostContent.observe(this) { postContent ->
            val intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, postContent)
                type = "text/plain"
            }
            val shareIntent = Intent.createChooser(
                intent, getString(R.string.share_post)
            )
            startActivity(shareIntent)
        }

        viewModel.playVideo.observe(this) { videoUrl ->
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(videoUrl))
            startActivity(intent)
        }

        viewModel.navigateToPostContentScreenEvent.observe(this) {
            val direction = OnePostFragmentDirections.toPostContentFragment(it)
            findNavController().navigate(direction)
        }
        viewModel.navigateToFeedFragment.observe(this) {
            val direction = OnePostFragmentDirections.toFeedFragment()
            findNavController().navigate(direction)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = OnePostFragmentBinding.inflate(layoutInflater, container, false).also { binding ->
        val viewHolder = PostViewHolder(binding.onePost, viewModel)
        viewModel.data.observe(viewLifecycleOwner) { posts ->
            val post = posts.find { it.id == args.postId } ?: run {
                findNavController().navigateUp()
                return@observe
            }
            viewHolder.bind(post)
        }
        val adapter = PostsAdapter(viewModel)
        viewModel.data.observe(viewLifecycleOwner) { posts->
            adapter.submitList(posts)
        }
    }.root

    override fun onResume() {
        super.onResume()

        setFragmentResultListener(
            requestKey = PostContentFragment.REQUEST_KEY
        ) { requestKey, bundle ->
            if (requestKey != PostContentFragment.REQUEST_KEY) return@setFragmentResultListener
            val newPostContent = bundle[PostContentFragment.NEW_CONTENT].toString()
            val newPostVideoUrl = bundle[PostContentFragment.NEW_VIDEO_URL].toString()
            viewModel.onSaveButtonClicked(EditPostResult(newPostContent, newPostVideoUrl))
        }
    }
}