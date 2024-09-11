package chenchen.engine.span

import android.graphics.Paint
import android.os.Build
import android.text.Layout
import android.text.StaticLayout
import android.text.TextPaint
import android.text.TextUtils
import android.widget.TextView
import kotlin.math.max

/**
 * Created by zzz on 2021/11/18
 */
/**
 * 行高设置，兼容低版本
 */
var TextView.lineHeightComponent: Int
    set(value) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            lineHeight = value
        } else {
            val fontHeight = paint.getFontMetricsInt(null)
            if (value != fontHeight) {
                setLineSpacing((value - fontHeight).toFloat(), 1f)
            }
        }
    }
    get() = lineHeight

/**
 * 基准线
 */
val TextView.baseline: Float
    get() = paint.baseline

/**
 * 基准线
 */
val Paint.baseline: Float
    get() = (fontMetrics.bottom - fontMetrics.top) / 2 - fontMetrics.bottom

/**
 * 根据TextView属性得到相应的layout
 */
fun TextView.getNewLayout(
    charSequence: CharSequence = text ?: "",
    start: Int = 0,
    end: Int = charSequence.length,
    textPaint: TextPaint = this.paint,
    width: Int = max(this.width - paddingLeft - paddingRight, 0),
    lineSpacingExtra: Float = this.lineSpacingExtra,
    lineSpacingMultiplier: Float = this.lineSpacingMultiplier,
    alignment: Layout.Alignment = Layout.Alignment.ALIGN_NORMAL,
    ellipsize: TextUtils.TruncateAt? = this.ellipsize,
    includePad: Boolean = true,
    ellipsizedWidth: Int = width,
    maxLines: Int = this.maxLines,
): Layout {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        StaticLayout.Builder.obtain(
            charSequence,
            start,
            end,
            textPaint,
            width
        ).setAlignment(alignment)
            .setLineSpacing(lineSpacingExtra, lineSpacingMultiplier)
            .setEllipsize(ellipsize)
            .setEllipsizedWidth(ellipsizedWidth)
            .setMaxLines(maxLines)
            .setIncludePad(includePad)
            .build()
    } else
        StaticLayout(
            charSequence,
            start,
            end,
            textPaint,
            width,
            Layout.Alignment.ALIGN_NORMAL,
            lineSpacingMultiplier,
            lineSpacingExtra,
            includePad,
            ellipsize,
            ellipsizedWidth
        ).apply {
            setMaxLines(maxLines)
        }
}

/**
 * 获取Hint的Layout
 */
fun TextView.getHintLayout(): Layout {
    val layout = this.layout
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        StaticLayout.Builder.obtain(this.hint, 0, this.hint.length, layout.paint, layout.width)
            .setAlignment(layout.alignment)
            .setEllipsize(this.ellipsize)
            .setBreakStrategy(this.breakStrategy)
            .setEllipsizedWidth(layout.ellipsizedWidth)
            .setMaxLines(this.maxLines)
            .setHyphenationFrequency(this.hyphenationFrequency)
            .setIncludePad(this.includeFontPadding)
            .setLineSpacing(this.lineSpacingExtra, this.lineSpacingMultiplier)
//            .setJustificationMode(this.justificationMode)
//            .setTextDirection(this.textDirectionHeuristic)
//            .setUseLineSpacingFromFallbacks()
//            .setIndents()
            .build()
    } else {
        StaticLayout(this.hint, 0, this.hint.length, layout.paint,
            layout.width, layout.alignment, this.lineSpacingExtra,
            this.lineSpacingMultiplier, this.includeFontPadding, this.ellipsize, layout.ellipsizedWidth)
    }
}

/**
 * 构建Layout
 */
fun buildLayout(
    text: CharSequence,
    textPaint: TextPaint,
    width: Int,
    maxLines: Int,
    lineSpacingExtra: Float,
    lineSpacingMultiplier: Float,
    alignment: Layout.Alignment = Layout.Alignment.ALIGN_NORMAL,
    includePad: Boolean = true,
    start: Int = 0,
    end: Int = text.length,
    ellipsize: TextUtils.TruncateAt? = null,
    ellipsizedWidth: Int = width,
): StaticLayout {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        StaticLayout.Builder.obtain(
            text,
            start,
            end,
            textPaint,
            width
        ).setAlignment(alignment)
            .setLineSpacing(lineSpacingExtra, lineSpacingMultiplier)
            .setEllipsize(ellipsize)
            .setEllipsizedWidth(ellipsizedWidth)
            .setMaxLines(maxLines)
            .setIncludePad(includePad)
            .build()
    } else
        StaticLayout(
            text,
            start,
            end,
            textPaint,
            width,
            alignment,
            lineSpacingMultiplier,
            lineSpacingExtra,
            includePad,
            ellipsize,
            ellipsizedWidth
        )
}