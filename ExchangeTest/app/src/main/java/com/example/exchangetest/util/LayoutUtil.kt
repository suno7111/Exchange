package com.example.exchangetest.util

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.view.View
import androidx.annotation.ColorRes
import android.graphics.drawable.ColorDrawable
import java.lang.Exception

object LayoutUtil {

    @JvmStatic
    fun color(view: View, @ColorRes id: Int?) {
        val shape = if (view.background != null && view.background is GradientDrawable) {
            view.background as GradientDrawable
        } else {
            GradientDrawable()
        }

        var color = Color.TRANSPARENT
        id?.let {
            color = view.context.getColor(id)
        }

        shape.setColor(Color.rgb(Color.red(color), Color.green(color), Color.blue(color)))
        shape.alpha = Color.alpha(color)
        view.background = shape
    }

    @JvmStatic
    fun gradient(
        view: View, @ColorRes start: Int?, @ColorRes end: Int?,
        orientation: GradientDrawable.Orientation? = GradientDrawable.Orientation.TOP_BOTTOM
    ) {
        var startColor = Color.TRANSPARENT
        var endColor = Color.TRANSPARENT
        start?.let {
            startColor = view.context.getColor(start)
        }
        end?.let {
            endColor = view.context.getColor(end)
        }

        val shape = if (view.background != null && view.background is GradientDrawable) {
            view.background as GradientDrawable
        } else {
            GradientDrawable(orientation, intArrayOf(startColor, endColor))
        }

        view.background = shape
    }

    @JvmStatic
    fun corner(view: View, dp: Float) {
        val shape: GradientDrawable
        if (view.background != null && view.background is GradientDrawable) {
            shape = view.background as GradientDrawable
        } else {
            shape = GradientDrawable()
            getBackgroundColor(view)?.let {
                shape.setColor(Color.rgb(Color.red(it), Color.green(it), Color.blue(it)))
                shape.alpha = Color.alpha(it)
            }
        }

        shape.cornerRadius = dp.dpToPx()
        view.clipToOutline = true
        view.background = shape
    }

    @JvmStatic
    fun corner(view: View, leftTopDp: Float, rightTopDp: Float, rightBottomDp: Float, leftBottomDp: Float) {
        val shape: GradientDrawable
        if (view.background != null && view.background is GradientDrawable) {
            shape = view.background as GradientDrawable
        } else {
            shape = GradientDrawable()
            getBackgroundColor(view)?.let {
                shape.setColor(Color.rgb(Color.red(it), Color.green(it), Color.blue(it)))
                shape.alpha = Color.alpha(it)
            }
        }

        shape.cornerRadii = floatArrayOf(
            leftTopDp.dpToPx(),
            leftTopDp.dpToPx(),
            rightTopDp.dpToPx(),
            rightTopDp.dpToPx(),
            rightBottomDp.dpToPx(),
            rightBottomDp.dpToPx(),
            leftBottomDp.dpToPx(),
            leftBottomDp.dpToPx()
        )
        view.clipToOutline = true
        view.background = shape
    }

    @JvmStatic
    fun stroke(view: View, colorRes: Int?, widthDp: Float) {
        val shape: GradientDrawable
        if (view.background != null && view.background is GradientDrawable) {
            shape = view.background as GradientDrawable
        } else {
            shape = GradientDrawable()
            getBackgroundColor(view)?.let {
                shape.setColor(Color.rgb(Color.red(it), Color.green(it), Color.blue(it)))
                shape.alpha = Color.alpha(it)
            }
        }

        var color = Color.TRANSPARENT
        colorRes?.let {
            color = view.context.getColor(colorRes)
        }

        shape.setStroke(widthDp.dpToPx().toInt(), color)
        view.clipToOutline = true
        view.background = shape
    }

    @JvmStatic
    fun shadow(view: View, elevationDp: Float?, translationZDp: Float?, z: Float?) {
        if(elevationDp != null) {
            view.elevation = elevationDp.dpToPx()
        }

        if(translationZDp != null) {
            view.translationZ = translationZDp.dpToPx()
        }

        if(z != null) {
            view.z = z.dpToPx()
        }
    }

    @JvmStatic
    fun strokeDash(view: View, @ColorRes id: Int, widthDp: Float, dashWidth: Float, dashGap: Float) {
        val shape: GradientDrawable
        if (view.background != null && view.background is GradientDrawable) {
            shape = view.background as GradientDrawable
        } else {
            shape = GradientDrawable()
            getBackgroundColor(view)?.let {
                shape.setColor(Color.rgb(Color.red(it), Color.green(it), Color.blue(it)))
                shape.alpha = Color.alpha(it)
            }
        }

        val color = view.context.getColor(id)

        shape.setStroke(
            widthDp.dpToPx().toInt(),
            color,
            dashWidth.dpToPx(),
            dashGap.dpToPx()
        )
        view.clipToOutline = true
        view.background = shape
    }

    private fun getBackgroundColor(view: View): Int? {
        val drawable = view.background ?: return null

        if (drawable is ColorDrawable) {
            return drawable.color
        }

        try {
            var field = drawable.javaClass.getDeclaredField("mState")
            field.isAccessible = true
            val obj = field.get(drawable)
            field = obj.javaClass.getDeclaredField("mUseColor")
            field.isAccessible = true
            return field.getInt(obj)
        } catch (e: Exception) {
        }

        return null
    }
}