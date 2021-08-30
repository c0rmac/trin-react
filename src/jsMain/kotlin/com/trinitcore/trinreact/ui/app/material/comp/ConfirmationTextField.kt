package com.trinitcore.trinreact.ui.app.material.comp

import com.ccfraser.muirwik.components.MTextFieldProps
import com.ccfraser.muirwik.components.mCircularProgress
import com.ccfraser.muirwik.components.mTextField
import com.trinitcore.trinreact.ui.app.material.comp.confirmation.ConfirmationButton
import com.trinitcore.trinreact.ui.trinreact.TComponent
import com.trinitcore.trinreact.ui.trinreact.TLoadingIndicType
import com.trinitcore.trinreact.ui.trinreact.TProps
import com.trinitcore.trinreact.ui.trinreact.TState
import kotlinx.css.*
import org.w3c.dom.events.Event
import react.RBuilder
import styled.StyledHandler
import styled.css
import styled.styledDiv

class ConfirmationTextField : TComponent<ConfirmationTextField.Props, ConfirmationTextField.State>() {

    companion object {
        const val componentIdentifier = "com.trinitcore.trinreact.fundemental.app.material.comp.ConfirmationTextField"
    }

    override val componentIdentifier: String
        get() = Companion.componentIdentifier

    interface Props : TProps {
        /** Callback for when the confirmation button is clicked. Return true if the load on click handler should be used */
        var confButtonOnClick: ((Event) -> Boolean)?
        var confirmSelected: Boolean

        var label: String
        var textFieldHandler: StyledHandler<MTextFieldProps>?
        /** Load a coroutine upon selecting the confirmation button.
         * The return value corresponds with ConfirmationButton.props.selected. */
        var loadConfButtonOnClick: (suspend () -> Unit)?
    }

    interface State : TState {
    }

    object Style : TComponent.Style(componentIdentifier) {
        val body by css {
            display = Display.flex
        }

        val confirmationButtonContainer by css {
            marginBottom = LinearDimension.auto
            marginTop = LinearDimension.auto
            paddingTop = 8.px
            paddingLeft = 8.px
        }
    }

    /* Subcomponents may be added below here */

    /* Subcomponents may be added above here */

    override var loadingIndicType: TLoadingIndicType = TLoadingIndicType.NONE

    override fun RBuilder.innerRender() {
        styledDiv {
            css { +Style.body }
            mTextField(props.label, handler = props.textFieldHandler)
            styledDiv {
                css { +Style.confirmationButtonContainer }
                if (state.isLoading) mCircularProgress {  }
                else
                    child(ConfirmationButton.Accept::class) {
                        attrs.selected = props.confirmSelected
                        attrs.onClick = {
                            val shouldUseLoadOnClickHandler = props.confButtonOnClick?.invoke(it)

                            if (shouldUseLoadOnClickHandler == true || shouldUseLoadOnClickHandler == null)
                                load {
                                    props.loadConfButtonOnClick?.invoke()
                                }
                        }
                    }
            }
        }
    }

}