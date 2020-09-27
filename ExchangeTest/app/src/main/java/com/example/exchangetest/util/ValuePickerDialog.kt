package com.example.exchangetest.util

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.annotation.Dimension
import androidx.annotation.StringRes
import com.example.exchangetest.R
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

interface OnValuePickerCallback {
    fun onPicked(index: Int, value: String)
}

class ValuePickerDialog(context: Context) : Dialog(context) {

    var title: String? = null
    var divider = false
    var selectedValue: Int? = null
    var selectedTextColor = context.getColor(R.color.white_two)
    var selectedBackgroundColor: Int? = null
    var selectedTv: TextView? = null
    var firstScrollPosition: Int? = null

    private var items: ArrayList<ValuePickerData> = ArrayList()
    private var callback: OnValuePickerCallback? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val view = View.inflate(context, R.layout.view_value_picker, null)
        val containerLl = view.findViewById<LinearLayout>(R.id.container_ll)

        title?.let {
            val titleTv = view.findViewById<TextView>(R.id.title_tv)
            titleTv.visibility = View.VISIBLE
            titleTv.text = it
        }

        items.forEachIndexed { index, value ->
            val valueTv = TextView(context)
            valueTv.setPadding(
                30f.dpToPx().toInt(),
                20f.dpToPx().toInt(),
                0,
                20f.dpToPx().toInt()
            )
            valueTv.setTextColor(context.getColor(value.color))
            valueTv.setTextSize(Dimension.SP, 15f)
            valueTv.text = value.text
            valueTv.background = context.getDrawable(R.color.white)
            valueTv.setOnClickListener {
                selectedValue?.let { key ->
                    selectedTv?.apply {
                        setTextColor(context.getColor(items[key].color))
                        background = context.getDrawable(R.color.white)
                    }
                }

                selectedBackgroundColor?.let { backgroundColor ->
                    valueTv.setTextColor(selectedTextColor)
                    valueTv.setBackgroundColor(backgroundColor)

                    GlobalScope.launch {
                        delay(20)
                        dismiss()
                    }
                } ?: run {
                    dismiss()
                }
                callback?.onPicked(index, value.text)

            }

            if (selectedValue == index) {
                valueTv.setTextColor(selectedTextColor)
                selectedBackgroundColor?.let {
                    valueTv.setBackgroundColor(it)
                }
                valueTv.addOnLayoutChangeListener { _, _, top, _, _, _, _, _, _ ->
                    findViewById<ScrollView>(R.id.scroll_v).apply {
                        scrollTo(0, top)
                    }
                }
                selectedTv = valueTv
            }

            if (firstScrollPosition == index) {
                valueTv.addOnLayoutChangeListener { _, _, top, _, _, _, _, _, _ ->
                    findViewById<ScrollView>(R.id.scroll_v).apply {
                        scrollTo(0, top)
                    }
                }
            }

            containerLl.addView(valueTv)

            if (divider && index < items.size - 1) {
                val divider = LinearLayout(context)
                val params = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, 1f.dpToPx().toInt()
                )
                divider.layoutParams = params
                divider.background = context.getDrawable(R.color.white_two)
                containerLl.addView(divider)
            }
        }

        setContentView(view)
    }

    fun setCallback(callback: OnValuePickerCallback) {
        this.callback = callback
    }

    fun addItem(@StringRes stringRes: Int, @ColorRes colorRes: Int) {
        items.add(ValuePickerData(context.getString(stringRes), colorRes))
    }

    fun addItem(text: String, @ColorRes colorRes: Int) {
        items.add(ValuePickerData(text, colorRes))
    }

    private data class ValuePickerData(val text: String, @ColorRes val color: Int)
}



