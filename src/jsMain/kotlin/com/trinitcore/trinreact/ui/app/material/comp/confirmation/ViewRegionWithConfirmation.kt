package com.trinitcore.trinreact.ui.app.material.comp.confirmation

import com.ccfraser.muirwik.components.transitions.mZoom
import com.trinitcore.trinreact.ui.app.material.comp.ConfirmationTextField
import com.trinitcore.trinreact.ui.trinreact.TComponent
import com.trinitcore.trinreact.ui.trinreact.TProps
import com.trinitcore.trinreact.ui.trinreact.TState
import kotlinx.css.*
import org.w3c.dom.events.Event
import react.RBuilder
import styled.css
import styled.styledDiv

class ViewRegionWithConfirmation(props: Props) :
    TComponent<ViewRegionWithConfirmation.Props, ViewRegionWithConfirmation.State>(props) {

    companion object {
        const val componentIdentifier = "com.trinitcore.trinreact.fundemental.app.material.comp.confirmation.ViewRegionWithConfirmation"
    }

    override val componentIdentifier: String
        get() = Companion.componentIdentifier

    interface Props : TProps {

        var confButtonOnClick: ((Event) -> Unit)?
        var confButtonLoadOnClick: (suspend (Event) -> Unit)?

        var confButtonSelected: Boolean
        var confButtonVisible: Boolean

    }

    interface State : TState {

    }

    object Style : TComponent.Style(componentIdentifier) {
        val body by ConfirmationTextField.Style.css {
            display = Display.flex
        }

        val confirmationButtonContainer by css {
            marginBottom = LinearDimension.auto
            marginTop = LinearDimension.auto
            paddingTop = 8.px
            paddingLeft = 8.px
        }

        val contentBody by css {
            width = 100.pct
        }
    }

    /* TUS : SUB_COMPONENTS */

    /* DEIREADH : SUB_COMPONENTS */

    override fun RBuilder.innerRender() {
        styledDiv {
            css { +Style.body }
            styledDiv {
                css { +Style.contentBody }
                children()
            }
            styledDiv {
                css { +Style.confirmationButtonContainer }

                mZoom(show = props.confButtonVisible) {
                    styledDiv {
                        child(ConfirmationButton.Accept::class) {
                            attrs.onClick = props.confButtonOnClick
                            attrs.loadOnClick = props.confButtonLoadOnClick

                            attrs.selected = props.confButtonSelected
                        }
                    }
                }
            }
        }
    }

}