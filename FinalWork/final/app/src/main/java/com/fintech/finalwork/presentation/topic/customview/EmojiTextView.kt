package com.fintech.finalwork.presentation.topic.customview

import android.content.Context
import android.content.res.TypedArray
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import com.fintech.finalwork.R
import com.fintech.finalwork.objects.Emoji

class EmojiTextView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0,
    var emoji: String = "",
    var count: Int = 0
) : View(context, attrs, defStyleAttr, defStyleRes) {

    private var text = ""
        set(value) {
            field = value
            requestLayout()
        }

    private val textPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.WHITE
        textSize = 45f
        textAlign = Paint.Align.CENTER
    }

    private val textBounds = Rect()
    private val textCoordinate = PointF()

    private val tempFontMetrics = Paint.FontMetrics()

    init {
        val typedArray: TypedArray = context.obtainStyledAttributes(
            attrs,
            R.styleable.EmojiTextView,
            defStyleAttr,
            defStyleRes
        )

        setBackgroundResource(R.drawable.bg_custom_emoji_view)

        text = typedArray.getString(R.styleable.EmojiTextView_customText).orEmpty()
        textPaint.color =
            typedArray.getColor(R.styleable.EmojiTextView_customTextColor, Color.WHITE)
        textPaint.textSize =
            typedArray.getDimension(R.styleable.EmojiTextView_customTextSize, 45f)

        setClickCount(count)
        typedArray.recycle()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        textPaint.getTextBounds(text, 0, text.length, textBounds)

        val textHeight = textBounds.height()
        val textWidth = textBounds.width()

        val totalWidth = textWidth + paddingRight + paddingLeft
        val totalHeight = textHeight + paddingTop + paddingBottom

        val resultWidth = resolveSize(totalWidth, widthMeasureSpec)
        val resultHeight = resolveSize(totalHeight, heightMeasureSpec)
        setMeasuredDimension(resultWidth, resultHeight)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        textPaint.getFontMetrics(tempFontMetrics)
        textCoordinate.x = w / 2f
        textCoordinate.y = h / 2f + textBounds.height() / 2 - tempFontMetrics.descent
    }

    override fun onCreateDrawableState(extraSpace: Int): IntArray {
        val drawableState = super.onCreateDrawableState(extraSpace + SUPPORTED_DRAWABLE_STATE.size)
        if (isSelected) {
            mergeDrawableStates(drawableState, SUPPORTED_DRAWABLE_STATE)
        }
        return drawableState
    }

    override fun onDraw(canvas: Canvas) {
        canvas.drawText(text, textCoordinate.x, textCoordinate.y, textPaint)
    }

    companion object {
        private val SUPPORTED_DRAWABLE_STATE = intArrayOf(android.R.attr.state_selected)
    }

    private fun setClickCount(value: Int) {
        val emojiCode = try {
            Emoji.valueOf(emoji).unicode
        } catch (e: IllegalArgumentException) {
            emoji
        }
        text = if (count == 0) emojiCode
        else "${emojiCode} $value"
    }
}