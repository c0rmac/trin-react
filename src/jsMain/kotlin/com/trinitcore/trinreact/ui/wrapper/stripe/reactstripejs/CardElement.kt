package com.trinitcore.trinreact.ui.wrapper.stripe.reactstripejs

import org.w3c.dom.events.Event
import react.RProps
import react.RState

data class CardElementOptions(
    val hidePostalCode: Boolean,
    val style: Style
    ) {
    data class Style(val base: Base) {
        data class Base(
            val fontSize: String,
            val color: String,
            val letterSpacing: String,
            val fontFamily: String
            )
    }
}

interface CardElementProps : RProps {
    var options: CardElementOptions?
    var onChange: (Event) -> Unit
}

interface CardElementState : RState {

}