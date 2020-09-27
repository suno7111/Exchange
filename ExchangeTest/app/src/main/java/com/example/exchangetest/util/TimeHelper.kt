package com.example.exchangetest.util

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.*

@SuppressLint("SimpleDateFormat")
object TimeHelper {
    private val klass = "$this@${Integer.toHexString(hashCode())}"

    fun msToFormat(ms: Long, format: String): String {
        return SimpleDateFormat(format).format(ms)
    }

    fun msToYMD(ms: Long, isAbsolute: Boolean = false): String {
        return if (isAbsolute) {
            SimpleDateFormat("yyyy-MM-dd", Locale.UK).format(ms)
        } else {
            SimpleDateFormat("yyyy-MM-dd").format(ms)
        }
    }

    fun msToYMDhms(ms: Long): String {
        return SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(ms)
    }

    fun msToYMDhm(ms: Long): String {
        return SimpleDateFormat("yyyy-MM-dd HH:mm").format(ms)
    }

    fun dateStringToMs(dateString: String, format: String): Long {
        val dateFormat = SimpleDateFormat(format)
        dateFormat.parse(dateString)?.let {
            return it.time
        } ?: run {
            return 0
        }
    }

    fun getTodayMs(ms: Long) : Long {
        val date = dateStringToMs(msToYMD(ms),"yyyy-MM-dd")
        return date
    }
}
