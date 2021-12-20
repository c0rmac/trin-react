package com.trinitcore.trinreact.ui.app.material.comp.confirmation

import com.ccfraser.muirwik.components.MIconProps
import com.ccfraser.muirwik.components.button.MButtonSize
import com.ccfraser.muirwik.components.button.mFab
import com.ccfraser.muirwik.components.button.size
import com.ccfraser.muirwik.components.createStyled
import com.ccfraser.muirwik.components.setStyledPropsAndRunHandler
import com.trinitcore.trinreact.ui.trinreact.*
import com.trinitcore.trinreact.ui.wrapper.materialicon.checkIconComponent
import com.trinitcore.trinreact.ui.wrapper.materialicon.closeIconComponent
import kotlinx.css.*
import org.w3c.dom.events.Event
import react.RBuilder
import react.RComponent
import react.RState
import styled.css

abstract class ConfirmationButton :
    TComponent<ConfirmationButton.Props, ConfirmationButton.State>() {

    companion object {
        const val componentIdentifier = "com.clickcostz.fundemental.app.comp.confirmation.AcceptButton"
    }

    override val componentIdentifier: String
        get() = Companion.componentIdentifier

    interface Props : TProps {
        var subtle: Boolean
        var selected: Boolean

        var onClick: ((Event) -> Unit)?
        var loadOnClick: (suspend (Event) -> Unit)?
    }

    interface State : TState {

    }

    object Style : TComponent.Style(componentIdentifier) {

    }

    /* Subcomponents may be added below here */

    class Accept : ConfirmationButton() {
        override val icon: RComponent<MIconProps, RState>
            get() = checkIconComponent
        override val selectionColor: Color
            get() = Color.accept
    }

    class Reject : ConfirmationButton() {
        override val icon: RComponent<MIconProps, RState>
            get() = closeIconComponent
        override val selectionColor: Color
            get() = Color.rejectSubtle
    }

    /* Subcomponents may be added above here */

    // TUS : RENDER
    override fun RBuilder.innerRender() {
        mFab {
            css {
                if (props.subtle) {
                    put("width", "26px !important")
                    put("height", "26px !important")
                    put("min-height", "0 !important")
                }
                if (props.selected) put("background-color", selectionColor.value + " !important")
            }
            attrs {
                onClick = {
                    props.loadOnClick?.let { loadOnClick ->
                        load {
                            loadOnClick(it)
                        }
                    }
                    props.onClick?.invoke(it)
                }
                if (!props.subtle) this.size = MButtonSize.small
            }

            +createStyled(icon, addAsChild = false) { setStyledPropsAndRunHandler(null) {
                css {
                    if (props.selected) color = Color.white
                    else if (props.subtle) color = Color("#696969")
                }
            } }
        }
    }

    abstract val icon : RComponent<MIconProps, RState>
    abstract val selectionColor: Color
    // DEIREADH : RENDER

}