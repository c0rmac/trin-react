package com.trinitcore.trinreact.ui.app.material.comp.toolbar

import com.ccfraser.muirwik.components.MColor
import com.ccfraser.muirwik.components.button.MButtonSize
import com.ccfraser.muirwik.components.button.mFab
import com.ccfraser.muirwik.components.button.mIconButton
import com.trinitcore.trinreact.ui.trinreact.TComponent
import com.trinitcore.trinreact.ui.trinreact.TProps
import com.trinitcore.trinreact.ui.trinreact.TState
import com.trinitcore.trinreact.ui.trinreact.styled.createStyled
import react.RBuilder
import react.ReactElement
import styled.StyledProps
import styled.css

abstract class ToolbarButton : TComponent<ToolbarButton.Props, TState>() {

    companion object {
        const val componentIdentifier = "com.trinitcore.trinreact.fundemental.app.material.comp.toolbar.ToolbarButton"
    }

    override val componentIdentifier: String
        get() = Companion.componentIdentifier

    interface Props : TProps, StyledProps {
        var onClick: () -> Unit
        var iconComponent: ReactElement
    }

}


class FABToolbarButton : ToolbarButton() {

    companion object {
        const val componentIdentifier = "com.trinitcore.trinreact.fundemental.app.material.comp.toolbar.FABToolbarButton"
    }

    override val componentIdentifier: String
        get() = Companion.componentIdentifier

    private object Style : TComponent.Style(componentIdentifier) {
        val body by css {
            put("background-color", "white !important")
        }
    }

    override fun RBuilder.innerRender() {
        mFab(color = MColor.default, size = MButtonSize.small, onClick = {
            props.onClick()
        }) {
            css {
                +Style.body
            }
            +props.iconComponent
        }
    }

}

fun RBuilder.fabToolbarButton(
    addAsChild: Boolean = true,
    onClick: () -> Unit,
    iconComponent: ReactElement
) = createStyled(FABToolbarButton::class, addAsChild = addAsChild) {
    attrs.onClick = onClick
    attrs.iconComponent = iconComponent
}

class IconToolbarButton : ToolbarButton() {

    companion object {
        const val componentIdentifier = "com.trinitcore.trinreact.fundemental.app.material.comp.toolbar.IconToolbarButton"
    }

    override val componentIdentifier: String
        get() = Companion.componentIdentifier

    private object Style : TComponent.Style(componentIdentifier) {
        val exitButton by css {
            put("margin-top", "auto !important")
            put("margin-right", "0px !important")
        }
    }

    override fun RBuilder.innerRender() {
        mIconButton(color = MColor.primary, onClick = {
            props.onClick()
        }) {
            css { +Style.exitButton }
            +props.iconComponent
        }
    }

}

fun RBuilder.iconToolbarButton(
    addAsChild: Boolean = true,
    onClick: () -> Unit,
    iconComponent: ReactElement
) = createStyled(IconToolbarButton::class, addAsChild = addAsChild) {
    attrs.onClick = onClick
    attrs.iconComponent = iconComponent
}