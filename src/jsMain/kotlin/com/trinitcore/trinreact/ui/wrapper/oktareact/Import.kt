@file:JsModule("@okta/okta-react")

package com.trinitcore.trinreact.ui.wrapper.oktareact

import com.clickcostz.fundemental.wrapper.oktareact.SecurityProps
import react.*

@JsName("Security") external class Security : Component<SecurityProps, RState> { override fun render(): ReactElement? }

@JsName("SecureRoute") external class SecureRouteComponent<T : RProps> : Component<SecureRouteProps<T>, RState> {
    override fun render(): ReactElement?
}

@JsName("LoginCallback") external class LoginCallback : Component<LoginCallbackProps, RState> { override fun render(): ReactElement? }

@JsName("withOktaAuth") external fun withOktaAuth(rClass: RClass<OktaAuthProps>) : FunctionalComponent<OktaAuthProps>