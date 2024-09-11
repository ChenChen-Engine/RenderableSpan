package chenchen.engine.span.renderer

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.text.Layout
import android.text.Spanned
import android.text.TextPaint
import android.text.style.CharacterStyle
import androidx.core.text.getSpans
import chenchen.engine.span.span.RenderableSpan
import kotlin.math.max
import kotlin.math.min

/**
 * @Author: chenchen
 * @CreateDate: 2022/12/7 18:29
 * @Description: 渲染器基类
 */
abstract class AbsSpanRenderer<Span : RenderableSpan>(
    protected val clazz: Class<Span>
) {

    private val rect = Rect()
    private val workPaint = TextPaint()

    fun onDraw(canvas: Canvas, paint: Paint, text: Spanned, layout: Layout) {
        workPaint.set(paint)
        val spans = text.getSpans(0, text.length, clazz)
        spans.forEach { span ->
            val spanStart = text.getSpanStart(span)
            val spanEnd = text.getSpanEnd(span)
            val spanText = text.subSequence(spanStart, spanEnd)
            val startLine = layout.getLineForOffset(spanStart)
            val endLine = layout.getLineForOffset(spanEnd)
            val startOffset =
                (layout.getPrimaryHorizontal(spanStart) + -1 * layout.getParagraphDirection(startLine)).toInt()
            val endOffset =
                (layout.getPrimaryHorizontal(spanEnd) + layout.getParagraphDirection(endLine)).toInt()
            val spanInfo = SpanInfo(
                span, spanText, spanStart, spanEnd,
                startLine, endLine, startOffset, endOffset,
                startLine != endLine
            )
            //注入start-end范围的其他样式
            val otherSpans = text.getSpans<CharacterStyle>(spanStart, spanEnd)
            otherSpans.forEach { it.updateDrawState(workPaint) }
            onDraw(canvas, workPaint, layout, spanInfo)
        }
    }

    private fun onDraw(canvas: Canvas, paint: TextPaint, layout: Layout, spanInfo: SpanInfo<Span>) {
        val paragraphDir = layout.getParagraphDirection(spanInfo.startLine)
        val lineEndOffset = if (paragraphDir == Layout.DIR_RIGHT_TO_LEFT) {
            layout.getLineLeft(spanInfo.startLine)
        } else {
            layout.getLineRight(spanInfo.startLine)
        }.toInt()
        val lineStartOffset = if (paragraphDir == Layout.DIR_RIGHT_TO_LEFT) {
            layout.getLineRight(spanInfo.startLine)
        } else {
            layout.getLineLeft(spanInfo.startLine)
        }.toInt()
        var lineBottom: Int
        var lineTop: Int
        var baseline: Int
        val isMultiLine = spanInfo.startLine != spanInfo.endLine
        if (isMultiLine) {
            for (lineIndex in spanInfo.startLine..spanInfo.endLine) {
                lineBottom = getLineBottom(layout, lineIndex)
                lineTop = getLineTop(layout, lineIndex)
                baseline = layout.getLineBaseline(lineIndex)
                when (lineIndex) {
                    spanInfo.startLine -> rect.set(spanInfo.startOffset, lineTop, lineEndOffset, lineBottom)
                    spanInfo.endLine -> rect.set(lineStartOffset, lineTop, spanInfo.endOffset, lineBottom)
                    else -> {
                        rect.set(
                            (layout.getLineLeft(lineIndex).toInt()),
                            lineTop,
                            (layout.getLineRight(lineIndex).toInt()),
                            lineBottom
                        )
                    }
                }
                onDraw(canvas, paint, layout, spanInfo, lineIndex, baseline, rect)
                rect.setEmpty()
            }
        } else {
            lineTop = getLineTop(layout, spanInfo.startLine)
            lineBottom = getLineBottom(layout, spanInfo.startLine)
            baseline = layout.getLineBaseline(spanInfo.startLine)
            val left = min(spanInfo.startOffset, spanInfo.endOffset)
            val right = max(spanInfo.startOffset, spanInfo.endOffset)
            rect.set(left, lineTop, right, lineBottom)
            onDraw(canvas, paint, layout, spanInfo, spanInfo.startLine, baseline, rect)
            rect.setEmpty()
        }
    }

    /**
     * @param canvas
     * @param layout
     * @param spanInfo 当前Span详情
     * @param lineIndex 当前绘制行
     * @param baseline 当前行baseline在整个TextView的位置
     * @param drawRect 可以绘制的区域
     */
    protected abstract fun onDraw(
        canvas: Canvas, paint: TextPaint,
        layout: Layout, spanInfo: SpanInfo<Span>,
        lineIndex: Int, baseline: Int, drawRect: Rect
    )

    protected fun getLineTop(layout: Layout, line: Int): Int {
        return layout.getLineTopWithoutPadding(line)/* - span.verticalPadding*/
    }

    protected fun getLineBottom(layout: Layout, line: Int): Int {
        return layout.getLineBottomWithoutPadding(line)/* + span.verticalPadding*/
    }
}