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
