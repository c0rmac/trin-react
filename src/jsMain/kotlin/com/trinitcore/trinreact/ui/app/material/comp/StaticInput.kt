package com.trinitcore.trinreact.ui.app.material.comp

import com.ccfraser.muirwik.components.button.mButtonBase
import com.trinitcore.trinreact.ui.trinreact.TComponent
import com.trinitcore.trinreact.ui.trinreact.TProps
import com.trinitcore.trinreact.ui.trinreact.TState
import com.trinitcore.trinreact.ui.wrapper.materialicon.mArrowForwardIosIcon
import kotlinx.css.*
import org.w3c.dom.events.Event
import react.RBuilder
import styled.css
import styled.styledH4
import styled.styledH5

class StaticInput : TComponent<StaticInput.Props, StaticInput.State>() {

    companion object {
        const val componentIdentifier = "com.trinitcore.trinreact.fundemental.app.material.comp.StaticInput"
    }

    override val componentIdentifier: String
        get() = Companion.componentIdentifier

    interface Props : TProps {
        var onClick: (event: Event) -> Unit
        var label: String
        var value: String
    }

    interface State : TState {

    }

    object Style : TComponent.Style(componentIdentifier) {
        val changePassword by css {
            put("width", "100% !important")
            put("align-items", "unset !important")
            put("vertical-align", "unset !important")
            put("display", "block !important")
            put("text-align", "left !important")
            put("padding-top", "8px !important")
            put("padding-bottom", "8px !important")
        }
    }

    /* TUS : SUB_COMPONENTS */

    /* DEIREADH : SUB_COMPONENTS */

    override fun RBuilder.innerRender() {
        mButtonBase(onClick = props.onClick) {
            css { +Style.changePassword }
            styledH5 { css { fontWeight = FontWeight.w200 }
                +props.label
            }
            styledH4 {
                +props.value
            }
            mArrowForwardIosIcon {
                css {
                    position = Position.absolute
                    right = 2.px
                    put("font-size", "18px !important")
                    put("top", "16px !important")
                }
            }
        }
    }

}

fun RBuilder.staticInput(label: String, value: String, onClick: (event: Event) -> Unit) = child(StaticInput::class) {
    attrs.label = label
    attrs.value = value
    attrs.onClick = onClick
}