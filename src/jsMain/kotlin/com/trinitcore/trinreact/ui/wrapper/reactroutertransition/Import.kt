@file:JsModule("react-router-transition")

package com.trinitcore.trinreact.ui.wrapper.reactroutertransition

import react.Component
import react.RState
import react.ReactElement


@JsName("AnimatedSwitch")
external class AnimatedSwitch : Component<AnimatedSwitchProps, RState> {
    override fun render(): ReactElement?
}

@JsName("spring")
external fun spring(value: Double, parameters: dynamic) : dynamic