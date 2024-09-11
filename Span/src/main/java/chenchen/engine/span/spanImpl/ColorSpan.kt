package chenchen.engine.span.spanImpl

import chenchen.engine.span.span.Colors
import chenchen.engine.span.span.Margins
import chenchen.engine.span.span.Radius
import chenchen.engine.span.span.RenderableSpan

/**
 * 纯色的Span
 * @param color 颜色
 * @param radius 圆角
 * @param margins 间距
 * @param isMatchText true绘制ascent到descent的范围，false绘制top到bottom的范围
 * 在正常文字排版下，这个参数true或false并无差异，在填充各种Span的情况下，差距明显
 * @author: chenchen
 * @since: 2024/9/3 17:21
 */
abstract class ColorSpan(
    val color: Colors,
    val radius: Radius = Radius(),
    val margins: Margins = Margins(),
    val isMatchText: Boolean = true,
) : RenderableSpan()

class BackgroundColorSpan(
    color: Colors,
    radius: Radius = Radius(),
    margins: Margins = Margins(),
    isMatchText: Boolean = true,
) : ColorSpan(color, radius, margins, isMatchText)

class ForegroundColorSpan(
    color: Colors,
    radius: Radius = Radius(),
    margins: Margins = Margins(),
    isMatchText: Boolean = true,
) : ColorSpan(color, radius, margins, isMatchText)