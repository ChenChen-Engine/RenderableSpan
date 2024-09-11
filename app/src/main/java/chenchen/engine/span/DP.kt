package chenchen.engine.span

/**
 * @author: chenchen
 * @since: 2023/12/25 16:49
 */
import android.content.res.Resources
import android.util.TypedValue

/**
 * dp 转 px（返回 float 型）
 */
val Number.dpf: Float
    get() = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        this.toFloat(),
        Resources.getSystem().displayMetrics
    ).let { (if (it >= 0) it + 0.5f else it - 0.5f) }

/**
 * dp 转 px（返回 int 型）
 */
val Number.dp: Int
    get() = this.dpf.toInt()

/**
 * px转dp
 */
val Int.px2dp: Float
    get() = this / Resources.getSystem().displayMetrics.density
