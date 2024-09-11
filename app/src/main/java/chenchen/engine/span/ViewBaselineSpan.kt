package chenchen.engine.span

import android.view.View
import android.widget.TextView
import chenchen.engine.span.span.Constraint
import chenchen.engine.span.span.Spacing
import chenchen.engine.span.spanImpl.ViewSpan

/**
 * @author: chenchen
 * @since: 2024/9/6 10:04
 */
class ViewBaselineSpan(
    target: TextView,
    drawView: View,
    spacing: Spacing = Spacing(),
    constraint: Constraint = Constraint.CenterToTextCenter
) : ViewSpan(target, drawView, spacing, constraint) {
    override fun provideBaseline(): Int {
        val tvName = drawView.findViewById<TextView>(R.id.tvName)
        return (tvName.top + tvName.height / 2 + tvName.paint.baseline).toInt()
    }
}