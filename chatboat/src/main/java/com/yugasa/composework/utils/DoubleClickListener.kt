package com.yugasa.composework.utils

import android.view.View
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

abstract class DoubleClickListener : View.OnClickListener {
    private var lastClickTime: Long = 0
    private var singleClick: Boolean = true
    override fun onClick(v: View) {
        val clickTime = System.currentTimeMillis()
        if (clickTime - lastClickTime < DOUBLE_CLICK_TIME_DELTA) {
            onDoubleClick(v)
            singleClick = false
            lastClickTime = 0
        } else {
            MainScope().launch(Dispatchers.Main) {
                delay(300)
                if (singleClick) {
                    onSingleClick(v)
                }
                singleClick = true
            }
        }
        lastClickTime = clickTime
    }
    abstract fun onDoubleClick(v: View)
    abstract fun onSingleClick(v: View)

    companion object {
        private const val DOUBLE_CLICK_TIME_DELTA: Long = 300 //milliseconds
    }
}