package com.yugasa.yubobotsdk.model

import com.google.gson.annotations.SerializedName

data class Followup(
    @SerializedName("nodeName") var nodeName: String? = null,
    @SerializedName("delayInMs") var delayInMs: String? = null
)
