package com.trinitcore.trinreact.ui.wrapper.reacthelmet

import react.RBuilder
import react.RHandler
import react.RProps

interface HelmetProps : RProps {

}

fun RBuilder.helmet(handler: RHandler<HelmetProps>) = child(Helmet::class) {
    handler.invoke(this)
}