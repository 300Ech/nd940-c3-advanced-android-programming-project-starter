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

    private var progress: Double = 0.0

    private var buttonText = ""
    private var widthSize = 0F
    private var heightSize = 0F
    private val buttonRoundness = 50F
    private var buttonRectangle = RectF(0f, 0f, 0f, 0f)
    private var mRadarRect = RectF(0f, 0f, 0f, 0f)

    private val textSize2 = 55F
    private val textBound = Rect()

    private var backgroundColorWhenIdle = 0
    private var textWhenIdle = ""
    private var textColorWhenIdle = 0

    private var backgroundColorWhenLoading = 0
    private var textWhenLoading = ""
    private var textColorWhenLoading = 0

    private var valueAnimator = ValueAnimator.ofInt(0, 360)

    private var buttonState: ButtonState by observable<ButtonState>(ButtonState.Completed) { p, old, new ->
        when(buttonState) {
            ButtonState.Loading -> {
                isActivated  = false
                isClickable = false
                buttonText = textWhenLoading
            }
            ButtonState.Completed -> {
                buttonText = textWhenIdle
                isActivated = true
                isClickable = true
            }
            ButtonState.Clicked -> {

            }
        }

        invalidate()
    }

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        textAlign = Paint.Align.CENTER
        textSize = textSize2
        typeface = Typeface.create("", Typeface.BOLD)
    }

    init {
        isClickable = true

        context.withStyledAttributes(attrs, R.styleable.LoadingButton) {
            backgroundColorWhenIdle = getColor(R.styleable.LoadingButton_buttonBackgroundColor, resources.getColor(R.color.colorPrimary))
            textWhenIdle = getString(R.styleable.LoadingButton_buttonText) ?: resources.getString(R.string.download)
            textColorWhenIdle = getColor(R.styleable.LoadingButton_buttonTextColor, resources.getColor(R.color.colorAccent))

            backgroundColorWhenLoading = getColor(R.styleable.LoadingButton_buttonLoadingBackgroundColor, resources.getColor(R.color.colorAccent))
            textWhenLoading = getString(R.styleable.LoadingButton_buttonLoadingText) ?: resources.getString(R.string.we_are_loading)
            textColorWhenLoading = getColor(R.styleable.LoadingButton_buttonLoadingTextColor, resources.getColor(R.color.colorPrimary))
        }

        valueAnimator.apply {
            duration = 2500
            addUpdateListener {
                progress = it.animatedValue.toString().toDouble()
                invalidate()
            }
            repeatCount = ValueAnimator.INFINITE
            repeatMode = ValueAnimator.RESTART
        }

        buttonState = ButtonState.Completed
    }

    override fun performClick(): Boolean {
        super.performClick()

        // starts animation
        buttonState = ButtonState.Loading
        valueAnimator.start()

        invalidate()
        return true
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        // Circle properties
        val diameter = 100f
        val radius = if (buttonState != ButtonState.Loading) 0f else diameter / 2

        // text properties
        paint.color = backgroundColorWhenIdle
        buttonRectangle.set(0F, 0F, widthSize, heightSize)
        textBound.set(0, 0, width, height)
        canvas?.drawRoundRect(buttonRectangle, buttonRoundness, buttonRoundness, paint)

        // button text
        val textBoundHeight = textBound.height()
        val textBoundWidth = textBound.width()
        paint.textAlign = Paint.Align.LEFT
        paint.getTextBounds(buttonText, 0, buttonText.length, textBound)
        paint.color = textColorWhenIdle

        val x: Float = (textBoundWidth / 2f - textBound.width() / 2f - textBound.left) - radius
        val y: Float = textBoundHeight / 2f + textBound.height() / 2f - textBound.bottom
        canvas?.drawText(buttonText, x, y, paint)

        // animated circle
        if (buttonState == ButtonState.Loading) {
            val circleTop = (heightSize / 2) - radius
            val circleBottom = (heightSize / 2) + radius
            val circleLeft = paint.measureText(buttonText) + x + 20f
            val circleRight = circleLeft + diameter

            mRadarRect.set(circleLeft, circleTop, circleRight, circleBottom)
            canvas?.drawArc(mRadarRect, 0f, progress.toFloat(), true, paint)
        }
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