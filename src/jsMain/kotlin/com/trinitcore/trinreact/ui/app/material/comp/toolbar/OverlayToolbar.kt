package com.trinitcore.trinreact.ui.app.material.comp.toolbar

import com.trinitcore.trinreact.ui.app.material.App
import com.trinitcore.trinreact.ui.trinreact.TComponent
import com.trinitcore.trinreact.ui.trinreact.TProps
import com.trinitcore.trinreact.ui.trinreact.TState
import com.trinitcore.trinreact.ui.trinreact.ViewController
import com.trinitcore.trinreact.ui.trinreact.styled.createStyled
import com.trinitcore.trinreact.ui.wrapper.materialicon.mArrowBackIosIcon
import com.trinitcore.trinreact.ui.wrapper.materialicon.mMenuIcon
import kotlinx.css.*
import react.RBuilder
import react.ReactElement
import react.child
import styled.StyledHandler
import styled.StyledProps
import styled.css
import styled.styledDiv

class OverlayToolbar : TComponent<OverlayToolbar.Props, OverlayToolbar.State>() {

    companion object {
        const val componentIdentifier = "com.trinitcore.trinreact.fundemental.app.material.comp.toolbar.OverlayToolbar"
    }

    override val componentIdentifier: String
        get() = Companion.componentIdentifier

    interface Props : TProps, GenericToolbarProps, StyledProps {

    }

    interface State : TState {

    }

    object Style : TComponent.Style(componentIdentifier) {
        val body by css {
            position = Position.absolute
            top = 0.px
            left = 0.px
            width = 100.pct
            zIndex = 3

            val padding = 18
            width = 100.pct - (padding * 2).px
            paddingLeft = padding.px
            paddingRight = padding.px
            paddingTop = (padding * 0.7).px
        }
    }

    /* TUS : SUB_COMPONENTS */

    /* DEIREADH : SUB_COMPONENTS */

    override fun RBuilder.innerRender() {
        styledDiv {
            css { +Style.body }
            child(genericToolbar) {
                attrs.leftButtons = props.leftButtons
                attrs.rightButtons = props.rightButtons
            }
        }
    }

}

fun RBuilder.overlayToolbar(
    leftButtonUIBS: Array<ReactElement> = emptyArray(),
    rightButtonUIBS: Array<ReactElement> = emptyArray(),
    handler: StyledHandler<OverlayToolbar.Props>
) = createStyled(OverlayToolbar::class) {
    attrs.leftButtons = leftButtonUIBS
    attrs.rightButtons = rightButtonUIBS
    handler.invoke(this)
}

fun RBuilder.defaultOverlayToolbar(
    viewControllerCxt: ViewController<*, *, *>,
    handler: StyledHandler<OverlayToolbar.Props>
) = overlayToolbar(leftButtonUIBS = arrayOf(
        fabToolbarButton(
                addAsChild = false,
                onClick = {
                    viewControllerCxt.history?.push(viewControllerCxt.defaultNavBackPath
                            ?: throw IllegalStateException("viewControllerCxt.defaultNavBackPath is not defined."))
                },
                iconComponent = mArrowBackIosIcon(addAsChild = false) {
                    css { marginLeft = 5.px }
                }
        )
),
        rightButtonUIBS = arrayOf(
                fabToolbarButton(
                        addAsChild = false,
                        onClick = { App.showDrawer() },
                        iconComponent = mMenuIcon(addAsChild = false)
                )
        ),
        handler
)