package com.fintech.homework_3

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fintech.homework_3.adapters.EmojiSheetListAdapter
import com.fintech.homework_3.data.Emojies
import com.fintech.homework_3.data.Reaction
import com.fintech.homework_3.data.CurrentUser
import com.fintech.homework_3.interfaces.OnEmojiClickListener
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class EmojiBottomSheet : BottomSheetDialogFragment(), OnEmojiClickListener {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val sheetView = inflater.inflate(R.layout.bottom_sheet_dialog_layout, container, false)
        val grid = sheetView.findViewById<RecyclerView>(R.id.grid_emoji)
        grid.layoutManager = GridLayoutManager(parentFragment?.context, 7)
        val gridAdapter = EmojiSheetListAdapter(Emojies.getEmojiList(), this)
        grid.adapter = gridAdapter
        return sheetView
    }

    override fun onEmojiClick(position: Int) {
        val messageId = arguments?.getInt("message")
        val smile = Emojies.getEmoji(position)
        if (messageId != null) {
            (context as MainActivity).onEmojiFlexClick(
                messageId, Reaction(CurrentUser.id, smile)
            )
            dialog?.dismiss()
        }
    }
}