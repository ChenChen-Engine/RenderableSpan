package chenchen.engine.span.spanImpl

import android.graphics.Canvas
import android.graphics.Paint
import android.view.View
import android.widget.TextView
import androidx.core.view.ViewCompat
import chenchen.engine.span.span.Constraint
import chenchen.engine.span.span.SpaceSpan
import chenchen.engine.span.span.Spacing

/**
 * @param drawView 需要绘制的view
 * 建议使用LayoutInflate生成的View
 * ```
 * ```
 * @author: chenchen
 * @since: 2024/9/4 16:43
 */
open class ViewSpan(
    target: TextView,
    val drawView: View,
    spacing: Spacing = Spacing(),
    constraint: Constraint = Constraint.CenterToTextCenter
) : SpaceSpan(target, spacing, constraint) {
    init {
        assert(drawView.measuredWidth != 0 && drawView.measuredHeight != 0) {
            """
                请先将view测量完成
                ```
                drawView.measure(0,0)//自适应宽度，自适应高度
                //or
                drawView.measure(0, MeasureSpec.makeMeasureSpec(50.dp, MeasureSpec.EXACTLY))//自适应宽度，高度固定
                ```
            """.trimIndent()
        }
        assert(ViewCompat.isLaidOut(drawView)) {
            """
                请先将view布局完成
                ```
                drawView.layout(0, 0, drawView.measureWidth, drawView.measureHeight)
                ```
            """.trimIndent()
        }
    }

    override fun draw(canvas: Canvas, text: CharSequence, start: Int, end: Int, paint: Paint, baseline: Float) {
        drawView.draw(canvas)
    }

    override fun provideContentWidth(text: CharSequence, start: Int, end: Int): Int {
        return drawView.measuredWidth
    }

    override fun provideContentHeight(paint: Paint): Int {
        return drawView.measuredHeight
    }
}