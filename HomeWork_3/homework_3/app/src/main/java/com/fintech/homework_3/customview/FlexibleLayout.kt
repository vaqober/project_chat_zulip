package com.fintech.homework_3.customview

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup
import android.widget.LinearLayout

class FlexibleLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : ViewGroup(context, attrs, defStyleAttr, defStyleRes) {

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        var totalWidth = 0
        var totalHeight = 0
        var childWidth: Int
        for (i in 0 until childCount) {
            val child = getChildAt(i)
            measureChildWithMargins(child, widthMeasureSpec, 0, heightMeasureSpec, totalHeight)

            childWidth = child.measuredWidth
            val parent = parent as LinearLayout
            val parentWidth = maxOf(
                parent.getChildAt(0).measuredWidth,
                parent.measuredWidth
            ) - parent.paddingLeft - parent.paddingRight
            if (parentWidth > (totalWidth + childWidth + childWidth / 2)) {
                totalHeight = maxOf(totalHeight, child.measuredHeight)
                totalWidth += childWidth + childWidth / 2
            } else {
                totalHeight += child.measuredHeight + child.measuredHeight / 2
                totalWidth = 0
            }
        }
        val resultWidth = resolveSize(totalWidth, widthMeasureSpec)
        val resultHeight = resolveSize(totalHeight, heightMeasureSpec)
        setMeasuredDimension(resultWidth, resultHeight)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        var currentBottom = 0
        var currentLeft = 0
        for (i in 0 until childCount) {
            val child = getChildAt(i)
            if (measuredWidth < currentLeft + child.measuredWidth) {
                currentLeft = 0
                currentBottom += child.measuredHeight + child.measuredHeight / 2
            }
            child.layout(
                currentLeft,
                currentBottom,
                currentLeft + child.measuredWidth,
                currentBottom + child.measuredHeight
            )
            currentLeft = child.right + child.measuredWidth / 2
        }
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