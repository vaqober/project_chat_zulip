package com.fintech.homework_4.customview

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.core.view.marginBottom
import androidx.core.view.marginTop
import com.fintech.homework_4.data.Message
import com.fintech.homework_4.R
import com.fintech.homework_4.data.CurrentUser
import com.fintech.homework_4.data.Reaction
import com.fintech.homework_4.interfaces.BottomSheetDialogListener
import com.fintech.homework_4.interfaces.OnEmojiFlexClickListener
import java.text.DateFormat
import java.text.SimpleDateFormat

class CustomMessageViewGroup @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0,
    private val listenerEmoji: OnEmojiFlexClickListener,
    private val listenerBottomSheet: BottomSheetDialogListener,
    var isHeader: Boolean = false
) : ViewGroup(context, attrs, defStyleAttr, defStyleRes) {


    var position: Int = 0
    var message: Message = Message()

    init {
        inflate(context, R.layout.custom_message_view_layout, this)
        setNewMessage(message)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {

        val dateView = getChildAt(0)
        val imageView = getChildAt(1)
        val linearView = getChildAt(2)

        var totalWidth = 0
        var totalHeight = 0

        var dateHeight = 0
        if (isHeader) {
            measureChildWithMargins(
                dateView,
                widthMeasureSpec,
                0,
                heightMeasureSpec,
                0
            )
            dateHeight = dateView.marginBottom + dateView.marginTop + dateView.measuredHeight
        }

        measureChildWithMargins(
            imageView,
            widthMeasureSpec, 0,
            heightMeasureSpec, 0
        )

        val marginLeft = (imageView.layoutParams as MarginLayoutParams).leftMargin
        val marginRight = (imageView.layoutParams as MarginLayoutParams).rightMargin
        totalWidth += imageView.measuredWidth + marginLeft + marginRight
        totalHeight = maxOf(totalHeight, imageView.measuredHeight)

        measureChildWithMargins(
            linearView,
            widthMeasureSpec,
            0,
            heightMeasureSpec,
            0
        )

        val textMarginLeft = (linearView.layoutParams as MarginLayoutParams).leftMargin
        val textMarginRight = (linearView.layoutParams as MarginLayoutParams).rightMargin

        totalWidth += linearView.measuredWidth + textMarginLeft + textMarginRight
        totalHeight = maxOf(
            totalHeight,
            linearView.measuredHeight + dateHeight
        )

        val resultWidth = resolveSize(totalWidth + paddingRight + paddingLeft, widthMeasureSpec)
        val resultHeight = resolveSize(totalHeight + paddingTop + paddingBottom, heightMeasureSpec)
        setMeasuredDimension(resultWidth, resultHeight)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {

        val dateView = getChildAt(0)
        val imageView = getChildAt(1)
        val linearView = getChildAt(2)

        if (!isHeader) {
            this.findViewById<TextView>(R.id.date).isVisible = false
        }

        var dateHeight = 0
        if (isHeader) {
            dateView.layout(
                //paddingLeft,
                (l + r) / 2 - dateView.measuredWidth / 2,
                paddingTop,
                (l + r) / 2 + dateView.measuredWidth / 2,
                paddingTop + dateView.measuredHeight,
                //paddingTop + dateView.measuredHeight
            )
            dateHeight = dateView.marginBottom + dateView.marginTop + dateView.measuredHeight
        }

        imageView.layout(
            paddingLeft,
            paddingTop + dateHeight,
            paddingLeft + imageView.measuredWidth,
            paddingTop + imageView.measuredHeight + dateHeight
        )

        val marginRight = (imageView.layoutParams as MarginLayoutParams).rightMargin

        linearView.layout(
            imageView.right + marginRight,
            paddingTop + dateHeight,
            imageView.right + marginRight + linearView.measuredWidth,
            paddingTop + linearView.measuredHeight + dateHeight
        )
    }

    override fun generateLayoutParams(attrs: AttributeSet?): LayoutParams {
        return MarginLayoutParams(context, attrs)
    }

    override fun checkLayoutParams(p: LayoutParams): Boolean {
        return p is MarginLayoutParams
    }

    override fun generateLayoutParams(p: LayoutParams): LayoutParams {
        return MarginLayoutParams(p)
    }

    fun setNewMessage(newMessage: Message) {
        message = newMessage
        updateView()
    }

    @SuppressLint("SimpleDateFormat")
    private fun updateView() {
        findViewById<TextView>(R.id.userName).text = message.userName
        findViewById<TextView>(R.id.messageText).text = message.message
        if (isHeader) {
            val dateFormatter: DateFormat = SimpleDateFormat("dd MMM")
            findViewById<TextView>(R.id.date)?.text =
                dateFormatter.format(message.date)
        }
    }

    fun updateReactions(reactions: MutableMap<String, MutableSet<Int>>) {
        val flex = this.findViewById<FlexibleLayout>(R.id.flex)
        flex.removeAllViews()
        reactions.forEach {
            if (it.value.size != 0) {
                val emoji = it.key

                flex.addView(EmojiTextView(context, emoji = emoji, count = it.value.size))
                if (it.value.contains(CurrentUser.id)) { //hardcoded
                    flex.getChildAt(flex.childCount - 1).isSelected = true
                }
                flex.getChildAt(flex.childCount - 1).setOnClickListener {
                    listenerEmoji.onEmojiFlexClick(
                        position,
                        Reaction(CurrentUser.id, emoji)
                    ) //hardcoded id
                }
            }
        }
        if (flex.childCount >= 1) {
            flex.addView(
                EmojiTextView(
                    context,
                    emoji = context.resources.getString(R.string.plus_draft)
                )
            )
            flex.getChildAt(flex.childCount - 1).setOnClickListener {
                listenerBottomSheet.showBottomSheetDialog(context, position)
            }
        } else {
            this.setOnLongClickListener {
                if (flex.childCount == 0) {
                    listenerBottomSheet.showBottomSheetDialog(context, position)
                }
                true
            }
        }
    }
}