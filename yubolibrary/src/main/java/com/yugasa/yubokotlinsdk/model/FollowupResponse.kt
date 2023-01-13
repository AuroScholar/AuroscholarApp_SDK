package com.yugasa.yubokotlinsdk.model


//{"client_id":"auro_scholar","conflicting_intents":[],"default_opt":"False","intent":"faq_feedback",
//    "intents_coherence":[],"intents_need_more_phrases":[],"phrase_scores":{},"possible_conflict":"False",
//    "replies":[{"link":"n14","option":"Yes"},{"link":"n52","option":"No"}],"score":"NA",
//    "session":{"detect_lang":false,"email":"","lang":"en","name":"","phone":"","visitor_id":false},
//    "status":"success","text":"Was it helpful to you?","training_accuracy":0.0,"tree":{
//    "action":{"checkbox":false,"property":"Select Action to Associate"},"crm":{"checkbox":false,
//    "property":""},"default":"False","followup":{"delayInMs":"0","nodeName":""},
//    "node_id":"n51","node_name":"faq_feedback","options":[{"link":"n14","option":"Yes"},
//    {"link":"n52","option":"No"}],"products":[],"text":"Was it helpful to you?","type_of_node":"general",
//    "type_opt":false},"type_option":false,"userId":"80e93673199b43aa1645074431528","user_query":"faq_feedback",
//    "validation_accuracy":0.0}

data class FollowupResponse(
    var success: Boolean?,
    val default_opt: Boolean?,
    val replies: List<Option>?,
    val text: String? = "",
    val type_option: Boolean? = false,
    var tree: ResponseBot.Tree? = null,
) {
    data class Message(
        val default_opt: Boolean?,
        val replies: List<Option>?,
        val text: String? = "",
        val type_option: Boolean? = false
    )
}