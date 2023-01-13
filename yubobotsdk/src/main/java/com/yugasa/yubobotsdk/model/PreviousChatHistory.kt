package com.yugasa.yubobotsdk.model

import com.google.gson.annotations.SerializedName

data class PreviousChatHistory(
    @SerializedName("success") var success: Boolean? = null,
    @SerializedName("message") var message: Message? = null,
    @SerializedName("formData") var formData: FormData? = null
) {

    data class Message(
        @SerializedName("userChat") var userChat: List<UserChat> = arrayListOf(),
        @SerializedName("sessionData") var sessionData: Session? = null
    ) {
        data class UserChat(

            @SerializedName("date") var date: String? = null,
            @SerializedName("createdAt") var createdAt: String? = null,
            @SerializedName("node") var node: String? = null,
            @SerializedName("session") var session: Session? = Session(),
            @SerializedName("typing") var typing: Boolean? = null,
            @SerializedName("replies") var replies: List<Option> = arrayListOf(),
            @SerializedName("messageType") var messageType: String? = null,
            @SerializedName("type_option") var type_option: Boolean? = null,
            @SerializedName("text") var text: String? = null,
            @SerializedName("intent") var intent: String? = "",

            )
    }

    data class FormData(

        @SerializedName("node_id") var nodeId: String? = null,
        @SerializedName("node_name") var nodeName: String? = null,
        @SerializedName("text") var text: String? = null,
        @SerializedName("options") var options: List<Option> = arrayListOf(),
        @SerializedName("type_of_node") var typeOfNode: String? = null,
        @SerializedName("type_opt") var typeOpt: Boolean? = null,
        @SerializedName("default") var default: String? = null,
        @SerializedName("followup") var followup: Followup? = null,
        @SerializedName("products") var products: List<String> = arrayListOf(),
        @SerializedName("crm") var crm: Crm? = Crm(),
        @SerializedName("action") var action: Action? = Action()
    )

}
