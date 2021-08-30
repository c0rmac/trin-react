package com.trinitcore.trinreact.ui.wrapper.reactroutertransition

import react.*

interface AnimatedSwitchProps : RProps {
    @Suppress("atEnter")
    var atEnter: RProps
    @Suppress("atLeave")
    var atLeave: RProps
    @Suppress("atActive")
    var atActive: RProps

    var mapStyles: dynamic

    var className: String?

    var switchRule: dynamic
    // var css:
}

/**
 * Animated switch
 */
fun RBuilder.animatedSwitch(
    className: String? = null,
    handler: RHandler<AnimatedSwitchProps>
) = child(
    AnimatedSwitch::class
) {
    attrs {
        this.className = className
    }
    handler.invoke(this)
}