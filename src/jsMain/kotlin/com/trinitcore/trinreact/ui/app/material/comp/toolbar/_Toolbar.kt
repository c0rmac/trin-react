package com.trinitcore.trinreact.ui.app.material.comp.toolbar

import com.trinitcore.trinreact.ui.trinreact.TComponent
import kotlinx.css.*
import react.*
import styled.css
import styled.styledDiv

private object GenericToolbarStyle : TComponent.Style("com.trinitcore.trinreact.fundemental.app.material.comp.toolbar.getGenericToolbar") {
    val body by css {
        display = Display.flex
        paddingBottom = 8.px
        minHeight = 56.px
    }

    val titleContainer by css {
        marginRight = LinearDimension.auto
        marginLeft = LinearDimension.auto
        marginTop = 16.px
        paddingLeft = 48.px
    }

    val exitButton by css {
        put("margin-top", "auto !important")
        put("margin-right", "0px !important")
    }
}

interface GenericToolbarProps : RProps {
    var leftButtons: Array<ReactElement>
    var rightButtons: Array<ReactElement>
}

internal val genericToolbar = functionalComponent<GenericToolbarProps> { props ->
    styledDiv {
        css {
            +GenericToolbarStyle.body
        }
        props.leftButtons.forEach { button ->
            +button
        }
        styledDiv {
            css {
                +GenericToolbarStyle.titleContainer
            }
            props.children()
        }
        props.rightButtons.forEach { button ->
            +button
        }
    }
}