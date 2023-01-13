package com.yugasa.yubobotsdk.utils

import android.view.View

interface RecyclerItemClickListener {
    fun onItemClick(pos: Int?, view: View? = null)
}