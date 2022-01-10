package com.fintech.homework_4.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fintech.homework_4.R
import com.fintech.homework_4.adapters.EmojiSheetListAdapter
import com.fintech.homework_4.data.Emojies
import com.fintech.homework_4.interfaces.OnEmojiClickListener
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class EmojiBottomSheet : BottomSheetDialogFragment(), OnEmojiClickListener {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val sheetView = inflater.inflate(R.layout.bottom_sheet_dialog_layout, container, false)
        requireArguments().getInt(arg_message, 0)
        val grid = sheetView.findViewById<RecyclerView>(R.id.grid_emoji)
        grid.layoutManager = GridLayoutManager(parentFragment?.context, 7)
        val gridAdapter = EmojiSheetListAdapter(Emojies.getEmojiList(), this)
        grid.adapter = gridAdapter
        return sheetView
    }

    companion object {

        private val arg_message = "message"
        private var message: Int? = null

        @JvmStatic
        fun newInstance(message: Int) =
            EmojiBottomSheet().apply {
                arguments = Bundle().apply {
                    putInt(arg_message, message)
                }
            }
    }

    override fun onEmojiClick(position: Int) {
        setFragmentResult("reactionEmojiKey", bundleOf("bundleReactionEmojiKey" to position))
        dialog?.dismiss()
    }
}