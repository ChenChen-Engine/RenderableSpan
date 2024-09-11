package chenchen.engine.span.span

/**
 * @author: chenchen
 * @since: 2024/9/3 17:24
 */
data class Radius(
    val leftTop: Float = 0f,
    val rightTop: Float = 0f,
    val leftBottom: Float = 0f,
    val rightBottom: Float = 0f
) {

    fun fill(radius: FloatArray): FloatArray {
        radius[0] = leftTop
        radius[1] = leftTop
        radius[2] = rightTop
        radius[3] = rightTop
        radius[4] = leftBottom
        radius[5] = leftBottom
        radius[6] = rightBottom
        radius[7] = rightBottom
        return radius
    }

    companion object {
        fun all(radius: Float): Radius {
            return Radius(radius, radius, radius, radius)
        }

        fun left(radius: Float): Radius {
            return Radius(leftTop = radius, leftBottom = radius)
        }

        fun top(radius: Float): Radius {
            return Radius(leftTop = radius, rightTop = radius)
        }

        fun right(radius: Float): Radius {
            return Radius(rightTop = radius, rightBottom = radius)
        }

        fun bottom(radius: Float): Radius {
            return Radius(leftBottom = radius, rightBottom = radius)
        }
    }
}