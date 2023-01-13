package com.yugasa.yubokotlinsdk.model

import com.google.gson.annotations.SerializedName

data class Crm(
    @SerializedName("property") var property: String? = null,
    @SerializedName("checkbox") var checkbox: Boolean? = null
)
