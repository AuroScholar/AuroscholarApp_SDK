package com.yugasa.yubobotsdk.model

import com.google.gson.annotations.SerializedName

data class FirstUserTest(@SerializedName("success"  ) var success  : Boolean?  = null,
                         @SerializedName("formData" ) var formData : FormData? = FormData()) {
    data class FormData (

        @SerializedName("node_id"      ) var nodeId     : String?       = null,
        @SerializedName("node_name"    ) var nodeName   : String?       = null,
        @SerializedName("text"         ) var text       : String?       = null,
        @SerializedName("options"      ) var options    : List<Option> = arrayListOf(),
        @SerializedName("type_of_node" ) var typeOfNode : String?       = null,
        @SerializedName("type_opt"     ) var typeOpt    : Boolean?      = null,
        @SerializedName("default"      ) var default    : String?       = null,
        @SerializedName("followup"     ) var followup   : Followup?     = Followup(),
        @SerializedName("products"     ) var products   : List<String>  = arrayListOf(),
        @SerializedName("crm"          ) var crm        : Crm?          = Crm(),
        @SerializedName("action"       ) var action     : Action?       = Action()) {

        data class Followup (
            @SerializedName("nodeName"  ) var nodeName  : String? = null,
            @SerializedName("delayInMs" ) var delayInMs : String? = null
        )

        data class Crm (
            @SerializedName("property" ) var property : String?  = null,
            @SerializedName("checkbox" ) var checkbox : Boolean? = null
        )

        data class Action (
            @SerializedName("checkbox" ) var checkbox : Boolean? = null,
            @SerializedName("property" ) var property : String?  = null
        )
    }

}