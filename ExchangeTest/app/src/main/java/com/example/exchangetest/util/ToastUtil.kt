package com.example.exchangetest.util

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import com.example.exchangetest.R

object ToastUtil {

    fun showToast(context: Context, msg: String) {
        val toast = Toast.makeText(context, msg, Toast.LENGTH_SHORT)
        toast.view = LayoutInflater.from(context).inflate(R.layout.view_toast, null)
        toast.view.findViewById<TextView>(R.id.toast_tv).let { tv ->
            LayoutUtil.corner(tv, 4f)
            tv.setTextColor(context.getColor(R.color.pinky_red))
            tv.text = msg
        }
        toast.show()
    }
}