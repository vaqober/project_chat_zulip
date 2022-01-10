package com.fintech.finalwork.presentation.streams

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import com.fintech.finalwork.R
import com.fintech.finalwork.appComponent
import com.fintech.finalwork.databinding.FragmentStreamsBinding
import com.fintech.finalwork.presentation.MainFragment
import com.fintech.finalwork.presentation.streams.StreamsTabFragment.Companion.TABS
import com.fintech.finalwork.presentation.streams.elm.*
import com.fintech.finalwork.presentation.streams.elm.StoreFactoryStreams.Companion.INITIAL_QUERY
import com.fintech.finalwork.presentation.streams.listeners.OnStreamClickListener
import com.fintech.finalwork.presentation.streams.listeners.OnTopicClickedListener
import com.fintech.finalwork.presentation.streams.objects.StreamTopicItem
import com.fintech.finalwork.presentation.streams.objects.Types
import com.fintech.finalwork.presentation.streams.objects.Types.VIEW_TYPE_CHANNEL
import com.fintech.finalwork.presentation.topic.TopicFragment
import vivid.money.elmslie.android.base.ElmFragment
import vivid.money.elmslie.core.store.Store
import javax.inject.Inject

class StreamsFragment : ElmFragment<Event, Effect, State>(), OnStreamClickListener,
    OnTopicClickedListener {

    private var _binding: FragmentStreamsBinding? = null
    private val binding get() = _binding!!
    private var streamsList = mutableListOf<StreamTopicItem>()
    private val streamsListAdapter = StreamListAdapter(streamsList, this, this)
    private val viewModelStream: StreamsViewModel by viewModels()

    @Inject
    lateinit var storeFactory: StoreFactoryStreams

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModelStream.store = store
        super.onCreateView(inflater, container, savedInstanceState)
        _binding = FragmentStreamsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onAttach(context: Context) {
        context.appComponent.injectStreamFragment(this)
        super.onAttach(context)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUi()
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
        if (isSubscribed()) {
            binding.addButton.setOnClickListener {
                showBottomSheetDialog()
            }
        } else {
            binding.addButton.isVisible = false
            binding.addButton.isClickable = false
        }
    }

    private fun showBottomSheetDialog() {
        setFragmentResultListener("stream") { _, bundle ->
            val resultStreamTitle = bundle.getString("streamTitle")
            val resultStreamDescription = bundle.getString("streamDescription")
            if (!resultStreamTitle.isNullOrEmpty() && !resultStreamDescription.isNullOrEmpty()) {
                viewModelStream.subscribeStream(
                    Subscribe(
                        description = resultStreamDescription,
                        name = resultStreamTitle
                    )
                )
            }
        }
        val streamBottomSheet = StreamsBottomSheet()
        streamBottomSheet.show(
            parentFragmentManager,
            streamBottomSheet.tag
        )
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

    override fun onStreamLongClick(streamId: Long, view: View) {
        val streamName = store.currentState.streams.find { it.id == streamId }?.name.toString()
        val topicFragment = TopicFragment.newInstance(streamId, streamName, null)
        requireActivity().supportFragmentManager.beginTransaction()
            .add(R.id.mainFragmentContainer, topicFragment, "topic")
            .show(topicFragment)
            .hide(StreamsTabFragment())
            .hide(MainFragment())
            .addToBackStack("main")
            .commitAllowingStateLoss()
    }

    override fun onTopicClicked(stream: Long, subject: String) {
        val streamName = store.currentState.streams.find { it.id == stream }?.name.toString()
        val topicFragment = TopicFragment.newInstance(stream, streamName, subject)
        requireActivity().supportFragmentManager.beginTransaction()
            .add(R.id.mainFragmentContainer, topicFragment, "topic")
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

    override val initEvent: Event
        get() = Event.Ui.LoadStreams(SearchQuery(INITIAL_QUERY, isSubscribed()))

    override fun createStore(): Store<Event, Effect, State> = (
            storeFactory.provide()
            )


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

    override fun handleEffect(effect: Effect): Unit? {
        when (effect) {
            is Effect.LoadError -> {
                Toast.makeText(
                    requireContext(),
                    "Loading error: ${effect.error.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
            is Effect.InsertDBError -> {
                Toast.makeText(
                    requireContext(),
                    "Loading error: ${effect.error.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
            is Effect.SubscribeError -> {
                Toast.makeText(
                    requireContext(),
                    "Subscribe error: ${effect.error.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        return super.handleEffect(effect)
    }

    private fun isSubscribed() = requireArguments().getString(ARG_TYPE).equals(TABS[0])
}