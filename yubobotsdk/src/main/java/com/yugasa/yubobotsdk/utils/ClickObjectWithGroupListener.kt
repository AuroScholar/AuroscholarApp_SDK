package com.yugasa.yubobotsdk.utils

import android.view.View

interface ClickObjectWithGroupListener {
    fun Click(position: Int, `object`: Any, parentPos: Int = 0)
}