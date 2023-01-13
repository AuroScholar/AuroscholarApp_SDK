package com.yugasa.yubobotsdk.service


interface ApiCallListener {
    fun onSuccess(response: String, requestUrl: String)
    fun onError(response: String, requestUrl: String)
}


