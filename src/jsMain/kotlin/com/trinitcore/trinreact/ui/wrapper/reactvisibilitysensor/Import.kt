package com.trinitcore.trinreact.ui.wrapper.reactvisibilitysensor

import com.ccfraser.muirwik.components.createStyled
import com.ccfraser.muirwik.components.setStyledPropsAndRunHandler
import react.*
import styled.StyledProps

interface VizSensorProps : StyledProps {
    var onChange: ((isVisible: Boolean) -> Unit)?
}

@JsModule("react-visibility-sensor")
private external val reactVisibilitySensorModule: dynamic
@Suppress("UnsafeCastFromDynamic") private val reactVisibilitySensorComponent: RComponent<VizSensorProps, RState> = reactVisibilitySensorModule.default
/** Viz Sensor */
fun RBuilder.vizSensor(className: String? = null, handler: RHandler<VizSensorProps>) = createStyled(
    reactVisibilitySensorComponent
) { setStyledPropsAndRunHandler(className, handler) }