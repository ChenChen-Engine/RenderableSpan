package chenchen.engine.span.spanImpl

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.widget.TextView
import chenchen.engine.span.span.Constraint
import chenchen.engine.span.span.SpaceSpan
import chenchen.engine.span.span.Spacing

/**
 * @param bitmap 需要绘制的Bitmap
 * @author: chenchen
 * @since: 2024/9/10 15:30
 */
class BitmapSpan(
    target: TextView,
    val bitmap: Bitmap,
    spacing: Spacing = Spacing(),
    constraint: Constraint = Constraint.CenterToTextCenter
) : SpaceSpan(target, spacing, constraint) {
    override fun draw(canvas: Canvas, text: CharSequence, start: Int, end: Int, paint: Paint, baseline: Float) {
        canvas.drawBitmap(bitmap, 0f, 0f, paint)
    }

    override fun provideContentWidth(text: CharSequence, start: Int, end: Int): Int {
        return bitmap.width
    }

    override fun provideContentHeight(paint: Paint): Int {
        return bitmap.height
    }
}