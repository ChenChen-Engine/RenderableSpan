package chenchen.engine.span.spanImpl

import chenchen.engine.span.span.Align
import chenchen.engine.span.span.Colors
import chenchen.engine.span.span.Offsets
import chenchen.engine.span.span.Radius
import chenchen.engine.span.span.RenderableSpan
import chenchen.engine.span.span.Spacing

/**
 * 横线Span
 * @author: chenchen
 * @since: 2024/9/4 11:06
 */
abstract class LineSpan(
    val strokeWidth: Float,
    val strokeColor: Colors,
    val radius: Radius = Radius(),
    val offsets: Offsets = Offsets(),
    val spacing: Spacing = Spacing(),
    val align: Align = Align.Baseline,
) : RenderableSpan()

class BackgroundLineSpan(
    strokeWidth: Float,
    strokeColor: Colors,
    radius: Radius = Radius(),
    offsets: Offsets = Offsets(),
    spacing: Spacing = Spacing(),
    align: Align = Align.Baseline,
) : LineSpan(strokeWidth, strokeColor, radius, offsets, spacing, align)

class ForegroundLineSpan(
    strokeWidth: Float,
    strokeColor: Colors,
    radius: Radius = Radius(),
    offsets: Offsets = Offsets(),
    spacing: Spacing = Spacing(),
    align: Align = Align.Baseline,
) : LineSpan(strokeWidth, strokeColor, radius, offsets, spacing, align)