package com.fintech.homework.presentation.streams

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import com.fintech.homework.R
import com.fintech.homework.databinding.FragmentStreamsBinding
import com.fintech.homework.di.StreamDI
import com.fintech.homework.presentation.MainFragment
import com.fintech.homework.presentation.streams.StreamsTabFragment.Companion.TABS
import com.fintech.homework.presentation.streams.elm.Effect
import com.fintech.homework.presentation.streams.elm.Event
import com.fintech.homework.presentation.streams.elm.SearchQuery
import com.fintech.homework.presentation.streams.elm.State
import com.fintech.homework.presentation.streams.elm.StoreFactoryStreams.Companion.INITIAL_QUERY
import com.fintech.homework.presentation.streams.listeners.OnStreamClickListener
import com.fintech.homework.presentation.streams.listeners.OnTopicClickedListener
import com.fintech.homework.presentation.streams.objects.StreamTopicItem
import com.fintech.homework.presentation.streams.objects.Types
import com.fintech.homework.presentation.streams.objects.Types.VIEW_TYPE_CHANNEL
import com.fintech.homework.presentation.topic.TopicFragment
import vivid.money.elmslie.android.base.ElmFragment
import vivid.money.elmslie.core.store.Store

class StreamsFragment : ElmFragment<Event, Effect, State>(), OnStreamClickListener,
    OnTopicClickedListener {

    private var _binding: FragmentStreamsBinding? = null
    private val binding get() = _binding!!
    private var streamsList = mutableListOf<StreamTopicItem>()
    private val streamsListAdapter = StreamListAdapter(streamsList, this, this)
    private val viewModelStream: StreamsViewModel by viewModels()


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
        viewModelStream.store = store
        if (savedInstanceState == null) {
            viewModelStream.searchStreamsInDB(SearchQuery(INITIAL_QUERY, isSubscribed()))
        }
    }

    private fun initUi() {
        binding.streams
        binding.streams.adapter = streamsListAdapter
        parentFragmentManager.setFragmentResultListener(
            "search",
            viewLifecycleOwner
        ) { _, bundle ->
            val searchQuery = bundle.getString("searchString")
            if (searchQuery != null) {
                viewModelStream.searchStreamsInDB(SearchQuery(searchQuery, isSubscribed()))
            }
        }
    }

    override fun onStreamClick(streamId: Long, view: View) {
        if (!(streamsList.find { it.id == streamId && it.type == VIEW_TYPE_CHANNEL }?.isExpanded)!!) {
            view.findViewById<ImageView>(R.id.arrow).setImageResource(R.drawable.ic_arrow_expanded)
            store.accept(Event.Ui.LoadTopics(stream = streamId))
        } else {
            view.findViewById<ImageView>(R.id.arrow).setImageResource(R.drawable.ic_arrow_expand)
            val newList = streamsList.toMutableList()
            newList.removeAll {
                it.parentId == streamId && it.type == Types.VIEW_TYPE_TOPIC
            }
            store.accept(Event.Ui.LoadTopics(stream = streamId))
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

    override val initEvent: Event = (
            Event.Ui.LoadStreams(SearchQuery(INITIAL_QUERY, true)))

    override fun createStore(): Store<Event, Effect, State> = (
            if (isSubscribed()) {
                StreamDI.INSTANCE.subsStreamStoreFactory.provide()
            } else {
                StreamDI.INSTANCE.allStreamStoreFactory.provide()
            })


    override fun render(state: State) {
        binding.loadingProgress.isVisible = state.isLoading
        binding.streams.isVisible = state.isEmptyState.not()
        streamsListAdapter.update(state.streams)
        state.streams.forEach {
            if (it.isExpanded) {
                if (!state.topics[it.id].isNullOrEmpty()) {
                    streamsListAdapter.expandTopics(state.topics[it.id]!!.toList())
                }
            }
        }
    }

    private fun isSubscribed() = requireArguments().getString(ARG_TYPE).equals(TABS[0])
}