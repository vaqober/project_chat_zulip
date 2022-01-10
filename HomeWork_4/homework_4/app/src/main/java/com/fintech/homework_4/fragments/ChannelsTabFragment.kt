package com.fintech.homework_4.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.fintech.homework_4.adapters.ChannelsFragmentsAdapter
import com.fintech.homework_4.databinding.FragmentChannelsTabBinding
import com.google.android.material.tabs.TabLayoutMediator

class ChannelsTabFragment : Fragment() {

    private var _binding: FragmentChannelsTabBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChannelsTabBinding.inflate(inflater, container, false)
        val view = binding.root
        val tabs: List<String> = listOf("Subscribed", "All streams")
        val channelsAdapter = ChannelsFragmentsAdapter(this)
        binding.viewPager.adapter = channelsAdapter
        channelsAdapter.update(
            listOf(
                ChannelsFragment.newInstance(tabs[0]),
                ChannelsFragment.newInstance(tabs[1])
            )
        )
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            if (position == 0) tab.text = "Subscribed"
            else tab.text = tabs[position]
        }.attach()
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}