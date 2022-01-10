package com.fintech.finalwork.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.fintech.finalwork.R
import com.fintech.finalwork.databinding.FragmentMainBinding
import com.fintech.finalwork.objects.CurrentUser
import com.fintech.finalwork.presentation.people.PeopleFragment
import com.fintech.finalwork.presentation.profile.ProfileFragment
import com.fintech.finalwork.presentation.streams.StreamsTabFragment


class MainFragment : Fragment() {
    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!
    private val channelsTabFragment = StreamsTabFragment()
    private val peopleFragment = PeopleFragment()
    private val profileFragment = ProfileFragment.newInstance(CurrentUser.id)

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
        binding.menuChannels.setColorFilter(
            ContextCompat.getColor(requireContext(), R.color.white)
        )
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
            changeTintSelect(it as ImageView, binding.menuPeople, binding.menuProfile)
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
            it.isSelected = !it.isSelected
            changeTintSelect(it as ImageView, binding.menuChannels, binding.menuProfile)
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
            changeTintSelect(it as ImageView, binding.menuChannels, binding.menuPeople)
        }
    }

    private fun changeTintSelect(
        viewClicked: ImageView,
        viewElse: ImageView,
        viewElse2: ImageView
    ) {
        viewClicked.isSelected = true
        viewElse.isSelected = false
        viewElse2.isSelected = false
        changeColorBySelected(viewClicked)
        changeColorBySelected(viewElse)
        changeColorBySelected(viewElse2)
    }

    private fun changeColorBySelected(view: ImageView) {
        when (view.isSelected) {
            true -> {
                view.setColorFilter(ContextCompat.getColor(requireContext(), R.color.white))
            }
            false -> {
                view.setColorFilter(ContextCompat.getColor(requireContext(), R.color.white_dark))
            }
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