package com.yugasa.yubokotlinsdk.model

data class ParamRequest(val clientId: String, val userId: String, val type: String, var session: Session) {
    constructor(clientId: String, userId: String, type: String, session: Session, clearChat: Boolean? = false) : this(clientId, userId, type, session)
}