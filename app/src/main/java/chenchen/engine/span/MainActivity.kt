package chenchen.engine.span

import android.graphics.Color
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import chenchen.engine.span.databinding.ActivityMainBinding
import chenchen.engine.span.rendererImpl.ColorRenderer
import chenchen.engine.span.rendererImpl.LineSpanRenderer
import chenchen.engine.span.span.Align
import chenchen.engine.span.span.Colors
import chenchen.engine.span.span.Constraint
import chenchen.engine.span.spanImpl.BackgroundColorSpan
import chenchen.engine.span.spanImpl.BackgroundLineSpan
import chenchen.engine.span.spanImpl.ForegroundColorSpan
import chenchen.engine.span.spanImpl.ForegroundLineSpan
import chenchen.engine.span.spanImpl.ViewSpan

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val spanView = LayoutInflater.from(this).inflate(R.layout.view_span, null, false)
        spanView.measure(0, View.MeasureSpec.makeMeasureSpec(50.dp, View.MeasureSpec.EXACTLY))
        spanView.layout(0, 0, spanView.measuredWidth, spanView.measuredHeight)
        binding.tv.addSpanForegroundRenderer(arrayListOf(ColorRenderer(ForegroundColorSpan::class.java)))
        binding.tv.addSpanBackgroundRenderer(arrayListOf(
            ColorRenderer(BackgroundColorSpan::class.java),
            LineSpanRenderer(BackgroundLineSpan::class.java),
        ))
        val sb = SpannableStringBuilder()
        sb.append("秋，是一幅色彩斑斓的画卷。", ForegroundColorSpan(Colors.Solid(0x88Ff9933.toInt())), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        sb.append("金黄的麦田", BackgroundColorSpan(Colors.Solid(0x8899Ff33.toInt())), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        sb.append("秋，是一幅色", ForegroundLineSpan(10f, Colors.Solid(Color.BLACK), align = Align.TextCenter), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        sb.append("金")
        sb.append("1", ViewSpan(binding.tv, spanView, constraint = Constraint.TopToTop), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        sb.append("黄")
        sb.append("1", ViewSpan(binding.tv, spanView, constraint = Constraint.TopToAscent), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        sb.append("的")
        sb.append("1", ViewSpan(binding.tv, spanView, constraint = Constraint.TopToLineCenter), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        sb.append("麦")
        sb.append("1", ViewSpan(binding.tv, spanView, constraint = Constraint.TopToTextCenter), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        sb.append("田")
        sb.append("1", ViewSpan(binding.tv, spanView, constraint = Constraint.TopToBaseline), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        sb.append("火", ForegroundColorSpan(Colors.Solid(0x88Ff9933.toInt())), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        sb.append("1", ViewSpan(binding.tv, spanView, constraint = Constraint.TopToDescent), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        sb.append("1", ForegroundColorSpan(Colors.Solid(0x88Ff9933.toInt())), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        sb.append("1", ViewSpan(binding.tv, spanView, constraint = Constraint.TopToBottom), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        sb.append("1", ForegroundColorSpan(Colors.Solid(0x88Ff9933.toInt())), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        sb.append("1", ViewSpan(binding.tv, spanView, constraint = Constraint.CenterToTop), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        sb.append("1", ForegroundColorSpan(Colors.Solid(0x88Ff9933.toInt())), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        sb.append("1", ViewSpan(binding.tv, spanView, constraint = Constraint.CenterToAscent), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        sb.append("1", ForegroundColorSpan(Colors.Solid(0x88Ff9933.toInt())), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        sb.append("1", ViewSpan(binding.tv, spanView, constraint = Constraint.CenterToLineCenter), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        sb.append("1", ForegroundColorSpan(Colors.Solid(0x88Ff9933.toInt())), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        sb.append("1", ViewSpan(binding.tv, spanView, constraint = Constraint.CenterToTextCenter), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        sb.append("还")
        sb.append("1", ViewSpan(binding.tv, spanView, constraint = Constraint.CenterToBaseline), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        sb.append("有")
        sb.append("1", ViewSpan(binding.tv, spanView, constraint = Constraint.CenterToDescent), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        sb.append("那")
        sb.append("1", ViewSpan(binding.tv, spanView, constraint = Constraint.CenterToBottom), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        sb.append("一")
        sb.append("1", ViewBaselineSpan(binding.tv, spanView, constraint = Constraint.BaselineToTextCenter), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        sb.append("抹")
        sb.append("1", ViewBaselineSpan(binding.tv, spanView, constraint = Constraint.BaselineToBaseline), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        sb.append("抹")
        sb.append("1", ViewSpan(binding.tv, spanView, constraint = Constraint.BottomToTop), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        sb.append("淡")
        sb.append("1", ViewSpan(binding.tv, spanView, constraint = Constraint.BottomToAscent), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        sb.append("淡")
        sb.append("1", ViewSpan(binding.tv, spanView, constraint = Constraint.BottomToLineCenter), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        sb.append("的")
        sb.append("1", ViewSpan(binding.tv, spanView, constraint = Constraint.BottomToTextCenter), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        sb.append("忧")
        sb.append("1", ViewSpan(binding.tv, spanView, constraint = Constraint.BottomToBaseline), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        sb.append("郁")
        sb.append("1", ViewSpan(binding.tv, spanView, constraint = Constraint.BottomToDescent), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        sb.append("蓝")
        sb.append("1", ViewSpan(binding.tv, spanView, constraint = Constraint.BottomToBottom), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        sb.append(".")
        sb.append("\n")
        sb.append("\n")
        sb.append("\n")
        sb.append("\n")
        binding.tv.text = sb
    }
}