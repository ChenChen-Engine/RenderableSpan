package chenchen.engine.span.renderer

import android.text.style.MetricAffectingSpan

/**
 * @Author: chenchen
 * @CreateDate: 2022/12/23 10:51
 * @Description: Span详情
 * @param span span
 * @param spanText span所在的文本
 * @param spanStart span在整个View的文本中的起始位置
 * @param spanEnd span在整个View的文本中的结束位置
 * @param startLine span所在的起始行
 * @param endLine span所在的结束行
 * @param startOffset 应该是span所在的起始行的偏移量
 * @param endOffset 应该是span所在的结束行的偏移量
 * @param isMultiLine span是否多行展示
 */
data class SpanInfo<Span : MetricAffectingSpan>(
    val span: Span,
    val spanText: CharSequence,
    val spanStart: Int,
    val spanEnd: Int,
    val startLine: Int,
    val endLine: Int,
    val startOffset: Int,
    val endOffset: Int,
    val isMultiLine: Boolean
)