package com.clickcostz.fundemental.wrapper.oktareact

import com.trinitcore.trinreact.ui.wrapper.oktareact.Security
import react.RBuilder
import react.RHandler
import styled.StyledProps

interface SecurityProps : StyledProps {
    var issuer: String
    var clientId: String
    var redirectUri: String
    var pkce: Boolean
}

/**
 * Okta Security
 */
fun RBuilder.security(
    issuer: String,
    clientId: String,
    redirectUri: String,
    pkce: Boolean,
    handler: RHandler<SecurityProps>
) = child(
    Security::class
) {
    attrs {
        this.issuer = issuer
        this.clientId = clientId
        this.redirectUri = redirectUri
        this.pkce = pkce
    }
    handler.invoke(this)
}