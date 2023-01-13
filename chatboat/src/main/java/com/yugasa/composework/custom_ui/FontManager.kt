package com.yugasa.composework.custom_ui

import android.content.Context
import android.graphics.Typeface
import android.view.View
import android.view.ViewGroup
import android.widget.TextView


class FontManager {
    companion object {
        const val ROOT = "fonts/"
        const val FONTAWESOME = ROOT + "fontawesome-webfont.ttf"

        fun getTypeface(context: Context, font: String?): Typeface {
            return Typeface.createFromAsset(context.assets, font)
        }

        fun markAsIconContainer(v: View?, typeface: Typeface?) {
            if (v is ViewGroup) {
                val vg = v
                for (i in 0 until vg.childCount) {
                    val child = vg.getChildAt(i)
                    markAsIconContainer(child, typeface)
                }
            } else if (v is TextView) {
                v.typeface = typeface
            }
        }
    }
}