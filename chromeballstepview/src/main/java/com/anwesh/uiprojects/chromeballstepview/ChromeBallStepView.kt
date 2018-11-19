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
import android.util.Log

val nodes : Int = 5
val ball_color : Int = Color.parseColor("#0288D1")
val arc_colors : Array<String> = arrayOf("#ef5350", "#FDD835", "#43A047")
val SIZE_FACTOR : Int = 3
val scDiv : Double = 0.51
val scGap : Float = 0.05f
val BACK_COLOR : Int = Color.parseColor("#BDBDBD")
val DELAY : Long = 30

fun Int.getInverse() : Float = 1f / this

fun Float.divideScale(i : Int, n : Int) : Float = Math.min(n.getInverse(), Math.max(0f, this - i * n.getInverse())) * n

fun Float.getScaleFactor() : Float = Math.floor(this / scDiv).toFloat()

fun Float.getMirrorValue(a : Int, b : Int) : Float = (1 - this) * a.getInverse() + this * b.getInverse()

fun Float.updateScale(dir : Float, a : Int, b : Int) : Float = getScaleFactor().getMirrorValue(a, b) * dir * scGap

fun Canvas.drawCBSNode(i : Int, scale : Float, paint : Paint) {
    val w : Float = width.toFloat()
    val h : Float = height.toFloat()
    val gap : Float = w / (nodes + 1)
    val size : Float = gap / SIZE_FACTOR
    val r : Float = size / 2
    val r1 : Float = size / 1.8f
    val sc1 : Float = scale.divideScale(0, 2)
    val sc2 : Float = scale.divideScale(1, 2)
    save()
    translate(gap * (i + 1), h/2)
    rotate(120f + 90f * sc2)
    val deg : Float = 360f / arc_colors.size
    Log.d("sc1","${sc1}")
    arc_colors.forEachIndexed {j, color ->
        val sc : Float = sc1.divideScale(j, arc_colors.size)
        paint.color = Color.parseColor(color)
        drawArc(RectF(-size, -size, size, size), deg * j, deg * sc, true, paint)
    }
    paint.color = BACK_COLOR
    drawCircle(0f, 0f, r1, paint)
    paint.color = ball_color
    drawCircle(0f, 0f, r, paint)
    restore()
}

class ChromeBallStepView(ctx : Context) : View(ctx) {

    private val paint : Paint = Paint(Paint.ANTI_ALIAS_FLAG)

    private val renderer : Renderer = Renderer(this)

    override fun onDraw(canvas : Canvas) {
        renderer.render(canvas, paint)
    }

    override fun onTouchEvent(event : MotionEvent) : Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                renderer.handleTap()
            }
        }
        return true
    }

    data class State(var scale : Float = 0f, var dir : Float = 0f, var prevScale : Float = 0f) {

        fun update(cb : (Float) -> Unit) {
            val k : Float = scale.updateScale(dir, arc_colors.size, 1)
            scale += k
            Log.d("updated scale by", "${k}")
            if (Math.abs(scale - prevScale) > 1) {
                scale = prevScale + dir
                dir = 0f
                prevScale = scale
                cb(prevScale)
            }
        }

        fun startUpdating(cb : () -> Unit) {
            if (dir == 0f) {
                dir = 1f - 2 * prevScale
                cb()
            }
        }
    }

    data class Animator(var view : View, var animated : Boolean = false) {

        fun animate(cb : () -> Unit) {
            if (animated) {
                cb()
                try {
                    Thread.sleep(DELAY)
                    view.invalidate()
                } catch(ex : Exception) {

                }
            }
        }

        fun start() {
            if (!animated) {
                animated = true
                view.postInvalidate()
            }
        }

        fun stop() {
            if (animated) {
                animated = false
            }
        }
    }

    data class CBSNode(var i : Int, val state : State = State()) {

        private var next : CBSNode? = null

        private var prev : CBSNode? = null

        init {
            addNeighbor()
        }

        fun addNeighbor() {
            if (i < nodes - 1) {
                next = CBSNode(i + 1)
                next?.prev = this
            }
        }

        fun draw(canvas : Canvas, paint : Paint) {
            canvas.drawCBSNode(i, state.scale, paint)
            next?.draw(canvas, paint)
        }

        fun update(cb : (Int, Float) -> Unit) {
            state.update {
                cb(i, it)
            }
        }

        fun startUpdating(cb : () -> Unit) {
            state.startUpdating(cb)
        }

        fun getNext(dir : Int, cb : () -> Unit) : CBSNode {
            var curr : CBSNode? = prev
            if (dir == 1) {
                curr = next
            }
            if (curr != null) {
                return curr
            }
            cb()
            return this
        }
    }

    data class ChromeBallStep(var i : Int) {

        private val root : CBSNode = CBSNode(0)

        private var curr : CBSNode = root

        private var dir : Int = 1

        fun draw(canvas : Canvas, paint : Paint) {
            root.draw(canvas, paint)
        }

        fun update(cb : (Int, Float) -> Unit) {
            curr.update {i, scl ->
                curr = curr.getNext(dir) {
                    dir *= -1
                }
                cb(i, scl)
            }
        }

        fun startUpdating(cb : () -> Unit) {
            curr.startUpdating(cb)
        }
    }

    data class Renderer(var view : ChromeBallStepView) {
        private val animator : Animator = Animator(view)

        private var curr : ChromeBallStep = ChromeBallStep(0)

        fun render(canvas : Canvas, paint : Paint) {
            canvas.drawColor(BACK_COLOR)
            curr.draw(canvas, paint)
            animator.animate {
                curr.update {i, scl ->
                    animator.stop()
                }
            }
        }

        fun handleTap() {
            curr.startUpdating {
                animator.start()
            }
        }
    }

    companion object {
        fun create(activity : Activity) : ChromeBallStepView {
            val view : ChromeBallStepView = ChromeBallStepView(activity)
            activity.setContentView(view)
            return view
        }
    }
}
