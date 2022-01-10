package com.fintech.homework_2.customview

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup
import com.fintech.homework_2.R

class CustomMessageLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : ViewGroup(context, attrs, defStyleAttr, defStyleRes) {

    init {
        inflate(context, R.layout.custom_message_view_layout, this)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val imageView = getChildAt(0)
        val linearView = getChildAt(1)

        var totalWidth = 0
        var totalHeight = 0

        measureChildWithMargins(imageView, widthMeasureSpec, 0, heightMeasureSpec, 0)

        val marginLeft = (imageView.layoutParams as MarginLayoutParams).leftMargin
        val marginRight = (imageView.layoutParams as MarginLayoutParams).rightMargin
        totalWidth += imageView.measuredWidth + marginLeft + marginRight
        totalHeight = maxOf(totalHeight, imageView.measuredHeight)

        measureChildWithMargins(
            linearView,
            widthMeasureSpec,
            imageView.measuredWidth,
            heightMeasureSpec,
            0
        )
        val textMarginLeft = (linearView.layoutParams as MarginLayoutParams).leftMargin
        val textMarginRight = (linearView.layoutParams as MarginLayoutParams).rightMargin
        totalWidth += linearView.measuredWidth + textMarginLeft + textMarginRight
        totalHeight = maxOf(totalHeight, linearView.measuredHeight)

        val resultWidth = resolveSize(totalWidth + paddingRight + paddingLeft, widthMeasureSpec)
        val resultHeight = resolveSize(totalHeight + paddingTop + paddingBottom, heightMeasureSpec)
        setMeasuredDimension(resultWidth, resultHeight)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        val imageView = getChildAt(0)
        val linearView = getChildAt(1)

        imageView.layout(
            paddingLeft,
            paddingTop,
            paddingLeft + imageView.measuredWidth,
            paddingTop + imageView.measuredHeight
        )

        val marginRight = (imageView.layoutParams as MarginLayoutParams).rightMargin

        linearView.layout(
            imageView.right + marginRight,
            paddingTop,
            imageView.right + marginRight + linearView.measuredWidth,
            paddingTop + linearView.measuredHeight
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
}