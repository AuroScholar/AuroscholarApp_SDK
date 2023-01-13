package com.yugasa.latestkotlinwork.custom_ui

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.view.ViewTreeObserver
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import android.widget.TextView
import com.yugasa.composework.utils.AppUtil


@SuppressLint("NewApi")
class CustomTextView : TextView {
    constructor(context: Context?) : super(context) {}
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        if (!isInEditMode) {
            AppUtil.setTypeface(attrs, this)
        }
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyle: Int) : super(
        context,
        attrs,
        defStyle
    ) {
        if (!isInEditMode) {
            AppUtil.setTypeface(attrs, this)
        }
    }

    fun setTextAndMaxLines(text: CharSequence?) {
        val vto = viewTreeObserver
        vto.addOnGlobalLayoutListener(object : OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                val vto = viewTreeObserver
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                    vto.removeGlobalOnLayoutListener(this)
                } else {
                    vto.removeOnGlobalLayoutListener(this)
                }
                val height = height
                val lineHeight = lineHeight
                val maxlines = height / lineHeight
                maxLines = maxlines
            }
        })
    }
}