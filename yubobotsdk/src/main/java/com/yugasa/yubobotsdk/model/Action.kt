package com.yugasa.yubobotsdk.model

import com.google.gson.annotations.SerializedName

data class Action(
    @SerializedName("checkbox") var checkbox: Boolean? = null,
    @SerializedName("property") var property: String? = null
)
