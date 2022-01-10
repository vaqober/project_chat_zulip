package com.fintech.homework.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.fintech.homework.ChannelsViewModel
import com.fintech.homework.R
import com.fintech.homework.TopicsViewModel
import com.fintech.homework.adapters.ChannelListAdapter
import com.fintech.homework.adapters.Types
import com.fintech.homework.adapters.Types.VIEW_TYPE_CHANNEL
import com.fintech.homework.data.ChannelTopicItem
import com.fintech.homework.data.ScreenState
import com.fintech.homework.databinding.FragmentChannelsBinding
import com.fintech.homework.interfaces.OnChannelClickListener

class ChannelsFragment : Fragment(), OnChannelClickListener {

    private var _binding: FragmentChannelsBinding? = null
    private val binding get() = _binding!!
    private var channelsList = mutableListOf<ChannelTopicItem>()
    private val channelsListAdapter = ChannelListAdapter(channelsList, onTopicClicked(), this)
    private val viewModelChannel: ChannelsViewModel by viewModels()
    private val viewModelTopic: TopicsViewModel by viewModels()
    private val topics: MutableMap<Int, MutableList<ChannelTopicItem>> = mutableMapOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        _binding = FragmentChannelsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUi()
        if (savedInstanceState == null) viewModelChannel.searchChannels(ChannelsViewModel.INITIAL_QUERY)
    }

    private fun initUi() {
        binding.channels
        binding.channels.adapter = channelsListAdapter
        viewModelChannel.screenState.observe(viewLifecycleOwner) { processChannelScreenState(it) }
        viewModelTopic.screenState.observe(viewLifecycleOwner) { processTopicScreenState(it) }
        parentFragmentManager.setFragmentResultListener(
            "channelSearch",
            viewLifecycleOwner
        ) { _, bundle ->
            val searchQuery = bundle.getString("channel")
            if (searchQuery != null) {
                viewModelChannel.searchChannels(searchQuery)
            }
        }
    }

    override fun onChannelClick(position: Int, view: View) {
        if (!(channelsList.find { it.id == position && it.type == VIEW_TYPE_CHANNEL }?.isExpanded)!!) {
            view.findViewById<ImageView>(R.id.arrow).setImageResource(R.drawable.ic_arrow_expanded)
            if (topics[position].isNullOrEmpty()) {
                viewModelTopic.searchTopics(position)
            } else {
                topics[position]?.let { channelsListAdapter.expandTopics(it.toList()) }
            }
        } else {
            view.findViewById<ImageView>(R.id.arrow).setImageResource(R.drawable.ic_arrow_expand)
            val newList = channelsList.toMutableList()
            newList.removeAll {
                it.parentId == position
                        && it.type == Types.VIEW_TYPE_TOPIC
            }
            newList.find { it.id == position && it.type == VIEW_TYPE_CHANNEL }?.isExpanded = false
            channelsListAdapter.update(newList)
        }
    }

    private fun onTopicClicked() = View.OnClickListener {
        this.activity?.supportFragmentManager?.beginTransaction()
            ?.add(R.id.mainFragmentContainer, TopicFragment.newInstance(), "topic")
            ?.show(TopicFragment())
            ?.hide(ChannelsFragment())
            ?.addToBackStack("main")
            ?.commitAllowingStateLoss()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val ARG_TYPE = "type"

        fun newInstance(type: String) =
            ChannelsFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_TYPE, type)
                }
            }
    }

    private fun processChannelScreenState(it: ScreenState?) {
        when (it) {
            is ScreenState.Result -> {
                val newItems = it.items
                channelsListAdapter.update(newItems)
                binding.loadingProgress.isVisible = false
            }
            ScreenState.Loading -> {
                binding.loadingProgress.isVisible = true
            }
            is ScreenState.Error -> {
                Toast.makeText(this.context, it.error.message, Toast.LENGTH_SHORT).show()
                binding.loadingProgress.isVisible = false
            }
        }
    }

    private fun processTopicScreenState(it: ScreenState?) {
        when (it) {
            is ScreenState.Result -> {
                val newItems = it.items
                topics[it.items[0].parentId] = it.items.toMutableList()
                channelsListAdapter.expandTopics(newItems)
                binding.loadingProgress.isVisible = false
            }
            ScreenState.Loading -> {
                binding.loadingProgress.isVisible = true
            }
            is ScreenState.Error -> {
                Toast.makeText(this.context, it.error.message, Toast.LENGTH_SHORT).show()
                binding.loadingProgress.isVisible = false
            }
        }
    }
}