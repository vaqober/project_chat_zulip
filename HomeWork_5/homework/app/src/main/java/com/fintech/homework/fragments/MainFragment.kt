package com.fintech.homework.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.fintech.homework.R
import com.fintech.homework.databinding.FragmentMainBinding


class MainFragment : Fragment() {
    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!
    private val channelsTabFragment = ChannelsTabFragment()
    private val peopleFragment = PeopleFragment()
    private val profileFragment = ProfileFragment()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        childFragmentManager.beginTransaction()
            .add(R.id.fragment_controller, channelsTabFragment, "channels")
            .show(channelsTabFragment)
            .commitAllowingStateLoss()
        setMenuClickListeners()
    }

    private fun setMenuClickListeners() {
        binding.menuChannels.setOnClickListener {
            val transaction = childFragmentManager.beginTransaction()
            transaction
                .hide(peopleFragment)
                .hide(profileFragment)
            if (!childFragmentManager.fragments.contains(channelsTabFragment)) {
                transaction
                    .add(R.id.fragment_controller, channelsTabFragment, "channels")
            }
            transaction
                .show(channelsTabFragment)
                .commitAllowingStateLoss()
        }
        binding.menuPeople.setOnClickListener {
            val transaction = childFragmentManager.beginTransaction()
            transaction
                .hide(channelsTabFragment)
                .hide(profileFragment)
            if (!childFragmentManager.fragments.contains(peopleFragment)) {
                transaction
                    .add(R.id.fragment_controller, peopleFragment, "people")
            }
            transaction
                .show(peopleFragment)
                .commitAllowingStateLoss()
        }
        binding.menuProfile.setOnClickListener {
            val transaction = childFragmentManager.beginTransaction()
            transaction
                .hide(channelsTabFragment)
                .hide(peopleFragment)
            if (!childFragmentManager.fragments.contains(profileFragment)) {
                transaction
                    .add(R.id.fragment_controller, profileFragment, "profile")
            }
            transaction
                .show(profileFragment)
                .commitAllowingStateLoss()
        }
    }

    companion object {
        fun newInstance() = MainFragment()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}