package com.trinitcore.trinreact.ui.trinreact.styled

import com.trinitcore.trinreact.ui.trinreact.TState
import react.RBuilder
import react.RComponent
import react.ReactElement
import styled.StyledElementBuilder
import styled.StyledHandler
import styled.StyledProps
import kotlin.reflect.KClass

fun <P : StyledProps, R : TState> RBuilder.createStyled(
        component: KClass<out RComponent<P, R>>,
        addAsChild: Boolean = true,
        handler: StyledHandler<P>
): ReactElement {
    val builder = StyledElementBuilder<P>(component.js)
    handler(builder)
    return if (addAsChild) child(builder.create()) else builder.create()
}