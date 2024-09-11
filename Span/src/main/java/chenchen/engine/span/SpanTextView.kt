package chenchen.engine.span

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import java.util.LinkedList

/**
 * @author: chenchen
 * @since: 2024/9/11 10:30
 */
open class SpanTextView : androidx.appcompat.widget.AppCompatTextView, ViewDrawExtension {

    private var listeners: LinkedList<OnViewDrawListener>? = null

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    override fun addOnViewDrawListener(listener: OnViewDrawListener) {
        listeners().add(listener)
    }

    override fun removeOnViewDrawListener(listener: OnViewDrawListener) {
        listeners().remove(listener)
    }

    override fun clearOnViewDrawListener() {
        listeners().clear()
    }

    override fun onDraw(canvas: Canvas) {
        listeners().forEach { it.onPreDraw(canvas) }
        super.onDraw(canvas)
        listeners().forEach { it.onPostDraw(canvas) }
    }

    private fun listeners(): LinkedList<OnViewDrawListener> {
        return listeners ?: LinkedList<OnViewDrawListener>().also { listeners = it }
    }
}