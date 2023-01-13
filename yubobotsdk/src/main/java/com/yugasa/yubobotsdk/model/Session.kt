package com.yugasa.yubobotsdk.model

import com.google.gson.annotations.SerializedName

class Session(
    var name: String? = null,
    var phone: String? = null,
    var email: String? = null,
    var appUserId: String? = "",
    val lang: String? = null,
    val createdDate: String? = null,
    val date: String? = null,
    val detect_lang: Boolean? = null,
    val visitor_id: Boolean? = null,
    var data: Map<String, Any>? = null,
    var score: Map<String, Any>? = null,
)