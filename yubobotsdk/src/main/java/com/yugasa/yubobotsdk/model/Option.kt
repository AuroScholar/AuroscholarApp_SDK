package com.yugasa.yubobotsdk.model

import com.google.gson.annotations.SerializedName
import java.nio.channels.spi.AbstractSelectionKey

data class Option(
    val link: String? = null,
    val option: String? = null,
    val score: Any? = null,
) {
    data class Score(
        @SerializedName("weight") var weight: String? = null,
        @SerializedName("label") var label: String? = null
    ) {

        companion object {
            fun dynamicScore(weight: String?, label: String?, key: String?) : Score {

                return Score()
            }
        }

    }
}