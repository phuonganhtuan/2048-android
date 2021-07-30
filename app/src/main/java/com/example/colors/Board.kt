package com.example.colors

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.util.Log
import android.view.View
import java.util.*


class Board(context: Context?, attributeSet: AttributeSet) : View(context, attributeSet) {

    var score = 0

    private var boardItemInset = 24f

    private var left = 0f
    private var top = 0f
    private var right = 0f
    private var bottom = 0f

    private val boardPaint by lazy {
        val paint = Paint(Paint.ANTI_ALIAS_FLAG)
        paint.color = Color.parseColor("#a0aeb8")
        paint.strokeWidth = 4f
        paint.style = Paint.Style.FILL
        paint
    }

    private val boardItemPaint by lazy {
        val paint = Paint(Paint.ANTI_ALIAS_FLAG)
        paint.color = Color.parseColor("#b3bec9")
        paint.strokeWidth = 4f
        paint.style = Paint.Style.FILL
        paint
    }

    private var rects = listOf<ItemRect>()

    fun setup() {
        left = 0f
        top = height / 2 - width / 2f
        right = width.toFloat()
        bottom = height / 2 + width / 2f
        boardItemInset = width / 40f
        val rect1 =
            ItemRect(
                left + boardItemInset,
                top + boardItemInset,
                width / 4f - 0.5f * boardItemInset,
                height / 2 - width / 4f - 0.5f * boardItemInset
            )

        val rect2 =
            ItemRect(
                width / 4f + 0.5f * boardItemInset,
                top + boardItemInset,
                width / 2f - 0.5f * boardItemInset,
                height / 2 - width / 4f - 0.5f * boardItemInset
            )
        val rect3 =
            ItemRect(
                width / 2f + 0.5f * boardItemInset,
                top + boardItemInset,
                width * 3f / 4f - 0.5f * boardItemInset,
                height / 2 - width / 4f - 0.5f * boardItemInset
            )
        val rect4 =
            ItemRect(
                width * 3f / 4f + 0.5f * boardItemInset,
                top + boardItemInset,
                width - boardItemInset,
                height / 2 - width / 4f - 0.5f * boardItemInset
            )
        val rect5 =
            ItemRect(
                left + boardItemInset,
                height / 2 - width / 4f + 0.5f * boardItemInset,
                width / 4f - 0.5f * boardItemInset,
                height / 2 - 0.5f * boardItemInset
            )
        val rect6 =
            ItemRect(
                width / 4f + 0.5f * boardItemInset,
                height / 2 - width / 4f + 0.5f * boardItemInset,
                width / 2f - 0.5f * boardItemInset,
                height / 2 - 0.5f * boardItemInset
            )
        val rect7 =
            ItemRect(
                width / 2f + 0.5f * boardItemInset,
                height / 2 - width / 4f + 0.5f * boardItemInset,
                width * 3f / 4f - 0.5f * boardItemInset,
                height / 2 - 0.5f * boardItemInset
            )
        val rect8 =
            ItemRect(
                width * 3f / 4f + 0.5f * boardItemInset,
                height / 2 - width / 4f + 0.5f * boardItemInset,
                width - boardItemInset,
                height / 2 - 0.5f * boardItemInset
            )
        val rect9 =
            ItemRect(
                left + boardItemInset,
                height / 2 + 0.5f * boardItemInset,
                width / 4f - 0.5f * boardItemInset,
                height / 2 + width / 4f - 0.5f * boardItemInset
            )
        val rect10 =
            ItemRect(
                width / 4f + 0.5f * boardItemInset,
                height / 2 + 0.5f * boardItemInset,
                width / 2f - 0.5f * boardItemInset,
                height / 2 + width / 4f - 0.5f * boardItemInset
            )
        val rect11 =
            ItemRect(
                width / 2f + 0.5f * boardItemInset,
                height / 2 + 0.5f * boardItemInset,
                width * 3f / 4f - 0.5f * boardItemInset,
                height / 2 + width / 4f - 0.5f * boardItemInset
            )
        val rect12 =
            ItemRect(
                width * 3f / 4f + 0.5f * boardItemInset,
                height / 2 + 0.5f * boardItemInset,
                width - boardItemInset,
                height / 2 + width / 4f - 0.5f * boardItemInset
            )
        val rect13 =
            ItemRect(
                left + boardItemInset,
                height / 2 + width / 4f + 0.5f * boardItemInset,
                width / 4f - 0.5f * boardItemInset,
                height / 2 + width / 2 - boardItemInset
            )
        val rect14 =
            ItemRect(
                width / 4f + 0.5f * boardItemInset,
                height / 2 + width / 4f + 0.5f * boardItemInset,
                width / 2f - 0.5f * boardItemInset,
                height / 2 + width / 2 - boardItemInset
            )
        val rect15 =
            ItemRect(
                width / 2f + 0.5f * boardItemInset,
                height / 2 + width / 4f + 0.5f * boardItemInset,
                width * 3f / 4f - 0.5f * boardItemInset,
                height / 2 + width / 2 - boardItemInset
            )
        val rect16 =
            ItemRect(
                width * 3f / 4f + 0.5f * boardItemInset,
                height / 2 + width / 4f + 0.5f * boardItemInset,
                width - boardItemInset,
                height / 2 + width / 2 - boardItemInset
            )
        rects = listOf(
            rect1,
            rect2,
            rect3,
            rect4,
            rect5,
            rect6,
            rect7,
            rect8,
            rect9,
            rect10,
            rect11,
            rect12,
            rect13,
            rect14,
            rect15,
            rect16
        )
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        val startTime = Calendar.getInstance().timeInMillis
        canvas?.drawRoundRect(left, top, right, bottom, boardItemInset, boardItemInset, boardPaint)
        for (element in rects) {
            canvas?.drawRoundRect(
                element,
                boardItemInset / 1.5f,
                boardItemInset / 1.5f,
                boardItemPaint
            )
        }
        Log.i("aaa", (Calendar.getInstance().timeInMillis - startTime).toString())
    }
}
