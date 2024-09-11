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
import chenchen.engine.span.spanImpl.LineSpan
import chenchen.engine.span.span.Align.*

/**
 * 横线渲染器，可以充当前景或背景
 * @author: chenchen
 * @since: 2024/9/3 16:40
 */
class LineSpanRenderer<Span : LineSpan>(clazz: Class<Span>) : AbsSpanRenderer<Span>(clazz) {
    private val path = Path()
    private val rectF = RectF()
    private val radius = FloatArray(8)
    override fun onDraw(
        canvas: Canvas, paint: TextPaint,
        layout: Layout, spanInfo: SpanInfo<Span>,
        lineIndex: Int, baseline: Int, drawRect: Rect) {
        val margins = spanInfo.span.spacing
        if (margins.top != 0 || margins.bottom != 0) {
            throw IllegalArgumentException(
                "不可用margins控制上下间距或大小，控制大小使用stokeHeight，控制上下间距使用offsets"
            )
        }
        rectF.set(
            drawRect.left.toFloat() + margins.left,
            drawRect.top.toFloat(),
            drawRect.right.toFloat() - margins.right,
            drawRect.top + spanInfo.span.strokeWidth
        )
        val fontMetrics = paint.fontMetrics
        val y = when (spanInfo.span.align) {
            Top -> drawRect.top.toFloat() - rectF.height() / 2f
            Ascent -> baseline + fontMetrics.ascent - rectF.height() / 2f
            LineCenter -> (drawRect.height() - rectF.height()) / 2f
            TextCenter -> (baseline + (fontMetrics.ascent + fontMetrics.descent) / 2f) - rectF.height() / 2f
            Baseline -> baseline.toFloat() - rectF.height() / 2f
            Descent -> baseline + fontMetrics.descent - rectF.height() / 2f
            Bottom -> drawRect.bottom - rectF.height() / 2
        }
        rectF.offsetTo(rectF.left, y)
        rectF.offset(spanInfo.span.offsets.x.toFloat(), spanInfo.span.offsets.y.toFloat())
        when (spanInfo.span.strokeColor) {
            is Colors.Gradient -> {
                paint.color = Color.BLACK
                paint.shader = spanInfo.span.strokeColor.generate(rectF)
            }
            is Colors.Solid -> {
                paint.color = spanInfo.span.strokeColor.color
                paint.shader = null
            }
        }
        path.reset()
        path.addRoundRect(rectF, spanInfo.span.radius.fill(radius), Path.Direction.CW)
        paint.style = Paint.Style.FILL
        canvas.drawPath(path, paint)
    }
}