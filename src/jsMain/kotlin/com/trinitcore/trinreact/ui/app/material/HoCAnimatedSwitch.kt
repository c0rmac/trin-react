package com.trinitcore.trinreact.ui.app.material

import com.trinitcore.trinreact.ui.wrapper.glamor.glamorCss
import com.trinitcore.trinreact.ui.wrapper.reactroutertransition.AnimatedSwitchProps
import com.trinitcore.trinreact.ui.wrapper.reactroutertransition.animatedSwitch
import com.trinitcore.trinreact.ui.wrapper.reactroutertransition.spring
import kotlinext.js.js
import kotlinext.js.jsObject
import react.RBuilder
import react.RHandler

// TUS : Router animation configuration
private val hOCSwitchRule = glamorCss(js {
    this.position = "relative"
    this["& > div"] = js {
        this.position = "absolute"
    }
})

private fun glide(value: Double): dynamic {
    return spring(value, js("{ stiffness: 174, damping: 24 }"))
}

// DEIREADH

fun RBuilder.hoCAnimatedSwitch(handler: RHandler<AnimatedSwitchProps>)
    = animatedSwitch(className = "view-holder-container") {
        attrs {
            atEnter = jsObject { }
            atEnter.asDynamic().offset = 100

            atLeave = jsObject { }
            atLeave.asDynamic().offset = glide(-100.0)

            atActive = jsObject { }
            atActive.asDynamic().offset = glide(0.0)

            switchRule = hOCSwitchRule
            mapStyles = { styles: dynamic ->
                js {
                    if (styles.offset == 0) {
                        this.transform = "unset"
                    } else this.transform = "translateX(" + styles.offset + "%)"
                }
            }
        }

        handler.invoke(this)
    }