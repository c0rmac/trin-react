package com.trinitcore.trinreact.ui.wrapper.oktareact

import react.*
import react.router.dom.RouteResultProps

interface SecureRouteProps<T : RProps> : RProps {
    var path: String
    var exact: Boolean
    var strict: Boolean
    var component: RClass<RProps>
    var render: (props: RouteResultProps<T>) -> ReactElement?
}

/**
 * Okta Secure Route
 * */
fun RBuilder.secureRoute(
    path: String,
    exact: Boolean = false,
    strict: Boolean = false,
    render: () -> ReactElement?
): ReactElement {
    return child<SecureRouteProps<RProps>, SecureRouteComponent<RProps>> {
        attrs {
            this.path = path
            this.exact = exact
            this.strict = strict
            this.render = { render() }
        }
    }
}