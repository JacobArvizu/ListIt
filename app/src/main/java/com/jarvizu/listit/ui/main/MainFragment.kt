package com.jarvizu.listit.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.jarvizu.listit.data.ListAdapter
import com.jarvizu.listit.data.local.Status
import com.jarvizu.listit.databinding.FragmentMainBinding
import com.skydoves.whatif.whatIfNotNull
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class MainFragment : Fragment() {

    private var loading = true
    var pastVisiblesItems = 0
    var visibleItemCount:Int = 0
    var totalItemCount:Int = 0

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MainViewModel by viewModels()
    private val adapter = ListAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Timber.d("MainFragment Created:")
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.rvRecycleView.layoutManager = LinearLayoutManager(requireActivity())
        binding.rvRecycleView.adapter = adapter
        viewModel.repo.observe(viewLifecycleOwner) { redditResource ->
            when (redditResource.status) {
                Status.SUCCESS -> {
                    Timber.d("Data loaded")
                    redditResource.data?.data?.children.whatIfNotNull { child ->
                        adapter.submitList(child)
                    }
                    loading = true
                }
                Status.LOADING -> {
                    Timber.d("Loading more data...")
                }
                Status.ERROR -> {
                    Timber.d("List Error!")
                }
            }
        }
        // Custom Pagination Handling
        binding.rvRecycleView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, lengthx: Int, lengthY: Int) {
                val layoutManager : LinearLayoutManager = binding.rvRecycleView.layoutManager as LinearLayoutManager
                if (lengthY > 0)
                {
                    visibleItemCount = layoutManager.childCount
                    totalItemCount = layoutManager.itemCount
                    pastVisiblesItems = layoutManager.findFirstVisibleItemPosition()
                    if (loading) {
                        if (visibleItemCount + pastVisiblesItems >= totalItemCount) {
                            loading = false
                            Timber.d("...", "End of list")
                            Snackbar.make(binding.relativeLayout, "Loading Additional Posts...", Snackbar.LENGTH_SHORT)
                                .show()
                            viewModel.getList()
                        }
                    }
                }
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}

