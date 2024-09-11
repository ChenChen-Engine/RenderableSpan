package chenchen.engine.span.span

/**
 * [LineCenter]和[TextCenter]正常情况下表现一致，
 * 但因各种不确定的文字排版，无法保证一致性，所以需要准确的设置依赖
 * @author: chenchen
 * @since: 2024/9/4 17:13
 */
enum class Constraint {
    TopToTop,
    TopToAscent,
    TopToLineCenter,
    TopToTextCenter,
    TopToBaseline,
    TopToBottom,
    TopToDescent,
    CenterToTop,
    CenterToAscent,
    CenterToLineCenter,
    CenterToTextCenter,
    CenterToBaseline,
    CenterToDescent,
    CenterToBottom,
    BaselineToTextCenter,
    BaselineToBaseline,
    BottomToTop,
    BottomToAscent,
    BottomToLineCenter,
    BottomToTextCenter,
    BottomToBaseline,
    BottomToDescent,
    BottomToBottom,
}