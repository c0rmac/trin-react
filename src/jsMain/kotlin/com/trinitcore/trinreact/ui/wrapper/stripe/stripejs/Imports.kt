@file:JsModule("@stripe/stripe-js")
@file:JsNonModule

package com.trinitcore.trinreact.ui.wrapper.stripe.stripejs

import kotlin.js.Promise

// @JsName("loadStripe")
external fun loadStripe(apiKey: String): Promise<Stripe>

external class Stripe {
    fun createPaymentMethod(params: dynamic): Promise<PaymentMethodContext>
    fun confirmCardPayment(clientSecret: String, params: dynamic): Promise<PaymentIntentContext>
}

external class PaymentMethodContext {
    val paymentMethod: PaymentMethod
    val error: dynamic
}

external class PaymentMethod {

}

external class PaymentIntentContext {
    val paymentIntent: PaymentIntent?
    val error: Error?
}

external class PaymentIntent {
    val id: String
}

external class Error {
    val message: String
}