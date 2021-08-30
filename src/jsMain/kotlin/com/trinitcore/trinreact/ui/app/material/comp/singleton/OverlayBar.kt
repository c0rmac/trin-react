package com.trinitcore.trinreact.ui.app.material.comp.singleton

import com.ccfraser.muirwik.components.transitions.mZoom
import com.trinitcore.trinreact.ui.trinreact.TComponent
import com.trinitcore.trinreact.ui.trinreact.TProps
import com.trinitcore.trinreact.ui.trinreact.TState
import kotlinx.css.*
import react.RBuilder
import react.ReactElement
import react.setState
import styled.css
import styled.styledDiv

object OverlayBar : TComponent<OverlayBar.Props, OverlayBar.State>() {

    override val componentIdentifier: String = "com.trinitcore.trinreact.fundemental.app.material.comp.singleton.OverlayBar"

    interface Props : TProps {
    }

    interface State : TState {
        var visible: Boolean
        var content: ReactElement?
    }

    object Style : TComponent.Style(componentIdentifier) {
        val wrapper by css {
            position = Position.fixed
            left = 0.px
            bottom = 4.pct
            zIndex = 3
            width = 100.pct
            display = Display.flex
        }
        val inner by css {
            put("box-shadow", "0 10px 16px 0 rgba(0,0,0,0.2),0 6px 20px 0 rgba(0,0,0,0.19) !important")

            padding(4.px)
            backgroundColor = Color.white
            borderRadius = 28.px

            marginLeft = LinearDimension.auto
            marginRight = LinearDimension.auto
        }
    }

    override fun State.init() {
        this.visible = false
    }

    /* TUS : SUB_COMPONENTS */

    /* DEIREADH : SUB_COMPONENTS */

    fun show(content: ReactElement) = setState {
        this.visible = true
        this.content = content
    }

    fun hide() = setState {
        this.visible = false
    }

    override fun RBuilder.innerRender() {
        mZoom(show = state.visible) {
            styledDiv {
                css { +Style.wrapper }
                styledDiv {
                    css { +Style.inner }
                    state.content?.let { +it }
                }
            }
        }
    }

}