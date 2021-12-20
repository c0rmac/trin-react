@file:JsModule("react-full-page")
@file:JsNonModule

package com.trinitcore.trinreact.ui.wrapper.reactfullpage

import react.Component
import react.RState
import react.ReactElement

@JsName("FullPage") external class FullPage : Component<FullPageProps, RState> { override fun render(): ReactElement? }

@JsName("Slide") external class Slide : Component<SlideProps, RState> { override fun render(): ReactElement? }
