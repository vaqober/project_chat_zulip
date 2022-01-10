package com.fintech.homework_4.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ConcatAdapter
import com.fintech.homework_4.R
import com.fintech.homework_4.adapters.ChannelsListAdapter
import com.fintech.homework_4.data.Channels
import com.fintech.homework_4.databinding.FragmentChannelsBinding

class ChannelsFragment : Fragment() {

    private var _binding: FragmentChannelsBinding? = null
    private val binding get() = _binding!!
    private var channelsList = Channels.getStubChannelList()
    private val channelsListAdapter = arrayListOf<ChannelsListAdapter>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChannelsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        for (channel in channelsList) {
            val adapter = ChannelsListAdapter(channel, onTopicClicked())
            channelsListAdapter.add(adapter)
        }
        val concatAdapterConfig = ConcatAdapter.Config.Builder()
            .setIsolateViewTypes(false)
            .build()
        val concatAdapter = ConcatAdapter(concatAdapterConfig, channelsListAdapter)
        binding.channels.adapter = concatAdapter
    }

    private fun onTopicClicked() = View.OnClickListener {
        this.activity?.supportFragmentManager?.beginTransaction()
            ?.add(R.id.mainFragmentContainer, TopicFragment.newInstance(), "topic")
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
}