package chenchen.engine.span

import android.widget.TextView
import androidx.annotation.CallSuper
import chenchen.engine.span.renderer.AbsSpanRenderer

/**
 * 前景渲染器
 * @author: chenchen
 * @since: 2024/9/3 18:07
 */
abstract class SpanRendererImpl<T>(
    protected val target: T
) : OnViewDrawListener where T : TextView, T : ViewDrawExtension {

    private val renderers = arrayListOf<AbsSpanRenderer<*>>()

    init {
        target.addOnViewDrawListener(this)
    }

    fun addRenderer(renderers: List<AbsSpanRenderer<*>>) {
        this.renderers.addAll(renderers)
    }

    fun removeRenderer(renderer: AbsSpanRenderer<*>) {
        renderers.remove(renderer)
    }

    @CallSuper
    open fun release() {
        target.removeOnViewDrawListener(this)
        renderers.clear()
    }

    protected fun onEach(action: (AbsSpanRenderer<*>) -> Unit) {
        renderers.onEach(action)
    }
}