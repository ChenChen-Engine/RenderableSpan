package chenchen.engine.span.span

/**
 * @author: chenchen
 * @since: 2024/9/3 17:30
 */
data class Margins(
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
        fun vertical(margin: Int): Margins {
            return Margins(top = margin, bottom = margin)
        }

        fun horizontal(margin: Int): Margins {
            return Margins(left = margin, right = margin)
        }
    }
}