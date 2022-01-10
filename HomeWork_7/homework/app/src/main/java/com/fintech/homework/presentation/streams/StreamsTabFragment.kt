package com.fintech.homework.presentation.streams

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import com.fintech.homework.databinding.FragmentStreamsTabBinding
import com.google.android.material.tabs.TabLayoutMediator

class StreamsTabFragment : Fragment() {

    private var _binding: FragmentStreamsTabBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStreamsTabBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val tabs: List<String> = listOf("Subscribed", "All streams")
        val channelsAdapter = StreamsFragmentsAdapter(
            this, mutableListOf(
                StreamsFragment.newInstance(tabs[0]),
                StreamsFragment.newInstance(tabs[1])
            )
        )
        binding.viewPager.adapter = channelsAdapter
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            if (position == 0) tab.text = "Subscribed"
            else tab.text = tabs[position]
        }.attach()
        binding.searchInput.doAfterTextChanged {
            childFragmentManager.setFragmentResult(
                "search",
                bundleOf("searchString" to it.toString())
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}