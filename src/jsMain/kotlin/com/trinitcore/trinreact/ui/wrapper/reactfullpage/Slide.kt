package com.trinitcore.trinreact.ui.wrapper.reactfullpage

import react.RBuilder
import react.RHandler
import styled.StyledProps

interface SlideProps : StyledProps {

}

fun RBuilder.slide(handler: RHandler<SlideProps>) = child(Slide::class) {
    handler.invoke(this)
}