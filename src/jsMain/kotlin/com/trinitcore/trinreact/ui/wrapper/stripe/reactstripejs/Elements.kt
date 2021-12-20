package com.trinitcore.trinreact.ui.wrapper.stripe.reactstripejs

import com.trinitcore.trinreact.ui.wrapper.stripe.stripejs.Stripe
import react.RProps
import react.RState
import kotlin.js.Promise

interface ElementsProps : RProps {
    var stripe: Promise<Stripe>
}

interface ElementsState : RState {

}