package com.trinitcore.trinreact.ui.wrapper.reactanimateheight

import com.ccfraser.muirwik.components.createStyled
import com.ccfraser.muirwik.components.setStyledPropsAndRunHandler
import react.RBuilder
import react.RComponent
import react.RHandler
import react.RState
import styled.StyledProps

interface ReactAnimationHeightProps : StyledProps {
    var height: Any
    var duration: Int
}

@JsModule("react-animate-height") @JsNonModule
private external val reactAnimateHeightModule: dynamic
@Suppress("UnsafeCastFromDynamic") private val reactAnimateHeightComponent: RComponent<ReactAnimationHeightProps, RState> = reactAnimateHeightModule.default
/** React Animate Height */
fun RBuilder.animateHeight(
    height: Any,
    duration: Int,
    className: String? = null,
    handler: RHandler<ReactAnimationHeightProps>
) = createStyled(reactAnimateHeightComponent) {
    attrs {
        this.height = height
        this.duration = duration
    }
    setStyledPropsAndRunHandler(className, handler)
}