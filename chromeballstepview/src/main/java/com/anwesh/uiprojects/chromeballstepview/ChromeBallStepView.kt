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
val scDiv : Float = 0.51f
val scGap : Float = 0.05f
