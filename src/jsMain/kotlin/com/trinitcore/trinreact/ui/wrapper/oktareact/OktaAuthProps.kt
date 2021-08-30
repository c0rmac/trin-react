package com.trinitcore.trinreact.ui.wrapper.oktareact

import react.RProps

interface OktaAuthProps : RProps {
    var authState: dynamic
    var authService: dynamic
}