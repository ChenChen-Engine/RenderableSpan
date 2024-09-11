package chenchen.engine.span

import android.graphics.Canvas

/**
 * @author: chenchen
 * @since: 2024/9/11 14:55
 */
interface ViewDrawExtension {
    fun addOnViewDrawListener(listener: OnViewDrawListener)
    fun removeOnViewDrawListener(listener: OnViewDrawListener)
    fun clearOnViewDrawListener()
}


interface OnViewDrawListener {
    /**
     * 在super.onDraw之前
     */
    open fun onPreDraw(canvas: Canvas) = Unit

    /**
     * 在super.onDraw之后
     */
    open fun onPostDraw(canvas: Canvas) = Unit
}