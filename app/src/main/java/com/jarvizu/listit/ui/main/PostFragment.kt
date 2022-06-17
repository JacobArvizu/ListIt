package com.jarvizu.listit.ui.main

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.jarvizu.listit.data.CommentAdapter
import com.jarvizu.listit.data.local.Status
import com.jarvizu.listit.databinding.FragmentPostBinding
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class PostFragment : Fragment() {
    private var _binding: FragmentPostBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MainViewModel by viewModels()
    private val commentList = mutableListOf<String>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        _binding = FragmentPostBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        /*
            Use the navigation arguments to set the post data
         */
        binding.toolbarComments.title = "Post: " + arguments?.get("Sub")
        binding.txtPostTitle.text = "Title:\n" + arguments?.get("PostTitle").toString()
        if (!TextUtils.isEmpty(arguments?.get("SelfPost").toString())) {
            binding.selfTextCard.visibility = View.VISIBLE
            binding.txtSelf.text = "Self Text:\n" + arguments?.get("SelfPost").toString()
        } else {
            binding.selfTextCard.visibility = View.INVISIBLE
        }

        // Get Comments
        val permaLink = arguments?.get("Permalink").toString()
        Timber.d(permaLink)
        viewModel.getComments(permaLink)

        binding.rvComments.layoutManager = LinearLayoutManager(requireActivity())

        viewModel.comments.observe(viewLifecycleOwner) { redditResource ->
            when (redditResource.status) {
                Status.SUCCESS -> {
                    Timber.d(redditResource.data?.data?.body.toString())
                    // Add comment body
                    redditResource?.data?.data?.let { commentList.add(it.body) }
                }
                Status.LOADING -> {
                    Timber.d("Loading more data...")
                }
                Status.ERROR -> {
                    Timber.d("List Error!")
                }
            }
            binding.rvComments.adapter = CommentAdapter(commentList)
        }
    }


    override fun onDestroy() {
        super.onDestroy()
    }

}

