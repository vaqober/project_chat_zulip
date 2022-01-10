package com.fintech.homework.fragments

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
import com.fintech.homework.MessageViewModel
import com.fintech.homework.R
import com.fintech.homework.adapters.MessageListAdapter
import com.fintech.homework.data.*
import com.fintech.homework.databinding.FragmentTopicBinding
import com.fintech.homework.interfaces.BottomSheetDialogListener
import com.fintech.homework.interfaces.OnEmojiFlexClickListener
import java.util.*

class TopicFragment : Fragment(), OnEmojiFlexClickListener, BottomSheetDialogListener {

    private var _binding: FragmentTopicBinding? = null
    private val binding get() = _binding!!

    private val messageList: MutableList<Message> = mutableListOf()
    private val messageAdapter = MessageListAdapter(messageList, this, this)
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
        binding.messages
        binding.messages.adapter = messageAdapter
        viewModelMessage.messageScreenState.observe(viewLifecycleOwner) { processMessageScreenState(it) }
        Toast.makeText(this.context, "messages loaded", Toast.LENGTH_SHORT).show()
        viewModelMessage.searchMessages(MessageViewModel.INITIAL_QUERY)
        binding.input.doAfterTextChanged {
            if (it?.length ?: 0 > 0) {
                binding.sendImage.setBackgroundResource(R.drawable.ic_vector_send)
                binding.sendImage.setOnClickListener {
                    messageList.add(
                        Message(
                            CurrentUser.id,
                            CurrentUser.name,
                            binding.input.text.toString(),
                            mutableListOf(),
                            Date()
                        )
                    ) //hardcoded id
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

    override fun onEmojiFlexClick(messagePosition: Int, reaction: Reaction) {
        val emojies = messageAdapter.dataSet[messagePosition].reactions
        if (emojies.contains(reaction)) {
            emojies.remove(reaction)
        } else {
            emojies.add(reaction)
        }
        messageAdapter.notifyItemChanged(messagePosition)
    }

    override fun showBottomSheetDialog(context: Context, messageId: Int) {
        setFragmentResultListener("reactionEmojiKey") { _, bundle ->
            val resultSmile = bundle.getInt("bundleReactionEmojiKey")
            val smile = Emojies.getEmoji(resultSmile)
            onEmojiFlexClick(messageId, Reaction(CurrentUser.id, smile))
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
                binding.loadingProgress.isVisible = false
            }
            MessageScreenState.Loading -> {
                binding.loadingProgress.isVisible = true
            }
            is MessageScreenState.Error -> {
                Toast.makeText(this.context, it.error.message, Toast.LENGTH_SHORT).show()
                binding.loadingProgress.isVisible = false
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        @JvmStatic
        fun newInstance() = TopicFragment()
    }
}