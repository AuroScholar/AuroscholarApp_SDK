package com.yugasa.yubobotsdk.model

import com.google.gson.annotations.SerializedName

data class ResponseBot(
    val client_id: String? = null,
    val userId: String? = null,
    var default_opt: String? = "False",
    var intent: String? = "",
    var train: String? = null,
    var node: String? = null,
    var node_id: String? = null,
    var node_name: String? = null,
    var basic: String? = null,
    var update_tree: String? = null,
    var update_story: String? = null,
    var category: String? = null,
    var unsubsribe: String? = null,
    var possible_conflict: String? = null,
    var replies: List<Option>? = null,
    var score: String? = null,
    var session: Session? = null,
    var status: String? = null,
    var text: String? = null,
    var message: String? = null,
    var success: Boolean? = null,
    var tree: Any? = null,
    @SerializedName(
        value = "type_option",
        alternate = ["type_opt"]
    ) var type_option: Boolean? = null,
    var chatHisory: Int? = 0,
    var typeMsg: String? = null
) {
    lateinit var strTree: String
    lateinit var objTree: Tree

    data class Tree(
        val default: String? = null,
        val node_id: String? = null,
        val node_name: String? = null,
        var options: List<Option>? = null,
        val products: List<Product>? = null,
        var text: String? = null,
        val type_of_node: String? = null,
        val type_opt: Boolean? = null,
        val action: Action? = null,
        val crm: Crm? = null,
        val followup: Followup? = null
    )

    data class Product(
        val cart_link: String? = "",
        val description: String? = "",
        val id: String? = "",
        val image: String? = "",
        val info_link: String? = "",
        val price: String? = "",
        var title: String? = ""
    )

}