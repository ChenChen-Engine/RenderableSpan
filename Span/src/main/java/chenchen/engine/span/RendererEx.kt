package chenchen.engine.span

import android.widget.TextView
import chenchen.engine.span.renderer.AbsSpanRenderer

/**
 * @author: chenchen
 * @since: 2024/9/3 18:04
 */

fun <T> T.addSpanForegroundRenderer(renderers: List<AbsSpanRenderer<*>>) where T : TextView, T : ViewDrawExtension {
    getSpanForegroundRenderer().addRenderer(renderers)
}

fun <T> T.addSpanBackgroundRenderer(renderers: List<AbsSpanRenderer<*>>) where T : TextView, T : ViewDrawExtension {
    getSpanBackgroundRenderer().addRenderer(renderers)
}

fun <T> T.getSpanForegroundRenderer(): SpanForegroundRendererImpl<T> where T : TextView, T : ViewDrawExtension {
    var renderer = (getTag(R.id.span_foreground_renderer) as? SpanForegroundRendererImpl<T>)
    if (renderer == null) {
        renderer = SpanForegroundRendererImpl(this)
        this.setTag(R.id.span_foreground_renderer, renderer)
    }
    return renderer
}

fun <T> T.getSpanBackgroundRenderer(): SpanBackgroundRendererImpl<T> where T : TextView, T : ViewDrawExtension {
    var renderer = (getTag(R.id.span_background_renderer) as? SpanBackgroundRendererImpl<T>)
    if (renderer == null) {
        renderer = SpanBackgroundRendererImpl(this)
        this.setTag(R.id.span_background_renderer, renderer)
    }
    return renderer
}