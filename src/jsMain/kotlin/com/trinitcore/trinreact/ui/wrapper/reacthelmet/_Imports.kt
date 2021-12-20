@file:JsModule("react-helmet")
@file:JsNonModule

package com.trinitcore.trinreact.ui.wrapper.reacthelmet

import react.Component
import react.RState
import react.ReactElement

@JsName("Helmet") external class Helmet : Component<HelmetProps, RState> { override fun render(): ReactElement? }