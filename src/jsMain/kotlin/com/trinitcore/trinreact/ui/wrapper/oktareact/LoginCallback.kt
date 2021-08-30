package com.trinitcore.trinreact.ui.wrapper.oktareact

import react.RBuilder
import react.RHandler
import styled.StyledProps

interface LoginCallbackProps : StyledProps {

}

fun RBuilder.loginCallback(
    handler: RHandler<LoginCallbackProps>
) = child(
    LoginCallback::class
) {
    attrs {

    }
    handler.invoke(this)
}