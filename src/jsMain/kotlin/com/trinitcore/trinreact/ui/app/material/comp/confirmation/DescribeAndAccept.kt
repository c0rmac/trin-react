package com.trinitcore.trinreact.ui.app.material.comp.confirmation

import com.ccfraser.muirwik.components.button.MButtonSize
import com.ccfraser.muirwik.components.button.MButtonVariant
import com.ccfraser.muirwik.components.button.mButton
import com.trinitcore.trinreact.ui.trinreact.TComponent
import com.trinitcore.trinreact.ui.trinreact.TProps
import com.trinitcore.trinreact.ui.trinreact.TState
import com.trinitcore.trinreact.ui.trinreact.TStateControl
import com.trinitcore.trinreact.ui.wrapper.materialicon.mArrowForwardIosIcon
import kotlinx.css.*
import kotlinx.html.js.onClickFunction
import org.w3c.dom.events.Event
import react.*
import react.dom.br
import react.dom.span
import styled.css
import styled.styledDiv
import styled.styledH4
import styled.styledSpan

class DescribeAndAccept(props: Props) : TComponent<DescribeAndAccept.Props, DescribeAndAccept.State>(props) {

    companion object {
        const val componentIdentifier = "com.clickcostz.consumerapp.layout.comp.confirmation.DescribeAndAcceptViewRegion"
    }

    override val componentIdentifier: String
        get() = Companion.componentIdentifier

    interface Props : TProps {

        var textTitle: String?
        var textBody: String

        /** Tap the entire view to invoke the accept button click */
        var tapToClick: Boolean
        var acceptButtonOnClick: (isSelected: Boolean) -> Unit
        var rejectButtonOnClick: ((isSelected: Boolean) -> Unit)?

        var stateControl: StateControl

    }

    interface State : TState {

    }

    object Style : TComponent.Style(componentIdentifier) {
        val text by css {
            width = 100.pct
            whiteSpace = WhiteSpace.preLine
            height = LinearDimension.fitContent
            marginTop = LinearDimension.auto
            marginBottom = LinearDimension.auto
            paddingRight = 6.px
        }

        val textTitle by css {
            fontWeight = FontWeight.bold
            paddingBottom = 16.px
        }

        val wrapper by css {
            paddingLeft = 12.px
            paddingRight = 12.px
            display = Display.flex
        }

        val inner by css {
            display = Display.flex
            width = 100.pct
        }

        val buttonsWrapper by css {
            display = Display.flex
            position = Position.relative
        }

        val buttonsInner by css {
            marginTop = LinearDimension.auto
            marginBottom = LinearDimension.auto
            width = 45.px
            textAlign = TextAlign.center
        }

        val acceptButtonContainer by css {
            marginTop = LinearDimension.auto
            marginBottom = LinearDimension.auto
            width = 45.px
            textAlign = TextAlign.center
        }

        val rejectButtonContainer by css {
            marginTop = 16.px
        }

    }

    // TUS : STATE_CONTROL
    class StateControl(
            var acceptButtonIsSelected: Boolean,
            var rejectButtonIsSelected: Boolean,
            manager: TComponent<*, *>
    ) : TStateControl(manager)
    val stateControl: StateControl
    get() = props.stateControl
    // DEIREADH : STATE_CONTROL

    // TUS : EVENT_HANDLERS
    private fun handleBodyOnClick(event: Event) {
        if (props.tapToClick) handleConfirmationBtnOnClick(event)
    }

    private fun handleConfirmationBtnOnClick(event: Event) {
        val newValue = !stateControl.acceptButtonIsSelected
        stateControl.manager.setState {
            stateControl.acceptButtonIsSelected = newValue
            props.acceptButtonOnClick(newValue)
        }
    }

    private fun handleCancelBtnOnClick(event: Event) {
        val newValue = !stateControl.rejectButtonIsSelected
        stateControl.manager.setState {
            stateControl.rejectButtonIsSelected = newValue
            props.rejectButtonOnClick?.invoke(newValue)
        }
    }
    // DEIREADH : EVENT_HANDLERS

    override fun RBuilder.innerRender() {
        styledDiv {
            attrs.onClickFunction = ::handleBodyOnClick
            css { +Style.wrapper }
            styledDiv {
                css { +Style.inner }
                styledH4 {
                    css { +Style.text }
                    val textTitle = props.textTitle
                    if (textTitle != null) {
                        styledSpan {
                            css { +Style.textTitle }
                            +textTitle
                        }
                        br { }
                    }
                    span {
                        +props.textBody
                    }
                }
                styledDiv {
                    css { +Style.buttonsWrapper }
                    styledDiv {
                        css {
                            +Style.buttonsInner
                        }
                        styledDiv {
                            css { +Style.acceptButtonContainer }
                            child(ConfirmationButton.Accept::class) {
                                attrs.selected = stateControl.acceptButtonIsSelected
                                attrs.onClick = ::handleConfirmationBtnOnClick
                            }
                        }
                        props.rejectButtonOnClick?.let {
                            styledDiv {
                                css { +Style.rejectButtonContainer }
                                mButton(caption = "Skip",
                                    variant = MButtonVariant.text, size = MButtonSize.small,
                                    onClick = ::handleCancelBtnOnClick
                                ) {
                                    attrs.endIcon = mArrowForwardIosIcon(addAsChild = false) {
                                        css {
                                            fontSize = 0.9.rem
                                            marginLeft = 1.px
                                        }
                                    }
                                    css {
                                        put("padding", "2px !important")
                                        put("min-width", "44px !important")
                                        put("font-size", "0.9rem !important")
                                        children(".MuiButton-label") {
                                            children(".MuiButton-endIcon") {
                                                marginTop = (-1).px
                                                put("margin-left", "1px !important")
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

}