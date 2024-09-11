package chenchen.engine.span.spanImpl

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.widget.TextView
import chenchen.engine.span.span.Constraint
import chenchen.engine.span.span.SpaceSpan
import chenchen.engine.span.span.Spacing

/**
 * @author: chenchen
 * @since: 2024/9/4 16:32
 */
class DrawableSpan(
    target: TextView,
    val drawable: Drawable,
    spacing: Spacing = Spacing(),
    constraint: Constraint = Constraint.CenterToTextCenter
) : SpaceSpan(target, spacing, constraint) {

    override fun draw(canvas: Canvas, text: CharSequence, start: Int, end: Int, paint: Paint, baseline: Float) {
        drawable.draw(canvas)
    }

    override fun provideContentWidth(text: CharSequence, start: Int, end: Int): Int {
        return drawable.bounds.width()
    }

    override fun provideContentHeight(paint: Paint): Int {
        return drawable.bounds.height()
    }
}