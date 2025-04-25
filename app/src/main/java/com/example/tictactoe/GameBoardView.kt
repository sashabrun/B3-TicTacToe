package com.example.tictactoe

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout

class GameBoardView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private val paint = Paint().apply {
        color = Color.BLACK
        strokeWidth = 12f
        style = Paint.Style.STROKE
        isAntiAlias = true
    }

    private var winningCells: Array<Int>? = null

    private val cellPositions = mutableListOf<Pair<Float, Float>>()
    private var cellSize = 0f

    init {
        setWillNotDraw(false)
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)

        if (changed || cellPositions.isEmpty()) {
            updateCellPositions()
        }
    }

    private fun updateCellPositions() {
        cellPositions.clear()

        if (childCount > 0) {
            val gridLayout = getChildAt(0)

            val gridChildCount = if (gridLayout is android.view.ViewGroup) gridLayout.childCount else 0

            if (gridChildCount == 9) {
                val firstButton = (gridLayout as android.view.ViewGroup).getChildAt(0)
                cellSize = firstButton.width.toFloat()

                for (i in 0 until 9) {
                    val button = (gridLayout as android.view.ViewGroup).getChildAt(i)
                    val centerX = button.left + button.width / 2f
                    val centerY = button.top + button.height / 2f
                    cellPositions.add(Pair(centerX, centerY))
                }
            }
        }
    }

    fun highlightWinningLine(winningCells: Array<Int>) {
        this.winningCells = winningCells
        invalidate()
    }

    fun clearWinningLine() {
        this.winningCells = null
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
    }

    override fun dispatchDraw(canvas: Canvas) {
        super.dispatchDraw(canvas)

        winningCells?.let { cells ->
            if (cells.size >= 2 && cellPositions.isNotEmpty()) {
                val startPos = cellPositions.getOrNull(cells[0])
                val endPos = cellPositions.getOrNull(cells[cells.size - 1])

                if (startPos != null && endPos != null) {
                    canvas.drawLine(
                        startPos.first, startPos.second,
                        endPos.first, endPos.second,
                        paint
                    )
                }
            }
        }
    }
}