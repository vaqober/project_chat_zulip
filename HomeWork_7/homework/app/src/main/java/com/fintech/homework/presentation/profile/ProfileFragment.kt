package com.fintech.homework.presentation.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.fintech.homework.databinding.FragmentProfileBinding
import com.fintech.homework.objects.CurrentUser

class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        if (arguments?.getLong(ARG_USER_ID) ?: CurrentUser.id != CurrentUser.id) {
            binding.logout.isClickable = false
            binding.logout.isVisible = false
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    companion object {

        private const val ARG_USER_ID = "user"

        @JvmStatic
        fun newInstance(parent: Long) =
            ProfileFragment().apply {
                arguments = Bundle().apply {
                    putLong(ARG_USER_ID, parent)
                }
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}