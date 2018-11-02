package com.thecoolguy.rumaan.fileio.utils

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.os.Build
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import com.thecoolguy.rumaan.fileio.R

object MaterialIn {

    private const val MATERIAL_IN_BLOCK = "materialInBlock"
    private const val MATERIAL_IN_BLOCK_WITHOUT_SLIDE = "materialInNoSlide"

    @JvmOverloads
    fun animate(view: View?, delayDirection: Int = Gravity.BOTTOM, slideDirection: Int = Gravity.BOTTOM) {
        if (view != null) {
            view.viewTreeObserver.addOnPreDrawListener(object : ViewTreeObserver.OnPreDrawListener {
                override fun onPreDraw(): Boolean {
                    view.viewTreeObserver.removeOnPreDrawListener(this)
                    initAnimation(view, 0, 0, convertGravity(view, delayDirection),
                            convertGravity(view, slideDirection))
                    return true
                }
            })
            view.invalidate()
        }
    }

    private fun convertGravity(view: View?, gravity: Int): Int {
        var gravity = gravity
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            val isRtl = view!!.layoutDirection == View.LAYOUT_DIRECTION_RTL
            if (gravity == Gravity.START) {
                gravity = if (isRtl) Gravity.RIGHT else Gravity.LEFT
            } else if (gravity == Gravity.END) {
                gravity = if (isRtl) Gravity.LEFT else Gravity.RIGHT
            }
        }
        return gravity
    }

    private fun initAnimation(view: View?, offsetX: Int, offsetY: Int, delayDir: Int,
                              slideDir: Int) {
        var offsetX = offsetX
        var offsetY = offsetY
        if (offsetX < 0) {
            offsetX = 0
        }
        if (offsetY < 0) {
            offsetY = 0
        }
        if (view is ViewGroup && view.childCount > 0 &&
                MATERIAL_IN_BLOCK != view.tag &&
                MATERIAL_IN_BLOCK_WITHOUT_SLIDE != view.tag) {
            val viewGroup = view as ViewGroup?
            val viewHeight = viewGroup!!.height
            for (i in 0 until viewGroup.childCount) {
                val child = viewGroup.getChildAt(i)
                val nextOffsetX = offsetX + if (delayDir == Gravity.RIGHT)
                    child.left
                else
                    if (delayDir == Gravity.LEFT) viewHeight - child.right else 0
                val nextOffsetY = offsetY + if (delayDir == Gravity.BOTTOM)
                    child.top
                else
                    if (delayDir == Gravity.TOP) viewHeight - child.bottom else 0
                initAnimation(child, nextOffsetX, nextOffsetY, delayDir, slideDir)
            }
        } else {
            val res = view!!.resources
            var slideTranslation = res.getDimensionPixelSize(R.dimen.material_in_anim_slide_offset)
            if (MATERIAL_IN_BLOCK_WITHOUT_SLIDE == view.tag) {
                slideTranslation = 0
            }
            var multY = 0
            if (slideDir == Gravity.TOP) {
                multY = 1
            } else if (slideDir == Gravity.BOTTOM) {
                multY = -1
            }
            var multX = 0
            if (slideDir == Gravity.LEFT) {
                multX = 1
            } else if (slideDir == Gravity.RIGHT) {
                multX = -1
            }
            val delayOffset = if (delayDir == Gravity.TOP || delayDir == Gravity.BOTTOM) offsetY else offsetX
            val delayDenominator = res.getDimension(R.dimen.material_in_delay_denominator)
            val delay = (delayOffset / delayDenominator).toLong()
            startAnimators(view, slideTranslation * multX, slideTranslation * multY, delay)
        }
    }

    fun startAnimators(view: View, startOffsetX: Int, startOffsetY: Int,
                       delay: Long) {
        if (view.visibility == View.VISIBLE && view.alpha != 0f) {
            view.clearAnimation()
            view.animate().cancel()
            val res = view.resources
            val endAlpha = view.alpha
            val endTranslateX = view.translationX
            val endTranslateY = view.translationY
            view.alpha = 0f
            val fade = ObjectAnimator.ofFloat(view, View.ALPHA, endAlpha)
            fade.duration = res.getInteger(R.integer.material_in_fade_anim_duration).toLong()
            fade.interpolator = AccelerateInterpolator()
            fade.startDelay = delay
            fade.start()
            val slide = view.animate()
            if (startOffsetY != 0) {
                view.translationY = startOffsetY.toFloat()
                slide.translationY(endTranslateY)
            } else {
                view.translationX = startOffsetX.toFloat()
                slide.translationX(endTranslateX)
            }
            slide.interpolator = DecelerateInterpolator(2f)
            slide.duration = res.getInteger(R.integer.material_in_slide_anim_duration).toLong()
            slide.startDelay = delay
            slide.setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationCancel(animation: Animator) {
                    if (fade.isStarted) {
                        fade.cancel()
                    }
                    view.alpha = endAlpha
                    view.translationX = endTranslateX
                    view.translationY = endTranslateY
                }
            })
            slide.start()
        }
    }
}