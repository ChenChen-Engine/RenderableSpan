package chenchen.engine.span

import android.graphics.Canvas
import android.text.Spanned
import android.widget.TextView
import androidx.core.graphics.withClip
import androidx.core.graphics.withTranslation

/**
 * @author: chenchen
 * @since: 2024/9/3 18:26
 */

class SpanForegroundRendererImpl<T>(
    target: T
) : SpanRendererImpl<T>(target) where T : TextView, T : ViewDrawExtension {
    override fun onPostDraw(canvas: Canvas) {
        if (target.text is Spanned && target.layout != null) {
            canvas.withClip(
                target.scrollX + target.totalPaddingLeft,
                target.scrollY + target.totalPaddingTop,
                target.width + target.scrollX - target.totalPaddingRight,
                target.height + target.scrollY - target.totalPaddingBottom
            ) {
                canvas.withTranslation(target.totalPaddingLeft.toFloat(), target.totalPaddingTop.toFloat()) {
                    onEach { it.onDraw(canvas, target.paint, target.text as Spanned, target.layout) }
                }
            }
        }
    }

    override fun release() {
        super.release()
        target.setTag(R.id.span_foreground_renderer, null)
    }
}