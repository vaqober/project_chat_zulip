package com.fintech.finalwork.presentation.topic.reaction

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fintech.finalwork.R
import com.fintech.finalwork.objects.Emoji
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class EmojiBottomSheet : BottomSheetDialogFragment(), OnEmojiClickListener {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val sheetView = inflater.inflate(R.layout.bottom_sheet_dialog_layout, container, false)
        requireArguments().getInt(ARG_MESSAGE, 0)
        val grid = sheetView.findViewById<RecyclerView>(R.id.grid_emoji)
        grid.layoutManager = GridLayoutManager(parentFragment?.context, 7)
        val gridAdapter = EmojiSheetListAdapter(Emoji.values().toList(), this)
        grid.adapter = gridAdapter
        return sheetView
    }

    companion object {

        private const val ARG_MESSAGE = "message"

        @JvmStatic
        fun newInstance(message: Int) =
            EmojiBottomSheet().apply {
                arguments = Bundle().apply {
                    putInt(ARG_MESSAGE, message)
                }
            }
    }

    override fun onEmojiClick(emoji: Emoji) {
        setFragmentResult("reactionEmojiKey", bundleOf("bundleReactionEmojiKey" to emoji.name))
        dialog?.dismiss()
    }
}