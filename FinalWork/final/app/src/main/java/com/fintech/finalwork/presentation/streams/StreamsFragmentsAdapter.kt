package com.fintech.finalwork.presentation.streams

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class StreamsFragmentsAdapter(
    fragment: Fragment,
    private val fragments: MutableList<Fragment>
) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = fragments.size

    override fun createFragment(position: Int): Fragment = fragments[position]

}
