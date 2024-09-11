package chenchen.engine.span.rendererImpl

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.Rect
import android.graphics.RectF
import android.text.Layout
import android.text.TextPaint
import chenchen.engine.span.renderer.AbsSpanRenderer
import chenchen.engine.span.renderer.SpanInfo
import chenchen.engine.span.span.Colors
import chenchen.engine.span.span.Colors.Gradient.Companion.generate
import chenchen.engine.span.spanImpl.ColorSpan

/**
 * 纯色渲染器，可以充当前景或背景
 * @author: chenchen
 * @since: 2024/9/3 16:40
 */
class ColorRenderer<Span : ColorSpan>(clazz: Class<Span>) : AbsSpanRenderer<Span>(clazz) {

    private val path = Path()
    private val rectF = RectF()
    private val radius = FloatArray(8)
    override fun onDraw(
        canvas: Canvas, paint: TextPaint,
        layout: Layout, spanInfo: SpanInfo<Span>,
        lineIndex: Int, baseline: Int, drawRect: Rect
    ) {
        if (spanInfo.span.isMatchText) {
            drawRect.set(
                drawRect.left,
                baseline + paint.fontMetricsInt.ascent,
                drawRect.right,
                baseline + paint.fontMetricsInt.descent
            )
        }
        val margins = spanInfo.span.margins
        drawRect.inset(margins.left, margins.top, margins.right, margins.bottom)
        rectF.set(drawRect)
        path.reset()
        path.addRoundRect(rectF, spanInfo.span.radius.fill(radius), Path.Direction.CW)
        when (spanInfo.span.color) {
            is Colors.Gradient -> {
                paint.color = Color.BLACK
                paint.shader = spanInfo.span.color.generate(rectF)
            }
            is Colors.Solid -> {
                paint.color = spanInfo.span.color.color
                paint.shader = null
            }
        }
        paint.style = Paint.Style.FILL
        canvas.drawPath(path, paint)
    }
}