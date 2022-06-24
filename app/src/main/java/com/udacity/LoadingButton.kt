package com.udacity

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import androidx.core.content.withStyledAttributes
import kotlin.properties.Delegates.observable

class LoadingButton @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private var buttonText = ""
    private var widthSize = 0F
    private var heightSize = 0F
    private val buttonRoundness = 50F
    private var buttonRectangle: RectF
    private val textSize2 = 55F
    private val textBound = Rect()

    private var backgroundColorWhenIdle = 0
    private var textWhenIdle = ""
    private var textColorWhenIdle = 0

    private var backgroundColorWhenLoading = 0
    private var textWhenLoading = ""
    private var textColorWhenLoading = 0

    private val valueAnimator = ValueAnimator()

    private var buttonState: ButtonState by observable<ButtonState>(ButtonState.Completed) { p, old, new ->
        invalidate()
    }

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        textAlign = Paint.Align.CENTER
        textSize = textSize2
        typeface = Typeface.create("", Typeface.BOLD)
    }

    init {
        context.withStyledAttributes(attrs, R.styleable.LoadingButton) {
            backgroundColorWhenIdle = getColor(R.styleable.LoadingButton_buttonBackgroundColor, resources.getColor(R.color.colorPrimary))
            textWhenIdle = getString(R.styleable.LoadingButton_buttonText) ?: resources.getString(R.string.download)
            textColorWhenIdle = getColor(R.styleable.LoadingButton_buttonTextColor, resources.getColor(R.color.colorAccent))

            backgroundColorWhenLoading = getColor(R.styleable.LoadingButton_buttonLoadingBackgroundColor, resources.getColor(R.color.colorAccent))
            textWhenLoading = getString(R.styleable.LoadingButton_buttonLoadingText) ?: resources.getString(R.string.we_are_loading)
            textColorWhenLoading = getColor(R.styleable.LoadingButton_buttonLoadingTextColor, resources.getColor(R.color.colorPrimary))
        }

        buttonRectangle = RectF()
        buttonState = ButtonState.Completed
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        paint.color = backgroundColorWhenIdle
        buttonRectangle.set(0F, 0F, widthSize, heightSize)
        textBound.set(0, 0, width, height)
        canvas?.drawRoundRect(buttonRectangle, buttonRoundness, buttonRoundness, paint)

        // Button label
        val textBoundHeight = textBound.height()
        val textBoundWidth = textBound.width()
        paint.textAlign = Paint.Align.LEFT
        paint.getTextBounds(textWhenIdle, 0, textWhenIdle.length, textBound)
        paint.color = textColorWhenIdle

        val x: Float = textBoundWidth / 2f - textBound.width() / 2f - textBound.left
        val y: Float = textBoundHeight / 2f + textBound.height() / 2f - textBound.bottom

        canvas?.drawText(textWhenIdle, x, y, paint)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val minw: Int = paddingLeft + paddingRight + suggestedMinimumWidth
        val w: Int = resolveSizeAndState(minw, widthMeasureSpec, 1)
        val h: Int = resolveSizeAndState(
            MeasureSpec.getSize(w),
            heightMeasureSpec,
            0
        )

        widthSize = w.toFloat()
        heightSize = h.toFloat()
        setMeasuredDimension(w, h)
    }
}