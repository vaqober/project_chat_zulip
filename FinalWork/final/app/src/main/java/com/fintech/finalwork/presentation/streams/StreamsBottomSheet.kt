package com.fintech.finalwork.presentation.streams

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import com.fintech.finalwork.databinding.FragmentBottomDialogCreateStreamBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class StreamsBottomSheet : BottomSheetDialogFragment() {

    private var _binding: FragmentBottomDialogCreateStreamBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        _binding = FragmentBottomDialogCreateStreamBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.accept.setOnClickListener {
            val streamTitle = binding.streamTitle.text.toString()
            val streamDescription = binding.streamDescription.text.toString()
            if (!streamTitle.isNullOrEmpty() && !streamDescription.isNullOrEmpty()) {
                setFragmentResult(
                    "stream", bundleOf(
                        "streamTitle" to streamTitle,
                        "streamDescription" to streamDescription
                    )
                )
                dialog?.dismiss()
            } else {
                Toast.makeText(
                    requireContext(),
                    "title and description is required",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        binding.dismiss.setOnClickListener {
            dialog?.dismiss()
        }
    }
}