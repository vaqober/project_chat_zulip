package com.fintech.homework.presentation.topic

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fintech.homework.R
import com.fintech.homework.appComponent
import com.fintech.homework.databinding.FragmentTopicBinding
import com.fintech.homework.objects.CurrentUser
import com.fintech.homework.objects.Emoji
import com.fintech.homework.presentation.topic.domain.query.MessageQuery
import com.fintech.homework.presentation.topic.domain.query.SearchQuery
import com.fintech.homework.presentation.topic.elm.Effect
import com.fintech.homework.presentation.topic.elm.Event
import com.fintech.homework.presentation.topic.elm.State
import com.fintech.homework.presentation.topic.elm.StoreFactoryMessages
import com.fintech.homework.presentation.topic.message.Message
import com.fintech.homework.presentation.topic.message.MessageListAdapter
import com.fintech.homework.presentation.topic.message.MessageViewModel
import com.fintech.homework.presentation.topic.reaction.BottomSheetDialogListener
import com.fintech.homework.presentation.topic.reaction.EmojiBottomSheet
import com.fintech.homework.presentation.topic.reaction.OnEmojiFlexClickListener
import com.fintech.homework.presentation.topic.reaction.Reaction
import vivid.money.elmslie.android.base.ElmFragment
import vivid.money.elmslie.core.store.Store
import java.util.*
import javax.inject.Inject


class TopicFragment : ElmFragment<Event, Effect, State>(), OnEmojiFlexClickListener,
    BottomSheetDialogListener {

    private var _binding: FragmentTopicBinding? = null
    private val binding get() = _binding!!

    private val messageList: MutableList<Message> = mutableListOf()
    private val messageAdapter = MessageListAdapter(mutableListOf(), this, this)
    private var layoutManager: LinearLayoutManager? = null
    private val viewModelMessage: MessageViewModel by viewModels()

    @Inject
    lateinit var storeFactory: StoreFactoryMessages

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTopicBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onAttach(context: Context) {
        context.appComponent.injectTopicFragment(this)
        super.onAttach(context)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModelMessage.store = store
        viewModelMessage.subscribeToMessagesDB(
            requireArguments().getLong(ARG_PARENT), requireArguments().getString(
                ARG_SUBJECT
            ).orEmpty()
        )
        viewModelMessage.searchMessages(
            SearchQuery(
                num_before = StoreFactoryMessages.PAGING_COUNT,
                num_after = 0,
                stream = requireArguments().getLong(ARG_PARENT),
                subject = requireArguments().getString(ARG_SUBJECT).toString()
            )
        )
        initUi()
    }

    private fun initUi() {
        layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        initPaging()
        binding.messages.adapter = messageAdapter
        binding.messages.layoutManager = layoutManager

        binding.topicTitle.text = "#Topic: ${arguments?.getString(ARG_SUBJECT)}"
        binding.input.doAfterTextChanged {
            if (it?.length ?: 0 > 0) {
                binding.sendImage.setBackgroundResource(R.drawable.ic_vector_send)
                binding.sendImage.setOnClickListener {
                    messageList.add(
                        Message(
                            messageAdapter.dataSet[messageAdapter.dataSet.size - 1].id + 1,
                            requireArguments().getLong(ARG_PARENT),
                            requireArguments().getString(ARG_SUBJECT).toString(),
                            CurrentUser.id,
                            CurrentUser.name,
                            binding.input.text.toString(),
                            Date(),
                            mutableListOf()
                        )
                    ) //hardcoded id
                    viewModelMessage.postMessage(
                        MessageQuery(
                            "stream",
                            listOf(requireArguments().getLong(ARG_PARENT)),
                            requireArguments().getString(ARG_SUBJECT).toString(),
                            binding.input.text.toString()
                        )
                    )
                    binding.input.text.clear()
                    messageAdapter.update(messageList.toList())
                    binding.messages.scrollToPosition(messageList.size - 1)
                }
            } else {
                binding.sendImage.setBackgroundResource(R.drawable.ic_vector_plus)
                binding.sendImage.setOnClickListener {
                    Toast.makeText(requireContext(), "Not implement yet, sorry", Toast.LENGTH_LONG)
                        .show()
                }
            }
        }
    }

    private fun initPaging() {
        binding.messages.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                val firstVisibleElement: Int =
                    layoutManager!!.findFirstCompletelyVisibleItemPosition()
                if (firstVisibleElement == 0) {
                    viewModelMessage.searchMessages(
                        SearchQuery(
                            num_before = ((store.currentState.messages.size
                                    / StoreFactoryMessages.PAGING_COUNT) + 1) * StoreFactoryMessages.PAGING_COUNT,
                            num_after = 0,
                            stream = requireArguments().getLong(ARG_PARENT),
                            subject = requireArguments().getString(ARG_SUBJECT).toString()
                        )
                    )
                }
            }
        })
    }

    override fun onEmojiFlexClick(messagePosition: Int, emoji: Emoji) {
        val emojies = messageAdapter.dataSet[messagePosition].reactions
        val reaction =
            Reaction(emoji.nameInZulip, Emoji.valueOf(emoji.name).unicode, CurrentUser.id)
        if (emojies.contains(reaction)) {
            viewModelMessage.deleteReaction(
                reaction.emoji_name,
                messageAdapter.dataSet[messagePosition].id
            )
            emojies.remove(reaction)
        } else {
            viewModelMessage.postReaction(
                reaction.emoji_name,
                messageAdapter.dataSet[messagePosition].id
            )
            emojies.add(reaction)
        }
        messageAdapter.notifyItemChanged(messagePosition)
    }

    override fun showBottomSheetDialog(context: Context, messageId: Int) {
        setFragmentResultListener("reactionEmojiKey") { _, bundle ->
            val resultEmoji = bundle.getString("bundleReactionEmojiKey")?.toUpperCase(Locale.ROOT)
            if (resultEmoji != null) {
                onEmojiFlexClick(messageId, Emoji.valueOf(resultEmoji))
            }
        }
        val emojiBottomSheet = EmojiBottomSheet.newInstance(messageId)
        emojiBottomSheet.show(
            parentFragmentManager,
            emojiBottomSheet.tag
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {

        private const val ARG_PARENT = "parent"
        private const val ARG_SUBJECT = "subject"

        @JvmStatic
        fun newInstance(parent: Long, subject: String) =
            TopicFragment().apply {
                arguments = Bundle().apply {
                    putLong(ARG_PARENT, parent)
                    putString(ARG_SUBJECT, subject)
                }
            }
    }

    override val initEvent: Event
        get() = Event.Ui.Init

    override fun createStore(): Store<Event, Effect, State> = storeFactory.provide()

    override fun render(state: State) {
        binding.loadingProgress.isVisible = state.isLoading
        messageAdapter.update(state.messages)
        if (!state.isEmptyState) {
            if (state.currentPage <= 1) {
                binding.messages.scrollToPosition(
                    state.messages.size - 1
                )
            } else {
                binding.messages.scrollToPosition(
                    StoreFactoryMessages.PAGING_COUNT
                )
            }
        }
    }

    override fun handleEffect(effect: Effect): Unit? {
        when (effect) {
            is Effect.LoadError -> {
                Toast.makeText(
                    requireContext(),
                    "Load message error: " + effect.error.message,
                    Toast.LENGTH_LONG
                ).show()
            }
            is Effect.DbError -> {
                Toast.makeText(
                    requireContext(),
                    "Db error: " + effect.error.message,
                    Toast.LENGTH_LONG
                ).show()
            }
            is Effect.InsertDBError -> {
                Toast.makeText(
                    requireContext(),
                    "Insert db error: " + effect.error.message,
                    Toast.LENGTH_LONG
                ).show()
            }
            is Effect.PostError -> {
                Toast.makeText(
                    requireContext(),
                    "Request post error: " + effect.error.message,
                    Toast.LENGTH_LONG
                ).show()
            }
        }
        return super.handleEffect(effect)
    }
}