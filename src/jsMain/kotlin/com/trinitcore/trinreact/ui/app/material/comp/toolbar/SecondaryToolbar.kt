package com.trinitcore.trinreact.ui.app.material.comp.toolbar

import com.trinitcore.trinreact.ui.trinreact.TComponent
import com.trinitcore.trinreact.ui.trinreact.TProps
import com.trinitcore.trinreact.ui.trinreact.TState
import com.trinitcore.trinreact.ui.trinreact.styled.createStyled
import react.*
import styled.StyledHandler
import styled.StyledProps

class SecondaryToolbar : TComponent<SecondaryToolbar.Props, SecondaryToolbar.State>() {

    companion object {
        const val componentIdentifier = "com.trinitcore.trinreact.fundemental.app.material.comp.toolbar.SecondaryToolbar"
    }

    override val componentIdentifier: String
        get() = Companion.componentIdentifier

    interface Props : TProps, GenericToolbarProps, StyledProps {

    }

    interface State : TState {

    }



    /* TUS : SUB_COMPONENTS */

    /* DEIREADH : SUB_COMPONENTS */


    override fun RBuilder.innerRender() {
        child(genericToolbar) {
            attrs.rightButtons = props.rightButtons
            attrs.leftButtons = props.leftButtons
            props.children()
        }
    }

}

fun RBuilder.secondaryToolbar(
    leftButtonUIBS: Array<ReactElement> = emptyArray(),
    rightButtonUIBS: Array<ReactElement> = emptyArray(),
    handler: StyledHandler<SecondaryToolbar.Props>
) = createStyled(SecondaryToolbar::class) {
    attrs.leftButtons = leftButtonUIBS
    attrs.rightButtons = rightButtonUIBS
    handler.invoke(this)
}