package com.example.cc

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.GestureDetector.SimpleOnGestureListener
import android.view.MotionEvent
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GestureDetectorCompat
import com.example.cc.databinding.ActivityMainBinding
import kotlin.math.atan2


class MainActivity : AppCompatActivity() {

    private lateinit var viewBinding: ActivityMainBinding
    private lateinit var gestureDetectorCompat: GestureDetectorCompat

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)
        Handler(Looper.getMainLooper()).postDelayed({
            initData()
            beginGame()
            viewBinding.board.invalidate()
            viewBinding.boardBg.invalidate()
        }, 300)
        viewBinding.board.setOnClickListener {
        }
        gestureDetectorCompat = GestureDetectorCompat(this, object : SimpleOnGestureListener() {
            override fun onFling(
                e1: MotionEvent,
                e2: MotionEvent,
                velocityX: Float,
                velocityY: Float
            ): Boolean {
                val angle =
                    Math.toDegrees(atan2((e1.y - e2.y).toDouble(), (e2.x - e1.x).toDouble()))
                        .toFloat()
                if (angle > -45 && angle <= 45) {
                    viewBinding.board.goRight()
                    displayScore()
                    return true
                }
                if (angle >= 135 && angle < 180 || angle < -135 && angle > -180) {
                    viewBinding.board.goLeft()
                    displayScore()
                    return true
                }
                if (angle < -45 && angle >= -135) {
                    viewBinding.board.goDown()
                    displayScore()
                    return true
                }
                if (angle > 45 && angle <= 135) {
                    viewBinding.board.goUp()
                    displayScore()
                    return true
                }
                return false
            }
        })
        viewBinding.board.setOnTouchListener { v, event ->
            gestureDetectorCompat.onTouchEvent(event)
            false
        }
        viewBinding.buttonUndo.setOnClickListener {
            viewBinding.board.undo()
            displayScore()
        }
        viewBinding.buttonNew.setOnClickListener {
            saveScoreIfNeed()
            initData()
            beginGame()
            viewBinding.board.invalidate()
        }
        viewBinding.board.onGameOver = {
            saveScoreIfNeed()
        }
        viewBinding.board.onGameWin = {
            saveScoreIfNeed()
        }
    }

    private fun saveScoreIfNeed() {
        val sharedPreferences = getSharedPreferences("2048SP", MODE_PRIVATE)
        val myEdit = sharedPreferences.edit()
        if (viewBinding.board.score > sharedPreferences.getInt("bestScore", 0)) {
            myEdit.putInt("bestScore", viewBinding.board.score)
            myEdit.apply()
        }
    }

    private fun displayScore() {
        viewBinding.textScore.text = viewBinding.board.score.toString()
    }

    private fun initData() = with(viewBinding.board) {
        setup()
        viewBinding.boardBg.setup()
    }

    private fun beginGame() = with(viewBinding.board) {
        resetBoard()
        genNewItem()
        genNewItem()
        val sharedPreferences = getSharedPreferences("2048SP", MODE_PRIVATE)
        viewBinding.textBest.text = sharedPreferences.getInt("bestScore", 0).toString()
        viewBinding.textScore.text = "0"
    }
}
