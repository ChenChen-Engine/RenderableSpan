package chenchen.engine.span.span

import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.RadialGradient
import android.graphics.RectF
import android.graphics.Shader
import android.graphics.SweepGradient
import androidx.annotation.ColorInt
import kotlin.math.max

/**
 * @author: chenchen
 * @since: 2024/9/3 17:45
 */
sealed class Colors {
    data class Solid(@ColorInt val color: Int) : Colors()
    data class Gradient(
        val type: Type,
        val angle: Int,
        @ColorInt val startColor: Int,
        @ColorInt val endColor: Int,
        @ColorInt val centerColor: Int = Color.TRANSPARENT,
        @Suppress("ArrayInDataClass")
        val positions: FloatArray = floatArrayOf(0f, 0.5f, 1f)
    ) : Colors() {
        enum class Type { Linear, Sweep, Radial }

        companion object {
            fun Gradient.generate(rectF: RectF): Shader {
                val colors = intArrayOf(startColor, centerColor, endColor)
                val positions = positions
                return when (type) {
                    Type.Linear -> LinearGradient(
                        rectF.left, rectF.centerY(),
                        rectF.right, rectF.centerY(),
                        colors,
                        positions,
                        Shader.TileMode.CLAMP
                    )
                    Type.Radial -> RadialGradient(
                        rectF.centerX(),
                        rectF.centerY(),
                        max(rectF.centerX(), rectF.centerY()),
                        colors,
                        positions,
                        Shader.TileMode.CLAMP
                    )
                    Type.Sweep -> SweepGradient(
                        rectF.centerX(),
                        rectF.centerY(),
                        colors,
                        positions,
                    )
                }
            }
        }
    }
}