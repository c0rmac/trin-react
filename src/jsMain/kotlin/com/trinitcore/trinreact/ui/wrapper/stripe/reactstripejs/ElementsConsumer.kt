package com.trinitcore.trinreact.ui.wrapper.stripe.reactstripejs

import com.trinitcore.trinreact.ui.wrapper.stripe.stripejs.StripeContext
import react.*

interface ElementsConsumerProps : RProps {

}

interface ElementsConsumerState : RState {

}

fun RBuilder.styledElementsConsumer(children: (context: StripeContext) -> ReactElement) {
    child(ElementsConsumer::class) {
        childList.add(children)
    }
}