package com.fintech.homework.presentation.streams

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.fintech.homework.R
import com.fintech.homework.databinding.FragmentStreamsBinding
import com.fintech.homework.presentation.MainFragment
import com.fintech.homework.presentation.streams.listeners.OnStreamClickListener
import com.fintech.homework.presentation.streams.listeners.OnTopicClickedListener
import com.fintech.homework.presentation.streams.objects.StreamTopicItem
import com.fintech.homework.presentation.streams.objects.Types
import com.fintech.homework.presentation.streams.objects.Types.VIEW_TYPE_CHANNEL
import com.fintech.homework.presentation.topic.TopicFragment
import com.fintech.homework.presentation.topic.TopicsViewModel

class StreamsFragment : Fragment(), OnStreamClickListener, OnTopicClickedListener {

    private var _binding: FragmentStreamsBinding? = null
    private val binding get() = _binding!!
    private var streamsList = mutableListOf<StreamTopicItem>()
    private val streamsListAdapter = StreamListAdapter(streamsList, this, this)
    private val viewModelStream: StreamsViewModel by viewModels()
    private val viewModelTopic: TopicsViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        _binding = FragmentStreamsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUi()
        if (savedInstanceState == null)
            viewModelStream.searchStreamsInDB(StreamsViewModel.INITIAL_QUERY)
    }

    private fun initUi() {
        binding.channels
        binding.channels.adapter = streamsListAdapter
        observeModels()
        parentFragmentManager.setFragmentResultListener(
            "search",
            viewLifecycleOwner
        ) { _, bundle ->
            val searchQuery = bundle.getString("searchString")
            if (searchQuery != null) {
                viewModelStream.searchStreamsInDB(searchQuery)
            }
        }
    }

    private fun observeModels() {
        viewModelStream.streamsScreenState.observe(viewLifecycleOwner) {
            processStreamScreenState(
                it
            )
        }
        viewModelStream.streamsDBScreenState.observe(viewLifecycleOwner) {
            processDBStreamScreenState(
                it
            )
        }
        viewModelTopic.streamsScreenState.observe(viewLifecycleOwner) { processTopicScreenState(it) }
        viewModelTopic.streamsDBScreenState.observe(viewLifecycleOwner) {
            processDBTopicScreenState(
                it
            )
        }
    }

    override fun onStreamClick(streamId: Long, view: View) {
        if (!(streamsList.find { it.id == streamId && it.type == VIEW_TYPE_CHANNEL }?.isExpanded)!!) {
            view.findViewById<ImageView>(R.id.arrow).setImageResource(R.drawable.ic_arrow_expanded)
            viewModelTopic.searchTopicsDB(streamId)
        } else {
            view.findViewById<ImageView>(R.id.arrow).setImageResource(R.drawable.ic_arrow_expand)
            val newList = streamsList.toMutableList()
            newList.removeAll {
                it.parentId == streamId && it.type == Types.VIEW_TYPE_TOPIC
            }
            newList.find { it.id == streamId && it.type == VIEW_TYPE_CHANNEL }?.isExpanded = false
            streamsListAdapter.update(newList)
        }
    }

    override fun onTopicClicked(stream: Long, subject: String) {
        val topicFragment = TopicFragment.newInstance(stream, subject)
        requireActivity().supportFragmentManager.beginTransaction()
            .add(R.id.mainFragmentContainer, TopicFragment.newInstance(stream, subject), "topic")
            .show(topicFragment)
            .hide(StreamsTabFragment())
            .hide(MainFragment())
            .addToBackStack("main")
            .commitAllowingStateLoss()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val ARG_TYPE = "type"

        fun newInstance(type: String) =
            StreamsFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_TYPE, type)
                }
            }
    }

    private fun processStreamScreenState(it: StreamsScreenState?) {
        when (it) {
            is StreamsScreenState.Result -> {
                val newItems = it.items
                streamsListAdapter.update(newItems)
                binding.loadingProgress.isVisible = false
                viewModelStream.insertStreamsInDB(newItems)
            }
            StreamsScreenState.Loading -> {
                binding.loadingProgress.isVisible = true
            }
            is StreamsScreenState.Error -> {
                Toast.makeText(this.context, it.error.message, Toast.LENGTH_SHORT).show()
                binding.loadingProgress.isVisible = false
            }
        }
    }

    private fun processDBStreamScreenState(it: StreamsScreenState?) {
        when (it) {
            is StreamsScreenState.Result -> {
                val newItems = it.items
                if (newItems.isNotEmpty()) {
                    streamsListAdapter.update(newItems)
                    binding.loadingProgress.isVisible = false
                }
            }
            StreamsScreenState.Loading -> {
            }
            is StreamsScreenState.Error -> {
                Toast.makeText(this.context, it.error.message, Toast.LENGTH_SHORT).show()
                binding.loadingProgress.isVisible = false
            }
        }
    }

    private fun processTopicScreenState(it: StreamsScreenState?) {
        when (it) {
            is StreamsScreenState.Result -> {
                val newItems = it.items
                if (newItems.isNotEmpty()) {
                    streamsListAdapter.expandTopics(newItems)
                    viewModelTopic.insertTopics(newItems)
                } else {
                    Toast.makeText(
                        this.context,
                        "There is no topics for this stream",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                binding.loadingProgress.isVisible = false
            }
            StreamsScreenState.Loading -> {
                binding.loadingProgress.isVisible = true
            }
            is StreamsScreenState.Error -> {
                Toast.makeText(this.context, it.error.message, Toast.LENGTH_SHORT).show()
                binding.loadingProgress.isVisible = false
            }
        }
    }

    private fun processDBTopicScreenState(it: StreamsScreenState?) {
        when (it) {
            is StreamsScreenState.Result -> {
                val newItems = it.items
                if (newItems.isNotEmpty()) {
                    streamsListAdapter.expandTopics(newItems)
                    binding.loadingProgress.isVisible = false
                }
            }
            StreamsScreenState.Loading -> {
            }
            is StreamsScreenState.Error -> {
                Toast.makeText(this.context, it.error.message, Toast.LENGTH_SHORT).show()
                binding.loadingProgress.isVisible = false
            }
        }
    }
}