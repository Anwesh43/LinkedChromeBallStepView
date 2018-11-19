package com.anwesh.uiprojects.chromeballstepview

/**
 * Created by anweshmishra on 19/11/18.
 */

import android.view.View
import android.view.MotionEvent
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Color
import android.graphics.RectF
import android.app.Activity
import android.content.Context

val nodes : Int = 5
val ball_color : Int = Color.parseColor("#1A237E")
val arc_colors : Array<String> = arrayOf("#f44336", "#FDD835", "#388E3C")
val SIZE_FACTOR : Int = 3
val STROKE_FACTOR : Int = 60
val scDiv : Double = 0.51
val scGap : Float = 0.05f

fun Int.getInverse() : Float = 1f / this

fun Float.divideScale(i : Int, n : Int) : Float = Math.max(n.getInverse(), this - i * n.getInverse())

fun Float.getScaleFactor() : Float = Math.floor(this / scDiv).toFloat()

fun Float.getMirrorValue(a : Int, b : Int) : Float = (1 - this) * a.getInverse() + this * b.getInverse()

fun Float.updateScale(dir : Float, a : Int, b : Int) : Float = this.getMirrorValue(a, b) * dir * scGap

fun Canvas.drawCBSNode(i : Int, scale : Float, paint : Paint) {
    val w : Float = width.toFloat()
    val h : Float = height.toFloat()
    val gap : Float = w / (nodes + 1)
    val size : Float = gap / SIZE_FACTOR
    val r : Float = size/3
    val sc1 : Float = scale.divideScale(0, 2)
    val sc2 : Float = scale.divideScale(1, 2)
    save()
    translate(gap * (i + 1), h/2)
    rotate(180f * sc2)
    paint.color = ball_color
    drawCircle(0f, 0f, r, paint)
    val deg : Float = 360f / arc_colors.size
    arc_colors.forEachIndexed {j, color ->
        val sc : Float = sc1.divideScale(j, arc_colors.size)
        paint.color = Color.parseColor(color)
        drawArc(RectF(-size, -size, size, size), 0f, deg * sc, true, paint)
    }
    restore()
}

class ChromeBallStepView(ctx : Context) : View(ctx) {

    private val paint : Paint = Paint(Paint.ANTI_ALIAS_FLAG)

    override fun onDraw(canvas : Canvas) {

    }

    override fun onTouchEvent(event : MotionEvent) : Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {

            }
        }
        return true
    }
}
