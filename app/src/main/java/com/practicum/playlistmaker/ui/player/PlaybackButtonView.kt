package com.practicum.playlistmaker.ui.player

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.appcompat.widget.AppCompatImageView
import com.practicum.playlistmaker.R

class PlaybackButtonView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
): AppCompatImageView(context, attrs, defStyleAttr) {

    enum class PlaybackState {
        PLAY, PAUSE
    }

    private var currentState: PlaybackState = PlaybackState.PLAY

    private var isEnabled = true

    private var playDrawable: Drawable? = null
    private var pauseDrawable: Drawable? = null

    private var imageRect = RectF(0f, 0f, 0f, 0f)
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)

    init {
        var obtain: TypedArray? = null;
        try {
            obtain = context.theme.obtainStyledAttributes(attrs, R.styleable.PlaybackButtonView, 0, 0)
                .apply{
                    playDrawable = getDrawable(R.styleable.PlaybackButtonView_playDrawable)
                    pauseDrawable = getDrawable(R.styleable.PlaybackButtonView_pauseDrawable)
                }
        }
        finally {
            obtain?.recycle()
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        imageRect = RectF(0f, 0f, measuredWidth.toFloat(), measuredHeight.toFloat())
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val bitmap = when (currentState) {
            PlaybackState.PLAY -> playDrawable
            PlaybackState.PAUSE -> pauseDrawable
        }

        bitmap?.let { drawable ->
            drawable.setBounds(0, 0, width, height)
            drawable.draw(canvas)
        }

//        bitmap?.let {
//            canvas.drawBitmap(bitmap.toBitmap(), null, imageRect, paint)
//        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (!isEnabled) {
            return false
        }
        return when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                true
            }
            MotionEvent.ACTION_UP -> {
                toggleState()
                performClick()
                true
            }
            else -> super.onTouchEvent(event)
        }
    }

    override fun performClick(): Boolean{
        super.performClick()
        return true
    }


    private fun toggleState() {
        if (!isEnabled) return

        currentState = when (currentState) {
            PlaybackState.PLAY -> PlaybackState.PAUSE
            PlaybackState.PAUSE -> PlaybackState.PLAY
        }

        invalidate()
    }


    fun setPlaybackState(isPlaying: Boolean) {
        if (!isEnabled) return
        val newState = if (isPlaying) PlaybackState.PAUSE else PlaybackState.PLAY

        if (currentState != newState) {
            currentState = newState
            invalidate()
        }
    }

    override fun setEnabled(enabled: Boolean) {
        if (isEnabled == enabled) return

        isEnabled = enabled
        super.setEnabled(enabled)

        isClickable = enabled
        isFocusable = enabled

        invalidate()
    }
}