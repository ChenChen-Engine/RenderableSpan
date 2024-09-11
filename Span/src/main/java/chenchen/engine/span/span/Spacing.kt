package chenchen.engine.span.span

/**
 * @author: chenchen
 * @since: 2024/9/11 11:03
 */
data class Spacing(
    val left: Int = 0,
    val top: Int = 0,
    val right: Int = 0,
    val bottom: Int = 0,
) {

    fun left(left: Int) = copy(left = left)
    fun top(top: Int) = copy(top = top)
    fun right(right: Int) = copy(right = right)
    fun bottom(bottom: Int) = copy(bottom = bottom)

    fun vertical(margin: Int) = copy(top = margin, bottom = margin)

    fun horizontal(margin: Int) = copy(left = margin, right = margin)

    companion object {
        fun vertical(margin: Int): Spacing {
            return Spacing(top = margin, bottom = margin)
        }

        fun horizontal(margin: Int): Spacing {
            return Spacing(left = margin, right = margin)
        }
    }
}