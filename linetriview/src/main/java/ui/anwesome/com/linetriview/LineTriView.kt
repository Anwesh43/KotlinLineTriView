package ui.anwesome.com.linetriview

/**
 * Created by anweshmishra on 12/03/18.
 */
import android.view.*
import android.content.*
import android.graphics.*
class LineTriView(ctx : Context) : View(ctx) {
    val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    override fun onDraw(canvas : Canvas) {

    }
    override fun onTouchEvent(event : MotionEvent) : Boolean {
        when(event.action) {
            MotionEvent.ACTION_DOWN -> {

            }
        }
        return true
    }
    data class State(var prevScale : Float = 0f, var dir : Float = 0f, var j : Int = 0, var jdir : Int = 1) {
        val scales : Array<Float> = arrayOf(0f, 0f, 0f)
        fun update(stopcb : (Float) -> Unit) {
            scales[j] += dir * 0.1f
            if(Math.abs(scales[j] - prevScale) > 1) {
                scales[j] = prevScale + dir
                j += jdir
                if(j == scales.size || j == -1) {
                    jdir *= -1
                    j += jdir
                    dir = 0f
                    prevScale = scales[j]
                    stopcb(prevScale)
                }
            }
        }
        fun startUpdating(startcb : () -> Unit) {
            if (dir == 0f) {
                dir = 1 - 2 * prevScale
                startcb()
            }
        }
    }
    data class Animator(var view : View, var animated : Boolean = false) {
        fun animate(updatecb : () -> Unit) {
            if (animated) {
                updatecb()
                try {
                    Thread.sleep(50)
                    view.invalidate()
                }
                catch (ex : Exception) {

                }
            }
        }
        fun start () {
            if (!animated) {
                animated = true
                view.postInvalidate()
            }
        }
        fun stop () {
            if (animated) {
                animated = false
            }
        }
    }
    data class LineTri(var i : Int) {
        val state = State()
        fun draw(canvas : Canvas, paint : Paint) {
            paint.color = Color.parseColor("#283593")
            val w = canvas.width.toFloat()
            val h = canvas.height.toFloat()
            for(i in 0..1) {
                canvas.save()
                canvas.translate(w/2, h/2)
                canvas.scale(1f, (1f - 2* i))
                canvas.rotate(180f * state.scales[2])
                val size = w/4 * state.scales[0]
                val path = Path()
                path.moveTo(-size, -h/6)
                path.lineTo(size , -h/6)
                path.lineTo(0f, -h/6 + (h/6) * state.scales[1])
                canvas.drawPath(path, paint)
                canvas.restore()
            }
        }
        fun update(stopcb : (Float) -> Unit) {
            state.update(stopcb)
        }
        fun startUpdating(startcb : () -> Unit) {
            state.startUpdating(startcb)
        }
    }
    data class Renderer(var view : LineTriView) {
        val lineTri : LineTri = LineTri(0)
        val animator : Animator = Animator(view)
        fun render(canvas : Canvas, paint : Paint) {
            canvas.drawColor(Color.parseColor("#212121"))
            lineTri.draw(canvas, paint)
            animator.animate {
                lineTri.update {
                    animator.stop()
                }
            }
        }
        fun handleTap() {
            lineTri.startUpdating {
                animator.stop()
            }
        }
    }
}