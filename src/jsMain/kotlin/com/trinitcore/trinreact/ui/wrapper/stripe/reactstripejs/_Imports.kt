@file:JsModule("@stripe/react-stripe-js")
@file:JsNonModule

package com.trinitcore.trinreact.ui.wrapper.stripe.reactstripejs

import react.Component
import react.RComponent
import react.ReactElement
import react.children
import kotlin.reflect.KClass

@JsName("CardElement")
external class CardElement : Component<CardElementProps, CardElementState> { override fun render(): ReactElement? }

@JsName("Elements")
external class Elements : Component<ElementsProps, ElementsState> {
    fun <R : Component<*, *>>getElement(kClass: JsClass<R>): Component<*, *>
    override fun render(): ReactElement?
}

@JsName("ElementsConsumer")
external class ElementsConsumer : Component<ElementsConsumerProps, ElementsConsumerState> {
    override fun render(): ReactElement?
}