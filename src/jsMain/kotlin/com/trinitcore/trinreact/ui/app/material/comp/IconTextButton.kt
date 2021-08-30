package com.trinitcore.trinreact.ui.app.material.comp

import com.ccfraser.muirwik.components.button.mButtonBase
import com.trinitcore.trinreact.ui.trinreact.TComponent
import com.trinitcore.trinreact.ui.trinreact.TProps
import com.trinitcore.trinreact.ui.trinreact.TState
import kotlinx.css.*
import org.w3c.dom.events.Event
import react.RBuilder
import react.ReactElement
import styled.css
import styled.styledDiv
import styled.styledH5

class IconTextButton : TComponent<IconTextButton.Props, IconTextButton.State>() {

    companion object {
        const val componentIdentifier = "com.trinitcore.trinreact.fundemental.app.material.comp.IconTextButton"
    }

    override val componentIdentifier: String
        get() = Companion.componentIdentifier

    interface Props : TProps {
        var onClick: ((Event) -> Unit)?
        var icon: ReactElement
        var text: String
    }

    interface State : TState {

    }

    object Style : TComponent.Style(componentIdentifier) {
        val wrapper by css {
            put("border-radius", "8px !important")
        }
        val inner by css {
            padding(0.px)
        }

        val iconContainer by css {
            marginBottom = 0.px
        }
        val text by css {
            whiteSpace = WhiteSpace.preLine
        }
    }

    /* TUS : SUB_COMPONENTS */

    /* DEIREADH : SUB_COMPONENTS */

    override fun RBuilder.innerRender() {
        mButtonBase(onClick = props.onClick) {
            css { +Style.wrapper }
            styledDiv {
                css { +Style.inner }
                styledDiv {
                    css { +Style.iconContainer }
                    +props.icon
                }
                styledDiv {
                    styledH5 {
                        css { +Style.text }
                        +props.text
                    }
                }
            }
        }
    }

}

fun RBuilder.iconTextButton(
    text: String,
    icon: ReactElement,
    onClick: ((Event) -> Unit)? = null
) = child(IconTextButton::class) {
    attrs.text = text
    attrs.icon = icon
    attrs.onClick = onClick
}