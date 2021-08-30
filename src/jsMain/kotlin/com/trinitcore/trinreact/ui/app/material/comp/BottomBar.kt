package com.trinitcore.trinreact.ui.app.material.comp

import com.trinitcore.trinreact.ui.trinreact.TComponent
import com.trinitcore.trinreact.ui.trinreact.TProps
import com.trinitcore.trinreact.ui.trinreact.TState
import kotlinx.css.*
import react.RBuilder
import styled.css
import styled.styledDiv

class BottomBar : TComponent<BottomBar.Props, BottomBar.State>() {

    companion object {
        const val componentIdentifier = "com.trinitcore.trinreact.fundemental.app.material.comp.BottomBar"
    }

    override val componentIdentifier: String
        get() = Companion.componentIdentifier

    interface Props : TProps {

    }

    interface State : TState {

    }

    object Style : TComponent.Style(componentIdentifier) {
        val wrapper by css {
            width = 100.pct
            left = 0.px
            paddingTop = 12.px
            paddingBottom = 12.px
            display = Display.flex
            backgroundColor = Color.white
            put("box-shadow", "0 1px 16px 0 rgba(0,0,0,0.2),0 1px 1px 0 rgba(0,0,0,0.19) !important")
        }

        val inner by css {
            display = Display.inlineBlock
            marginLeft = LinearDimension.auto
            marginRight = LinearDimension.auto
            width = 100.pct
        }
    }

    /* TUS : SUB_COMPONENTS */

    /* DEIREADH : SUB_COMPONENTS */

    override fun RBuilder.innerRender() {
        styledDiv {
            css { +Style.wrapper }
            styledDiv {
                css { +Style.inner }
                children()
            }
        }
    }

}