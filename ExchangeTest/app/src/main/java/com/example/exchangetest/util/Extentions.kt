package com.example.exchangetest.util

import android.content.res.Resources
import android.util.TypedValue
import java.text.DecimalFormat
import java.util.*
import kotlin.math.roundToInt

fun Int.dpToPx() = TypedValue.applyDimension(
    TypedValue.COMPLEX_UNIT_DIP,
    this.toFloat(),
    Resources.getSystem().displayMetrics
).roundToInt()

fun Float.dpToPx() =
    TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        this,
        Resources.getSystem().displayMetrics
    )

fun Int.toStringDecimal(currency: Currency? = null): String {
    return toStringDecimal(this, currency)
}

fun Long.toStringDecimal(currency: Currency? = null): String {
    return toStringDecimal(this, currency)
}

fun Double.toStringDecimal(currency: Currency? = null): String {
    return toStringDecimal(this, currency)
}

private fun toStringDecimal(value: Any, currency: Currency?): String {
    var formatString = "#,###.00"
    currency?.let {
        for (i in 1..it.defaultFractionDigits) {
            if (i == 1) {
                formatString += "."
            }
            formatString += "#"
        }
    }
    return DecimalFormat(formatString).format(value)
}