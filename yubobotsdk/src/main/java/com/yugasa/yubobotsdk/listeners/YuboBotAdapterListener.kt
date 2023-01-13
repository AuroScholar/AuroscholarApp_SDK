package com.yugasa.yubobotsdk.listeners

interface YuboBotAdapterListener {
    fun onClickYuboBotView(key: Int, data: HashMap<Int, Any>)
}