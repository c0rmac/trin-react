package com.ccfraser.muirwik.components.button

import com.ccfraser.muirwik.components.*
import org.w3c.dom.events.Event
import react.RBuilder
import react.RComponent
import react.RProps
import react.RState
import styled.StyledHandler

@JsModule("@material-ui/core/ButtonBase")
@JsNonModule
private external val buttonBaseModule: dynamic

@Suppress("UnsafeCastFromDynamic")
private val buttonBaseComponent: RComponent<MButtonBaseProps, RState> = buttonBaseModule.default

interface MButtonBaseProps: StyledPropsWithCommonAttributes {
    var centerRipple: Boolean
    var component: String
    var disabled: Boolean
    var disableRipple: Boolean
    var focusRipple: Boolean
    var onKeyboardFocus: (Event) -> Unit

    @JsName("TouchRippleProps")
    var touchRippleProps: RProps?
}

@Suppress("EnumEntryName")
enum class MButtonSize {
    small, medium, large
}

@Suppress("EnumEntryName")
enum class MButtonVariant {
    text, outlined, contained
}

fun RBuilder.mButtonBase(
        disabled: Boolean = false,
        onClick: ((Event) -> Unit)? = null,
        hRefOptions: HRefOptions? = null,

        addAsChild: Boolean = true,
        className: String? = null,
        handler: StyledHandler<MButtonBaseProps>? = null) = createStyled(buttonBaseComponent, addAsChild) {
    attrs.disabled = disabled
    hRefOptions?.let { setHRefTargetNoOpener(attrs, it) }
    onClick?.let { attrs.onClick = onClick }

    setStyledPropsAndRunHandler(className, handler)
}