package com.example.colors

import android.graphics.RectF

class ItemRect() : RectF() {

    constructor(left: Float, top: Float, right: Float, bottom: Float) : this() {
        this.left = left
        this.right = right
        this.top = top
        this.bottom = bottom
    }

    fun setTop(top: Float) {
        this.top = top
    }

    fun setBottom(bottom: Float) {
        this.bottom = bottom
    }

    fun setRight(right: Float) {
        this.right = right
    }

    fun setLeft(left: Float) {
        this.left = left
    }
}
