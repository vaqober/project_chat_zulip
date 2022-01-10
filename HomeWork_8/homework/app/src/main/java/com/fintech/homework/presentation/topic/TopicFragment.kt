package com.fintech.homework.presentation.topic

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fintech.homework.R
import com.fintech.homework.databinding.FragmentTopicBinding
import com.fintech.homework.network.data.models.query.MessageQuery
import com.fintech.homework.network.data.models.query.SearchQuery
import com.fintech.homework.objects.CurrentUser
import com.fintech.homework.objects.Emoji
import com.fintech.homework.presentation.RequestsBlankScreenState
import com.fintech.homework.presentation.RequestsIdScreenState
import com.fintech.homework.presentation.topic.message.Message
import com.fintech.homework.presentation.topic.message.MessageListAdapter
import com.fintech.homework.presentation.topic.message.MessageScreenState
import com.fintech.homework.presentation.topic.message.MessageViewModel
import com.fintech.homework.presentation.topic.reaction.BottomSheetDialogListener
import com.fintech.homework.presentation.topic.reaction.EmojiBottomSheet
import com.fintech.homework.presentation.topic.reaction.OnEmojiFlexClickListener
import com.fintech.homework.presentation.topic.reaction.Reaction
import java.util.*


class TopicFragment : Fragment(), OnEmojiFlexClickListener, BottomSheetDialogListener {

    private var _binding: FragmentTopicBinding? = null
    private val binding get() = _binding!!

    private val messageList: MutableList<Message> = mutableListOf()
    private val messageAdapter = MessageListAdapter(messageList, this, this)
    private var layoutManager: LinearLayoutManager? = null
    private val viewModelMessage: MessageViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTopicBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUi()
    }

    private fun initUi() {
        observeModels()
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
                            listOf(requireArguments().getLong(ARG_PARENT).toInt()),
                            arguments?.getString(ARG_SUBJECT).toString(),
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
                    TODO()
                }
            }
        }
    }

    private fun initPaging() {
        binding.messages.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                val firstVisibleElement: Int = layoutManager!!.findFirstVisibleItemPosition()
                if (firstVisibleElement == 0) {
                    viewModelMessage.searchMessages(
                        SearchQuery(
                            num_before = messageAdapter.itemCount + 25,
                            num_after = 0,
                            stream = requireArguments().getLong(ARG_PARENT),
                            subject = requireArguments().getString(ARG_SUBJECT).toString()
                        )
                    )
                }
            }
        })
    }

    private fun observeModels() {
        viewModelMessage.subscribeToMessagesDB(
            requireArguments().getLong(ARG_PARENT),
            requireArguments().getString(ARG_SUBJECT).toString()
        )
        viewModelMessage.messageScreenState.observe(viewLifecycleOwner) {
            processMessageScreenState(it)
        }
        viewModelMessage.messageDBScreenState.observe(viewLifecycleOwner) {
            processMessageDBScreenState(it)
        }
        viewModelMessage.requestScreenState.observe(viewLifecycleOwner) {
            processRequestScreenState(it)
        }
        viewModelMessage.requestIdScreenState.observe(viewLifecycleOwner) {
            processRequestIdScreenState(it)
        }
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

    private fun processMessageScreenState(it: MessageScreenState?) {
        when (it) {
            is MessageScreenState.Result -> {
                val newItems = it.items
                messageAdapter.update(newItems)
                messageAdapter.dataSet.clear()
                messageAdapter.dataSet.addAll(newItems)
                viewModelMessage.insertMessagesDB(newItems)
                binding.loadingProgress.isVisible = false
            }
            MessageScreenState.Loading -> {
                binding.loadingProgress.isVisible = true
            }
            is MessageScreenState.Error -> {
                Toast.makeText(requireContext(), it.error.message, Toast.LENGTH_LONG).show()
                binding.loadingProgress.isVisible = false
            }
        }
    }

    private fun processMessageDBScreenState(it: MessageScreenState?) {
        when (it) {
            is MessageScreenState.Result -> {
                val newItems = it.items
                if (newItems.isNotEmpty()) {
                    messageAdapter.update(newItems)
                    messageAdapter.dataSet.clear()
                    messageAdapter.dataSet.addAll(newItems)
                    binding.messages.scrollToPosition(messageList.size - 1)
                    binding.loadingProgress.isVisible = false
                } else {
                    viewModelMessage.searchMessages(
                        SearchQuery(
                            stream = requireArguments().getLong(ARG_PARENT),
                            subject = requireArguments().getString(ARG_SUBJECT).toString()
                        )
                    )
                }
            }
            MessageScreenState.Loading -> {
                binding.loadingProgress.isVisible = false
            }
            is MessageScreenState.Error -> {
                Toast.makeText(requireContext(), it.error.message, Toast.LENGTH_LONG).show()
                binding.loadingProgress.isVisible = false
            }
        }
    }

    private fun processRequestScreenState(it: RequestsBlankScreenState?) {
        when (it) {
            is RequestsBlankScreenState.Result -> {
                if (it.response.result == "success") {
                    Toast.makeText(
                        requireContext(),
                        "Request successfully upload",
                        Toast.LENGTH_LONG
                    ).show()
                } else {
                    Toast.makeText(
                        requireContext(),
                        "Something wong. Request response: ${it.response}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            RequestsBlankScreenState.Loading -> {
            }
            is RequestsBlankScreenState.Error -> {
                Toast.makeText(requireContext(), it.error.message, Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun processRequestIdScreenState(it: RequestsIdScreenState?) {
        when (it) {
            is RequestsIdScreenState.Result -> {
                if (it.response.result == "success") {
                    Toast.makeText(
                        requireContext(),
                        "Request successfully upload",
                        Toast.LENGTH_LONG
                    ).show()
                    messageAdapter.dataSet[messageAdapter.dataSet.size - 1].id = it.response.id
                } else {
                    Toast.makeText(
                        requireContext(),
                        "Something wong. Request response: ${it.response}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            RequestsIdScreenState.Loading -> {
            }
            is RequestsIdScreenState.Error -> {
                Toast.makeText(requireContext(), it.error.message, Toast.LENGTH_LONG).show()
            }
        }
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

}