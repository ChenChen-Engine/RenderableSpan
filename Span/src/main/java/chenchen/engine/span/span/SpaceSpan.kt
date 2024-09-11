package chenchen.engine.span.span

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.text.Spanned
import android.text.method.BaseMovementMethod
import android.text.style.CharacterStyle
import android.text.style.ReplacementSpan
import android.widget.TextView
import androidx.core.graphics.withClip
import androidx.core.graphics.withTranslation
import chenchen.engine.span.renderer.getLineEndOrUnknown
import chenchen.engine.span.renderer.getLineForOffsetOrUnknown
import chenchen.engine.span.renderer.getLineStartOrUnknown
import chenchen.engine.span.span.Constraint.*

/**
 * 在文字中申请一段空间，可在这个空间中自由绘制。
 * 缺点：
 * 1. 最大只能占据一行，超过一行的宽度会超出TextView，显示异常
 * 2. 不能折叠换行，只能一行显示完，要么在当前行显示完，要么换一行显示完，无法当绘制到行尾的时候换一行继续绘制
 * 优点：
 * 可在一行的宽度内，任意指定宽度和高度绘制任意内容
 *
 * PS:
 * 需要支持光标跳过整个Span，需要重写[BaseMovementMethod.left]和[BaseMovementMethod.right]
 * ```
 * class CustomMovementMethod : BaseMovementMethod{
 *
 *   override fun left(TextView widget, Spannable buffer) :Boolean {
 *     return Selection.moveLeft(buffer, widget.getLayout())
 *   }
 *
 *   override fun right(TextView widget, Spannable buffer) :Boolean {
 *     return Selection.moveRight(buffer, widget.getLayout())
 *   }
 * }
 * ```
 *
 * @param target [SpaceSpan]需要作用到的[TextView]
 * @param spacing [SpaceSpan]与[SpaceSpan]的间距，而非与文字的间距
 * @param constraint 对齐方式
 * @author: chenchen
 * @since: 2024/9/4 15:04
 */
abstract class SpaceSpan(
    val target: TextView,
    val spacing: Spacing = Spacing(),
    val constraint: Constraint = CenterToTextCenter
) : ReplacementSpan() {

    /**
     * 自适应左边间距
     */
    private var adaptiveLeftMargin = 0

    /**
     * 自适应右边间距
     */
    private var adaptiveRightMargin = 0

    /**
     * 自适应顶部间距
     */
    private var adaptiveTopMargin = 0

    /**
     * 自适应底部间距
     */
    private var adaptiveBottomMargin = 0

    /**
     * 可绘制的区域
     */
    private var drawRectF = RectF()

    /**
     * 提供要绘制的宽
     */
    protected abstract fun provideContentWidth(text: CharSequence, start: Int, end: Int): Int

    /**
     * 提供要绘制的宽
     */
    protected abstract fun provideContentHeight(paint: Paint): Int

    /**
     * 提供baseline的位置，以[provideContentHeight]为高度，从0开始算
     */
    protected open fun provideBaseline(): Int {
        throw NotImplementedError("需要实现这个方法")
    }

    /**
     * 重写需要绘制的内容
     * 这里提供一个技巧：
     * 如果想便捷绘制，自己构造View，然后View.toBitmap()出来再画到画布上
     * 如果追求性能，全部都需要自己绘制。
     * @param canvas 画布，已经偏移好正确的位置Rect(0, 0, width, height)，按照自己提供的width和height绘制即可
     * @param text 整段文本
     * @param start 当前span的起始位置
     * @param end 当前span的结束位置
     * @param paint Span的画笔
     * @param baseline 当前行的基准线，如果需要与正常文本对齐可以使用，否则自己基于新的paint和height计算基准线
     */
    abstract fun draw(canvas: Canvas, text: CharSequence, start: Int, end: Int, paint: Paint, baseline: Float)

    override fun getSize(paint: Paint, text: CharSequence, start: Int, end: Int, fm: Paint.FontMetricsInt?): Int {
        measureHeight(text, start, end, paint, fm)
        return measureWidth(text, start, end)
    }

    final override fun draw(canvas: Canvas, text: CharSequence, start: Int, end: Int, x: Float, top: Int, baseline: Int, bottom: Int, paint: Paint) {
        val y = top + drawOffsetY(paint, top, baseline, bottom).toFloat()
        drawRectF.set(
            x + adaptiveLeftMargin, y,
            x + adaptiveLeftMargin + provideContentWidth(text, start, end),
            y + provideContentHeight(paint)
        )
        canvas.withClip(drawRectF) {
            canvas.withTranslation(drawRectF.left, drawRectF.top) {
                //这个paint最好不要直接使用，被污染过，另构造一个新的使用
                draw(canvas, text, start, end, Paint(), baseline.toFloat() - y)
            }
        }
    }

    /**
     * 测量整体宽度(内容区宽度)+(左间距)+(右间距)
     */
    protected open fun measureWidth(text: CharSequence, start: Int, end: Int): Int {
        val spanned = text as? Spanned
        val startLine = target.layout.getLineForOffsetOrUnknown(start)
        val endLine = target.layout.getLineForOffsetOrUnknown(end)
        adaptiveLeftMargin = measureAdaptiveLeftMargin(spanned, startLine, start)
        adaptiveRightMargin = measureAdaptiveRightMargin(spanned, endLine, end)
        return provideContentWidth(text, start, end) + adaptiveLeftMargin + adaptiveRightMargin
    }

    /**
     * 测量高度
     */
    protected open fun measureHeight(text: CharSequence, start: Int, end: Int, paint: Paint, workFontMetrics: Paint.FontMetricsInt?): Int {
        val spanned = text as? Spanned
        val startLine = target.layout.getLineForOffsetOrUnknown(start)
        val endLine = target.layout.getLineForOffsetOrUnknown(end)
        val contentHeight = provideContentHeight(paint)
        val fontMetrics = paint.fontMetricsInt
        adaptiveTopMargin = measureAdaptiveTopMargin(spanned, startLine)
        adaptiveBottomMargin = measureAdaptiveBottomMargin(spanned, endLine)
        if (workFontMetrics != null) {
            val lineTop = fontMetrics.top
            val lineAscent = fontMetrics.ascent
            val lineTextCenter = (fontMetrics.ascent + fontMetrics.descent) / 2
            val lineCenter = (fontMetrics.top + fontMetrics.bottom) / 2
            val lineBaseline = fontMetrics.bottom - fontMetrics.descent
            val lineDescent = fontMetrics.descent
            val lineBottom = fontMetrics.bottom
            //frontMetrics可以用来确定一行所在的位置以及高度
            //只要把top的位置确定，bottom就是contentHeight，至于ascent和descent不重要，但为了不出现意外，同步为top和bottom
            workFontMetrics.top = when (constraint) {
                TopToTop -> lineTop
                TopToAscent -> lineAscent
                TopToLineCenter -> lineCenter
                TopToTextCenter -> lineTextCenter
                TopToBaseline -> lineBaseline
                TopToDescent -> lineDescent
                TopToBottom -> lineBottom
                CenterToTop -> lineTop - contentHeight / 2
                CenterToAscent -> lineAscent - contentHeight / 2
                CenterToLineCenter -> lineCenter - contentHeight / 2
                CenterToBaseline -> lineBaseline - contentHeight / 2
                CenterToDescent -> lineDescent - contentHeight / 2
                CenterToBottom -> lineBottom - contentHeight / 2
                CenterToTextCenter -> lineTextCenter - contentHeight / 2
                BaselineToTextCenter -> lineTextCenter - provideBaseline()
                BaselineToBaseline -> lineBaseline - provideBaseline()
                BottomToTop -> lineTop - contentHeight
                BottomToAscent -> lineAscent - contentHeight
                BottomToLineCenter -> lineCenter - contentHeight
                BottomToTextCenter -> lineTextCenter - contentHeight
                BottomToBaseline -> lineBaseline - contentHeight
                BottomToDescent -> lineDescent - contentHeight
                BottomToBottom -> lineBottom - contentHeight
            }
            workFontMetrics.top -= adaptiveTopMargin
            workFontMetrics.bottom = workFontMetrics.top + contentHeight + adaptiveBottomMargin
            workFontMetrics.ascent = workFontMetrics.top
            workFontMetrics.descent = workFontMetrics.bottom
        }
        return (workFontMetrics?.bottom ?: fontMetrics.bottom) -
                (workFontMetrics?.top ?: fontMetrics.top)
    }

    /**
     * 在top的基础上偏移
     */
    private fun drawOffsetY(paint: Paint, top: Int, baseline: Int, bottom: Int): Int {
        val fontMetricsInt = paint.fontMetricsInt
        val contentHeight = provideContentHeight(paint)
        val lineAscent = baseline - top + fontMetricsInt.ascent
        val lineDescent = baseline - top + fontMetricsInt.descent
        val lineTop = 0
        val lineCenter = (bottom - top) / 2
        val lineTextCenter = (lineAscent + lineDescent) / 2
        val lineBaseline = baseline - top
        val lineBottom = bottom - top
        return when (constraint) {
            TopToTop -> lineTop
            TopToAscent -> lineAscent
            TopToLineCenter -> lineCenter
            TopToTextCenter -> lineTextCenter
            TopToBaseline -> lineBaseline
            TopToDescent -> lineDescent
            TopToBottom -> lineBottom
            CenterToTop -> lineTop - contentHeight / 2
            CenterToAscent -> lineAscent - contentHeight / 2
            CenterToLineCenter -> lineCenter - contentHeight / 2
            CenterToTextCenter -> lineTextCenter - contentHeight / 2
            CenterToBaseline -> lineBaseline - contentHeight / 2
            CenterToDescent -> lineDescent - contentHeight / 2
            CenterToBottom -> lineBottom - contentHeight / 2
            BaselineToTextCenter -> lineTextCenter - provideBaseline()
            BaselineToBaseline -> lineBaseline - provideBaseline()
            BottomToTop -> lineTop - contentHeight
            BottomToAscent -> lineAscent - contentHeight
            BottomToLineCenter -> lineCenter - contentHeight
            BottomToTextCenter -> lineTextCenter - contentHeight
            BottomToBaseline -> lineBaseline - contentHeight
            BottomToDescent -> lineDescent - contentHeight
            BottomToBottom -> lineBottom - contentHeight
        }
    }

    /**
     * 测量自适应的左边间距
     */
    private fun measureAdaptiveLeftMargin(spanned: Spanned?, line: Int, start: Int): Int {
        return if (start == 0 || start == target.layout.getLineStartOrUnknown(line)) {
            spacing.left
        } else {
            if (start - 1 > 0) {
                //上一个如果是SpaceSpan就间距/2
                val previousSpan = getSpan(spanned, start - 1, start)
                if ((previousSpan?.spacing?.right ?: 0) > 0) {
                    spacing.left / 2
                } else {
                    spacing.left
                }
            } else {
                spacing.left
            }
        }
    }

    /**
     * 测量自适应的右边间距
     */
    private fun measureAdaptiveRightMargin(spanned: Spanned?, line: Int, end: Int): Int {
        return if (end == 0 || end == target.layout.getLineEndOrUnknown(line)) {
            spacing.right
        } else {
            if (end + 1 < (spanned?.length ?: 0)) {
                //下一个如果是SpaceSpan就间距/2
                val nextSpan = getSpan(spanned, end, end + 1)
                if ((nextSpan?.spacing?.left ?: 0) > 0) {
                    spacing.right / 2
                } else {
                    spacing.right
                }
            } else {
                spacing.right
            }
        }
    }

    /**
     * 测量自适应的顶部间距
     */
    private fun measureAdaptiveTopMargin(spanned: Spanned?, line: Int): Int {
        return if (line == 0) {
            spacing.top
        } else {
            if (line - 1 > 0) {
                val preLineStart = target.layout.getLineStartOrUnknown(line - 1)
                val preLineEnd = target.layout.getLineEndOrUnknown(line - 1)
                val topSpan = getSpan(spanned, preLineStart, preLineEnd)
                if ((topSpan?.spacing?.bottom ?: 0) > 0) {
                    spacing.top / 2
                } else {
                    spacing.top
                }
            } else {
                spacing.top
            }
        }
    }

    /**
     * 测量自适应的底部间距
     */
    private fun measureAdaptiveBottomMargin(spanned: Spanned?, line: Int): Int {
        return if (line == target.lineCount - 1) {
            spacing.bottom
        } else {
            if (line + 1 < target.lineCount - 1) {
                val nextLineStart = target.layout.getLineStartOrUnknown(line + 1)
                val nextLineEnd = target.layout.getLineEndOrUnknown(line + 1)
                val bottomSpan = getSpan(spanned, nextLineStart, nextLineEnd)
                if ((bottomSpan?.spacing?.top ?: 0) > 0) {
                    spacing.bottom / 2
                } else {
                    spacing.bottom
                }
            } else {
                spacing.bottom
            }
        }
    }

    private fun getSpan(spanned: Spanned?, start: Int, end: Int): SpaceSpan? {
        val spans = spanned?.getSpans(start, end, CharacterStyle::class.java)
        return spans?.firstOrNull { it is SpaceSpan } as? SpaceSpan
    }

    private fun debugDrawFontMetricsLines(canvas: Canvas, top: Int, bottom: Int, baseline: Int, paint: Paint) {
        val lineTop = top
        paint.color = Color.BLACK
        canvas.drawLine(0f, lineTop.toFloat(), target.width.toFloat(), lineTop.toFloat(), paint)
        val lineAscent = baseline + paint.fontMetricsInt.ascent
        paint.color = Color.WHITE
        canvas.drawLine(0f, lineAscent.toFloat(), target.width.toFloat(), lineAscent.toFloat(), paint)
        val lineDescent = baseline + paint.fontMetricsInt.descent
        paint.color = Color.GREEN
        canvas.drawLine(0f, lineDescent.toFloat(), target.width.toFloat(), lineDescent.toFloat(), paint)
        val lineCenter = (top + bottom) / 2
        paint.color = Color.YELLOW
        canvas.drawLine(0f, lineCenter.toFloat(), target.width.toFloat(), lineCenter.toFloat(), paint)
        val lineTextCenter = (lineAscent + lineDescent) / 2
        paint.color = Color.RED
        canvas.drawLine(0f, lineTextCenter.toFloat(), target.width.toFloat(), lineTextCenter.toFloat(), paint)
        val lineBaseline = baseline
        paint.color = Color.BLUE
        canvas.drawLine(0f, lineBaseline.toFloat(), target.width.toFloat(), lineBaseline.toFloat(), paint)
        val lineBottom = bottom
        paint.color = Color.MAGENTA
        canvas.drawLine(0f, lineBottom.toFloat(), target.width.toFloat(), lineBottom.toFloat(), paint)
    }
}
