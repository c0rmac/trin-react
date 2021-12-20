package com.trinitcore.trinreact.ui.app.material.comp

import com.ccfraser.muirwik.components.*
import com.ccfraser.muirwik.components.button.mIconButton
import com.trinitcore.trinreact.ui.trinreact.TComponent
import com.trinitcore.trinreact.ui.trinreact.TProps
import com.trinitcore.trinreact.ui.trinreact.TState
import com.trinitcore.trinreact.ui.wrapper.materialicon.mCloseIcon
import kotlinx.css.*
import org.w3c.dom.events.Event
import react.RBuilder
import react.RComponent
import react.RState
import react.ReactElement
import styled.css
import styled.styledDiv
import styled.styledH2

class Toast(props: Props) : TComponent<Toast.Props, TState>(props) {

    companion object {
        val componentIdentifier: String = "com.trinitcore.trinreact.fundemental.app.material.comp.Toast"
    }

    override val componentIdentifier: String
        get() = Companion.componentIdentifier

    interface Props : TProps {
        var propBundleForState: PropBundleForState

        var closeButtonOnClick: (Event) -> Unit
    }

    // TUS : STYLES
    object Style : TComponent.Style(componentIdentifier) {

        val body by css {
            position = Position.relative
            backgroundColor = Color.white
            put("box-shadow", "0 1px 16px 0 rgba(0,0,0,0.2),0 1px 1px 0 rgba(0,0,0,0.19) !important")
        }

        val titleRegion by css {
            padding(16.px, 8.px, 16.px, 8.px)
            display = Display.flex
            width = 100.pct

            children("*") {
                float = Float.left
                marginTop = LinearDimension.auto
                marginBottom = LinearDimension.auto
            }
        }

        val titleIcon by css {
            marginRight = 6.px
            put("font-size", "28px !important")
        }

        val titleText by css {

        }

        val content by css {
            width = 100.pct
        }

        val closeButton by css {
            position = Position.absolute
            top = 0.px
            right = 0.px
        }

    }
    // DEIREADH : STYLES

    // TUS : PROP_BUNDLE
    data class PropBundleForState(var titleText: String, var titleIcon: RComponent<MIconProps,RState>?, var content: ReactElement)
    // DEIREADH : PROP_BUNDLE

    override fun RBuilder.innerRender() {
        styledDiv {
            css { +Style.body }

            styledDiv {
                css { +Style.closeButton }
                mIconButton {
                    attrs {
                        this.onClick = props.closeButtonOnClick
                    }
                    mCloseIcon()
                }
            }

            styledDiv {
                css { +Style.titleRegion }
                val titleIcon = props.propBundleForState.titleIcon
                if (titleIcon != null)
                    +createStyled(titleIcon, addAsChild = false) {
                        setStyledPropsAndRunHandler(null) {
                            css { +Style.titleIcon }
                        }
                    }

                styledH2 {
                    css { +Style.titleText }
                    +props.propBundleForState.titleText
                }
            }
            styledDiv {
                css { +Style.content }
                +props.propBundleForState.content
            }
        }
    }

}