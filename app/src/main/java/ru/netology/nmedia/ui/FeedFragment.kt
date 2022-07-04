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
import ru.netology.nmedia.post.EditPostResult
import ru.netology.nmedia.R
import ru.netology.nmedia.adapter.PostsAdapter
import ru.netology.nmedia.databinding.FeedFragmentBinding
import ru.netology.nmedia.ui.PostContentFragment.Companion.NEW_CONTENT
import ru.netology.nmedia.ui.PostContentFragment.Companion.NEW_VIDEO_URL
import ru.netology.nmedia.viewModel.PostViewModel

class FeedFragment : Fragment() {

    private val viewModel: PostViewModel by viewModels(
        ownerProducer = ::requireParentFragment
    )

    private var draft: EditPostResult? = null

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

        setFragmentResultListener(
            requestKey = PostContentFragment.DRAFT_KEY
        ) { requestKey, bundle ->
            if (requestKey != PostContentFragment.DRAFT_KEY) return@setFragmentResultListener
            val newPostContent = bundle[NEW_CONTENT].toString()
            val newPostVideoUrl = bundle[NEW_VIDEO_URL].toString()
            draft = EditPostResult(newPostContent, newPostVideoUrl)
        }

        viewModel.navigateToPostContentScreenEvent.observe(this) {
            val direction = FeedFragmentDirections.toPostContentFragment(it)
            findNavController().navigate(direction)
        }

        viewModel.navigateToOnePost.observe(this) {
            val direction = FeedFragmentDirections.toOnePostFragment(it)
            findNavController().navigate(direction)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = FeedFragmentBinding.inflate(layoutInflater, container, false).also { binding ->
        val adapter = PostsAdapter(viewModel)
        binding.list.adapter = adapter
        viewModel.data.observe(viewLifecycleOwner) { posts ->
            val newPost = posts.size > adapter.itemCount
            adapter.submitList(posts) {
                if (newPost) {
                    binding.list.smoothScrollToPosition(0)
                }
            }
        }
        binding.fab.setOnClickListener {
            viewModel.onAddButtonClicked(draft)
        }
    }.root

    override fun onResume() {
        super.onResume()

        setFragmentResultListener(
            requestKey = PostContentFragment.REQUEST_KEY
        ) { requestKey, bundle ->
            if (requestKey != PostContentFragment.REQUEST_KEY) return@setFragmentResultListener
            val newPostContent = bundle[NEW_CONTENT].toString()
            val newPostVideoUrl = bundle[NEW_VIDEO_URL].toString()
            viewModel.onSaveButtonClicked(EditPostResult(newPostContent, newPostVideoUrl))
            draft = null
        }
    }

}