package com.example.cc

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import java.util.*
import kotlin.math.pow


enum class GameState {
    PLAYING, OVER, WIN
}

class Board(context: Context?, attributeSet: AttributeSet) : View(context, attributeSet) {

    var score = 0

    private var boardItemInset = 24f

    private var left = 0f
    private var top = 0f
    private var right = 0f
    private var bottom = 0f

    private var gameState = GameState.PLAYING
    private var canUndo = false

    var onGameOver: (() -> Unit)? = null
    var onGameWin: (() -> Unit)? = null

    private var newItemPos = 0
    private var mergedPos = mutableListOf<Int>()

    private val boardPaint by lazy {
        val paint = Paint(Paint.ANTI_ALIAS_FLAG)
        paint.color = Color.parseColor("#b8aca0")
        paint.strokeWidth = 4f
        paint.style = Paint.Style.FILL
        paint
    }

    private val boardPaintOver by lazy {
        val paint = Paint(Paint.ANTI_ALIAS_FLAG)
        paint.color = Color.parseColor("#90b8aca0")
        paint.strokeWidth = 4f
        paint.style = Paint.Style.FILL
        paint
    }

    private val boardPaintWin by lazy {
        val paint = Paint(Paint.ANTI_ALIAS_FLAG)
        paint.color = Color.parseColor("#90ebc765")
        paint.strokeWidth = 4f
        paint.style = Paint.Style.FILL
        paint
    }

    private val boardItemPaint by lazy {
        val paint = Paint(Paint.ANTI_ALIAS_FLAG)
        paint.color = Color.parseColor("#c9c1b3")
        paint.strokeWidth = 4f
        paint.style = Paint.Style.FILL
        paint
    }

    private val textPaint1 by lazy {
        val paint = Paint(Paint.ANTI_ALIAS_FLAG)
        paint.color = Color.parseColor("#666666")
        paint.strokeWidth = 4f
        paint.typeface = Typeface.DEFAULT_BOLD
        paint.textSize = 128f
        paint.style = Paint.Style.FILL
        paint
    }

    private val textPaint2 by lazy {
        val paint = Paint(Paint.ANTI_ALIAS_FLAG)
        paint.textSize = 128f
        paint.typeface = Typeface.DEFAULT_BOLD
        paint.color = Color.parseColor("#ffffff")
        paint.strokeWidth = 4f
        paint.style = Paint.Style.FILL
        paint
    }

    private val textPaint3 by lazy {
        val paint = Paint(Paint.ANTI_ALIAS_FLAG)
        paint.textSize = 128f
        paint.typeface = Typeface.DEFAULT_BOLD
        paint.color = Color.parseColor("#ffffff")
        paint.strokeWidth = 4f
        paint.style = Paint.Style.FILL
        paint
    }

    private val itemPaint = mutableListOf<Paint>()
    private var rects = listOf<ItemRect>()
    private var items = mutableListOf<Int>()
    private var backupList = listOf<Int>()

    fun setup() {
        itemPaint.clear()
        val colors = listOf("#f7f2e9", "#f0e5d1", "#f0bf84", "#eba76c", "#eb816c", "#ed5651", "#f7dd72", "#f2d355", "#f7d548", "#f7d548", "#c391ff")
        for (element in colors) {
            val paint = Paint(Paint.ANTI_ALIAS_FLAG)
            paint.color = Color.parseColor(element)
            paint.strokeWidth = 4f
            paint.style = Paint.Style.FILL
            itemPaint.add(paint)
        }
        left = 0f
        top = height / 2 - width / 2f
        right = width.toFloat()
        bottom = height / 2 + width / 2f
        boardItemInset = width / 40f
        val rect1 =
            ItemRect(left + boardItemInset,
                top + boardItemInset,
                width / 4f - 0.5f * boardItemInset,
                height / 2 - width / 4f - 0.5f * boardItemInset)

        val rect2 =
            ItemRect(width / 4f + 0.5f * boardItemInset,
                top + boardItemInset,
                width / 2f - 0.5f * boardItemInset,
                height / 2 - width / 4f - 0.5f * boardItemInset)
        val rect3 =
            ItemRect(width / 2f + 0.5f * boardItemInset,
                top + boardItemInset,
                width * 3f / 4f - 0.5f * boardItemInset,
                height / 2 - width / 4f - 0.5f * boardItemInset)
        val rect4 =
            ItemRect(width * 3f / 4f + 0.5f * boardItemInset,
                top + boardItemInset,
                width - boardItemInset,
                height / 2 - width / 4f - 0.5f * boardItemInset)
        val rect5 =
            ItemRect(left + boardItemInset,
                height / 2 - width / 4f + 0.5f * boardItemInset,
                width / 4f - 0.5f * boardItemInset,
                height / 2 - 0.5f * boardItemInset)
        val rect6 =
            ItemRect(width / 4f + 0.5f * boardItemInset,
                height / 2 - width / 4f + 0.5f * boardItemInset,
                width / 2f - 0.5f * boardItemInset,
                height / 2 - 0.5f * boardItemInset)
        val rect7 =
            ItemRect(width / 2f + 0.5f * boardItemInset,
                height / 2 - width / 4f + 0.5f * boardItemInset,
                width * 3f / 4f - 0.5f * boardItemInset,
                height / 2 - 0.5f * boardItemInset)
        val rect8 =
            ItemRect(width * 3f / 4f + 0.5f * boardItemInset,
                height / 2 - width / 4f + 0.5f * boardItemInset,
                width - boardItemInset,
                height / 2 - 0.5f * boardItemInset)
        val rect9 =
            ItemRect(left + boardItemInset,
                height / 2 + 0.5f * boardItemInset,
                width / 4f - 0.5f * boardItemInset,
                height / 2 + width / 4f - 0.5f * boardItemInset)
        val rect10 =
            ItemRect(width / 4f + 0.5f * boardItemInset,
                height / 2 + 0.5f * boardItemInset,
                width / 2f - 0.5f * boardItemInset,
                height / 2 + width / 4f - 0.5f * boardItemInset)
        val rect11 =
            ItemRect(width / 2f + 0.5f * boardItemInset,
                height / 2 + 0.5f * boardItemInset,
                width * 3f / 4f - 0.5f * boardItemInset,
                height / 2 + width / 4f - 0.5f * boardItemInset)
        val rect12 =
            ItemRect(width * 3f / 4f + 0.5f * boardItemInset,
                height / 2 + 0.5f * boardItemInset,
                width - boardItemInset,
                height / 2 + width / 4f - 0.5f * boardItemInset)
        val rect13 =
            ItemRect(left + boardItemInset,
                height / 2 + width / 4f + 0.5f * boardItemInset,
                width / 4f - 0.5f * boardItemInset,
                height / 2 + width / 2 - boardItemInset)
        val rect14 =
            ItemRect(width / 4f + 0.5f * boardItemInset,
                height / 2 + width / 4f + 0.5f * boardItemInset,
                width / 2f - 0.5f * boardItemInset,
                height / 2 + width / 2 - boardItemInset)
        val rect15 =
            ItemRect(width / 2f + 0.5f * boardItemInset,
                height / 2 + width / 4f + 0.5f * boardItemInset,
                width * 3f / 4f - 0.5f * boardItemInset,
                height / 2 + width / 2 - boardItemInset)
        val rect16 =
            ItemRect(width * 3f / 4f + 0.5f * boardItemInset,
                height / 2 + width / 4f + 0.5f * boardItemInset,
                width - boardItemInset,
                height / 2 + width / 2 - boardItemInset)
        rects = listOf(rect1, rect2, rect3, rect4, rect5, rect6, rect7, rect8, rect9, rect10, rect11, rect12, rect13, rect14, rect15, rect16)
        gameState = GameState.PLAYING
        score = 0
        canUndo = false
    }

    fun resetBoard() {
        items = mutableListOf(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0)
        backupList = items.toMutableList()
    }

    fun goRight() {
        canUndo = true
        backupList = items.toList()
        if (gameState == GameState.WIN || gameState == GameState.OVER) return
        for (i in items.size - 1 downTo 0) {
            if (i != 3 && i != 7 && i != 11 && i != 15) {
                when {
                    i < 3 -> {
                        for (j in 3 downTo i + 1) {
                            if (items[j] == 0) {
                                items[j] = items[i]
                                items[i] = 0
                            }
                        }
                    }
                    i < 7 -> {
                        for (j in 7 downTo i + 1) {
                            if (items[j] == 0) {
                                items[j] = items[i]
                                items[i] = 0
                            }
                        }
                    }
                    i < 11 -> {
                        for (j in 11 downTo i + 1) {
                            if (items[j] == 0) {
                                items[j] = items[i]
                                items[i] = 0
                            }
                        }
                    }
                    i < 15 -> {
                        for (j in 15 downTo i + 1) {
                            if (items[j] == 0) {
                                items[j] = items[i]
                                items[i] = 0
                            }
                        }
                    }
                }
            }
        }
        for (i in 3 downTo 1) {
            if (items[i] == items[i - 1] && items[i] != 0) {
                items[i] += 1
                mergedPos.add(i)
                score += 2.toDouble().pow(items[i]).toInt()
                if (i == 1) {
                    items[0] = 0
                } else {
                    for (j in i - 1 downTo 1) {
                        items[j] = items[j - 1]
                    }
                    items[0] = 0
                }
            }
        }
        for (i in 7 downTo 5) {
            if (items[i] == items[i - 1] && items[i] != 0) {
                items[i] += 1
                mergedPos.add(i)
                score += 2.toDouble().pow(items[i]).toInt()
                if (i == 1) {
                    items[4] = 0
                } else {
                    for (j in i - 1 downTo 5) {
                        items[j] = items[j - 1]
                    }
                    items[4] = 0
                }
            }
        }
        for (i in 11 downTo 9) {
            if (items[i] == items[i - 1] && items[i] != 0) {
                items[i] += 1
                mergedPos.add(i)
                score += 2.toDouble().pow(items[i]).toInt()
                if (i == 1) {
                    items[8] = 0
                } else {
                    for (j in i - 1 downTo 9) {
                        items[j] = items[j - 1]
                    }
                    items[8] = 0
                }
            }
        }
        for (i in 15 downTo 13) {
            if (items[i] == items[i - 1] && items[i] != 0) {
                items[i] += 1
                mergedPos.add(i)
                score += 2.toDouble().pow(items[i]).toInt()
                if (i == 1) {
                    items[12] = 0
                } else {
                    for (j in i - 1 downTo 13) {
                        items[j] = items[j - 1]
                    }
                    items[12] = 0
                }
            }
        }
        invalidate()
        if (!(backupList.toTypedArray() contentEquals items.toTypedArray())) genNewItem()
        checkIfWinOrLose()
    }

    fun goLeft() {
        canUndo = true
        backupList = items.toList()
        if (gameState == GameState.WIN || gameState == GameState.OVER) return
        for (i in 0 until items.size) {
            if (i != 0 && i != 4 && i != 8 && i != 12) {
                when {
                    i > 12 -> {
                        for (j in 12 until i) {
                            if (items[j] == 0) {
                                items[j] = items[i]
                                items[i] = 0
                            }
                        }
                    }
                    i > 8 -> {
                        for (j in 8 until i) {
                            if (items[j] == 0) {
                                items[j] = items[i]
                                items[i] = 0
                            }
                        }
                    }
                    i > 4 -> {
                        for (j in 4 until i) {
                            if (items[j] == 0) {
                                items[j] = items[i]
                                items[i] = 0
                            }
                        }
                    }
                    i > 0 -> {
                        for (j in 0 until i) {
                            if (items[j] == 0) {
                                items[j] = items[i]
                                items[i] = 0
                            }
                        }
                    }
                }
            }
        }
        for (i in 0 until 3) {
            if (items[i] == items[i + 1] && items[i] != 0) {
                items[i] += 1
                mergedPos.add(i)
                score += 2.toDouble().pow(items[i]).toInt()
                if (i == 2) {
                    items[3] = 0
                } else {
                    for (j in i + 1 until 3) {
                        items[j] = items[j + 1]
                    }
                    items[3] = 0
                }
            }
        }
        for (i in 4 until 7) {
            if (items[i] == items[i + 1] && items[i] != 0) {
                items[i] += 1
                mergedPos.add(i)
                score += 2.toDouble().pow(items[i]).toInt()
                if (i == 6) {
                    items[7] = 0
                } else {
                    for (j in i + 1 until 7) {
                        items[j] = items[j + 1]
                    }
                    items[7] = 0
                }
            }
        }
        for (i in 8 until 11) {
            if (items[i] == items[i + 1] && items[i] != 0) {
                items[i] += 1
                mergedPos.add(i)
                score += 2.toDouble().pow(items[i]).toInt()
                if (i == 10) {
                    items[11] = 0
                } else {
                    for (j in i + 1 until 11) {
                        items[j] = items[j + 1]
                    }
                    items[11] = 0
                }
            }
        }
        for (i in 12 until 15) {
            if (items[i] == items[i + 1] && items[i] != 0) {
                items[i] += 1
                mergedPos.add(i)
                score += 2.toDouble().pow(items[i]).toInt()
                if (i == 14) {
                    items[15] = 0
                } else {
                    for (j in i + 1 until 15) {
                        items[j] = items[j + 1]
                    }
                    items[15] = 0
                }
            }
        }
        if (!(backupList.toTypedArray() contentEquals items.toTypedArray())) genNewItem()
        checkIfWinOrLose()
        invalidate()
    }

    fun goUp() {
        canUndo = true
        backupList = items.toList()
        if (gameState == GameState.WIN || gameState == GameState.OVER) return
        for (i in 0 until items.size) {
            if (i != 0 && i != 1 && i != 2 && i != 3) {
                when (i) {
                    4, 8, 12 -> {
                        for (j in 0 until i step 4) {
                            if (items[j] == 0) {
                                items[j] = items[i]
                                items[i] = 0
                            }
                        }
                    }
                    5, 9, 13 -> {
                        for (j in 1 until i step 4) {
                            if (items[j] == 0) {
                                items[j] = items[i]
                                items[i] = 0
                            }
                        }
                    }
                    6, 10, 14 -> {
                        for (j in 2 until i step 4) {
                            if (items[j] == 0) {
                                items[j] = items[i]
                                items[i] = 0
                            }
                        }
                    }
                    7, 11, 15 -> {
                        for (j in 3 until i step 4) {
                            if (items[j] == 0) {
                                items[j] = items[i]
                                items[i] = 0
                            }
                        }
                    }
                }
            }
        }
        for (i in 0 until 12 step 4) {
            if (items[i] == items[i + 4] && items[i] != 0) {
                items[i] += 1
                mergedPos.add(i)
                score += 2.toDouble().pow(items[i]).toInt()
                if (i == 8) {
                    items[12] = 0
                } else {
                    for (j in i + 4 until 12 step 4) {
                        items[j] = items[j + 4]
                    }
                    items[12] = 0
                }
            }
        }
        for (i in 1 until 13 step 4) {
            if (items[i] == items[i + 4] && items[i] != 0) {
                items[i] += 1
                mergedPos.add(i)
                score += 2.toDouble().pow(items[i]).toInt()
                if (i == 9) {
                    items[13] = 0
                } else {
                    for (j in i + 4 until 13 step 4) {
                        items[j] = items[j + 4]
                    }
                    items[13] = 0
                }
            }
        }
        for (i in 2 until 14 step 4) {
            if (items[i] == items[i + 4] && items[i] != 0) {
                items[i] += 1
                mergedPos.add(i)
                score += 2.toDouble().pow(items[i]).toInt()
                if (i == 10) {
                    items[14] = 0
                } else {
                    for (j in i + 4 until 14 step 4) {
                        items[j] = items[j + 4]
                    }
                    items[14] = 0
                }
            }
        }
        for (i in 3 until 15 step 4) {
            if (items[i] == items[i + 4] && items[i] != 0) {
                items[i] += 1
                mergedPos.add(i)
                score += 2.toDouble().pow(items[i]).toInt()
                if (i == 11) {
                    items[15] = 0
                } else {
                    for (j in i + 4 until 15 step 4) {
                        items[j] = items[j + 4]
                    }
                    items[15] = 0
                }
            }
        }
        if (!(backupList.toTypedArray() contentEquals items.toTypedArray())) genNewItem()
        checkIfWinOrLose()
        invalidate()
    }

    fun goDown() {
        canUndo = true
        backupList = items.toList()
        if (gameState == GameState.WIN || gameState == GameState.OVER) return
        for (i in items.size - 1 downTo 0) {
            if (i != 12 && i != 13 && i != 14 && i != 15) {
                when (i) {
                    4, 8, 0 -> {
                        for (j in 12 downTo i step 4) {
                            if (items[j] == 0) {
                                items[j] = items[i]
                                items[i] = 0
                            }
                        }
                    }
                    5, 9, 1 -> {
                        for (j in 13 downTo i step 4) {
                            if (items[j] == 0) {
                                items[j] = items[i]
                                items[i] = 0
                            }
                        }
                    }
                    6, 10, 2 -> {
                        for (j in 14 downTo i step 4) {
                            if (items[j] == 0) {
                                items[j] = items[i]
                                items[i] = 0
                            }
                        }
                    }
                    7, 11, 3 -> {
                        for (j in 15 downTo i step 4) {
                            if (items[j] == 0) {
                                items[j] = items[i]
                                items[i] = 0
                            }
                        }
                    }
                }
            }
        }
        for (i in 12 downTo 4 step 4) {
            if (items[i] == items[i - 4] && items[i] != 0) {
                items[i] += 1
                mergedPos.add(i)
                score += 2.toDouble().pow(items[i]).toInt()
                if (i == 4) {
                    items[0] = 0
                } else {
                    for (j in i - 4 downTo 4 step 4) {
                        items[j] = items[j - 4]
                    }
                    items[0] = 0
                }
            }
        }
        for (i in 13 downTo 5 step 4) {
            if (items[i] == items[i - 4] && items[i] != 0) {
                items[i] += 1
                mergedPos.add(i)
                score += 2.toDouble().pow(items[i]).toInt()
                if (i == 5) {
                    items[1] = 0
                } else {
                    for (j in i - 4 downTo 5 step 4) {
                        items[j] = items[j - 4]
                    }
                    items[1] = 0
                }
            }
        }
        for (i in 14 downTo 6 step 4) {
            if (items[i] == items[i - 4] && items[i] != 0) {
                items[i] += 1
                mergedPos.add(i)
                score += 2.toDouble().pow(items[i]).toInt()
                if (i == 6) {
                    items[2] = 0
                } else {
                    for (j in i - 4 downTo 6 step 4) {
                        items[j] = items[j - 4]
                    }
                    items[2] = 0
                }
            }
        }
        for (i in 15 downTo 7 step 4) {
            if (items[i] == items[i - 4] && items[i] != 0) {
                items[i] += 1
                mergedPos.add(i)
                score += 2.toDouble().pow(items[i]).toInt()
                if (i == 7) {
                    items[3] = 0
                } else {
                    for (j in i - 4 downTo 7 step 4) {
                        items[j] = items[j - 4]
                    }
                    items[3] = 0
                }
            }
        }
        if (!(backupList.toTypedArray() contentEquals items.toTypedArray())) genNewItem()
        checkIfWinOrLose()
        invalidate()
    }

    fun genNewItem() {
        val numOfEmptySlot = items.filter { it == 0 }.size
        val genSlot = (0 until numOfEmptySlot).random()
        var index = 0
        for (i in 0 until items.size) {
            if (items[i] == 0) {
                if (index == genSlot) {
                    val itemNum = (1..2).random()
                    items[i] = itemNum
                    newItemPos = i
                    break
                } else {
                    index += 1
                }
            }
        }
    }

    fun undo() {
        if (canUndo && gameState == GameState.PLAYING) {
            items = backupList.toMutableList()
            invalidate()
        }
    }

    private fun checkIfWinOrLose() {
        val numOfEmptySlot = items.filter { it == 0 }.size
        var isHaveEqual = false
        if (items[0] == items[1] || items[1] == items[2] || items[2] == items[3] || items[4] == items[5] || items[5] == items[6] || items[6] == items[7]
            || items[8] == items[9] || items[9] == items[10] || items[10] == items[11] || items[12] == items[13] || items[13] == items[14] || items[14] == items[15]) isHaveEqual = true
        if (items[0] == items[4] || items[4] == items[8] || items[8] == items[12] || items[1] == items[5] || items[5] == items[9] || items[9] == items[13]
            || items[2] == items[6] || items[6] == items[10] || items[10] == items[14] || items[3] == items[7] || items[7] == items[11] || items[11] == items[15]) isHaveEqual = true
        if (numOfEmptySlot == 0 && !isHaveEqual) {
            displayGameOver()
            return
        }
        if (items.contains(11)) {
            displayGameWin()
        }
    }

    private fun displayGameOver() {
        gameState = GameState.OVER
        onGameOver?.let { it() }
    }

    private fun displayGameWin() {
        gameState = GameState.WIN
        onGameWin?.let { it() }
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        val startTime = Calendar.getInstance().timeInMillis
        canvas?.drawRoundRect(left, top, right, bottom, boardItemInset, boardItemInset, boardPaint)
        for (i in items.indices) {
            if (items[i] > 0) {
                canvas?.drawRoundRect(rects[i], boardItemInset / 1.5f, boardItemInset / 1.5f, itemPaint[items[i] - 1])
                val text = 2.toDouble().pow(items[i]).toInt().toString()
                when (items[i]) {
                    1, 2, 3 -> {
                        textPaint1.textSize = width / 8f
                        textPaint2.textSize = width / 8f
                    }
                    4, 5, 6 -> {
                        textPaint1.textSize = 0.78f * width / 8f
                        textPaint2.textSize = 0.78f * width / 8f
                    }
                    7, 8, 9 -> {
                        textPaint1.textSize = 0.66f * width / 8f
                        textPaint2.textSize = 0.66f * width / 8f
                    }
                    else -> {
                        textPaint1.textSize = 0.58f * width / 8f
                        textPaint2.textSize = 0.58f * width / 8f
                    }
                }
                if (items[i] < 3) {
                    canvas?.drawText(text, (rects[i].left + rects[i].right) / 2f - (text.length * textPaint1.textSize) / 3.5f, (rects[i].top + rects[i].bottom) / 2 + textPaint1.textSize / 3f, textPaint1)
                } else {
                    canvas?.drawText(text, (rects[i].left + rects[i].right) / 2f - (text.length * textPaint2.textSize) / 3.5f, (rects[i].top + rects[i].bottom) / 2 + textPaint2.textSize / 3f, textPaint2)
                }
                if (i == newItemPos) {
                    val animateLeft: ObjectAnimator = ObjectAnimator.ofFloat(rects[i], "left", (rects[i].left + rects[i].right) / 2 - (-rects[i].left + rects[i].right) / 4, rects[i].left)
                    val animateRight: ObjectAnimator = ObjectAnimator.ofFloat(rects[i], "right", (rects[i].left + rects[i].right) / 2 + (-rects[i].left + rects[i].right) / 4, rects[i].right)
                    val animateTop: ObjectAnimator = ObjectAnimator.ofFloat(rects[i], "top", (rects[i].top + rects[i].bottom) / 2 - (-rects[i].top + rects[i].bottom) / 4, rects[i].top)
                    val animateBottom: ObjectAnimator = ObjectAnimator.ofFloat(rects[i], "bottom", (rects[i].top + rects[i].bottom) / 2 + (-rects[i].top + rects[i].bottom) / 4, rects[i].bottom)
                    animateRight.addUpdateListener {
                        newItemPos = -1
                        postInvalidate()
                    }
                    val rectAnimation = AnimatorSet()
                    rectAnimation.interpolator = AccelerateDecelerateInterpolator()
                    rectAnimation.playTogether(animateLeft, animateRight, animateBottom, animateTop)
                    rectAnimation.setDuration(200).start()
                }
            } else {
                canvas?.drawRoundRect(rects[i], boardItemInset / 1.5f, boardItemInset / 1.5f, boardItemPaint)
            }
        }
        mergedPos.forEach { i ->
            val animateLeft: ObjectAnimator = ObjectAnimator.ofFloat(rects[i], "left", rects[i].left - boardItemInset, rects[i].left)
            val animateRight: ObjectAnimator = ObjectAnimator.ofFloat(rects[i], "right", rects[i].right + boardItemInset, rects[i].right)
            val animateTop: ObjectAnimator = ObjectAnimator.ofFloat(rects[i], "top", rects[i].top - boardItemInset, rects[i].top)
            val animateBottom: ObjectAnimator = ObjectAnimator.ofFloat(rects[i], "bottom", rects[i].bottom + boardItemInset, rects[i].bottom)
            animateRight.addUpdateListener {
                postInvalidate()
            }
            val rectAnimation = AnimatorSet()
            rectAnimation.interpolator = AccelerateDecelerateInterpolator()
            rectAnimation.playTogether(animateLeft, animateRight, animateBottom, animateTop)
            rectAnimation.setDuration(200).start()
        }
        mergedPos.clear()
        when (gameState) {
            GameState.PLAYING -> {

            }
            GameState.OVER -> {
                canvas?.drawRoundRect(left, top, right, bottom, boardItemInset, boardItemInset, boardPaintOver)
                canvas?.drawText("You lose!", width / 2f - 6 * textPaint3.textSize / 3f, height / 2 - textPaint3.textSize / 3f, textPaint3)
            }
            GameState.WIN -> {
                canvas?.drawRoundRect(left, top, right, bottom, 16f, 16f, boardPaintWin)
                canvas?.drawText("You win!", width / 2f - 5.5f * textPaint3.textSize / 3f, height / 2 - textPaint3.textSize / 3f, textPaint3)
            }
        }
        Log.i("aaa", (Calendar.getInstance().timeInMillis - startTime).toString())
    }
}
