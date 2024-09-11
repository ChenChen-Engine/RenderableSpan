package chenchen.engine.span.span

import android.text.TextPaint
import android.text.style.MetricAffectingSpan

open class RenderableSpan : MetricAffectingSpan() {
    final override fun updateDrawState(tp: TextPaint) {}

    final override fun updateMeasureState(textPaint: TextPaint) {}
}