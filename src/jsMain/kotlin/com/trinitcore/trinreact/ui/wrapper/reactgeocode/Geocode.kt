package com.trinitcore.trinreact.ui.wrapper.reactgeocode

import kotlin.js.Promise

@JsModule("react-geocode") @JsNonModule
private external val geocodeModule: dynamic
@Suppress("UnsafeCastFromDynamic") val geocode: Geocode = geocodeModule.default

external class Geocode {

    fun setApiKey(key: String)

    fun setLanguage(langCode: String)

    fun setRegion(countryCode: String)

    fun enableDebug()

    fun fromLatLng(lat: Double, long: Double): Promise<GeoResponse>

    fun fromAddress(address: String): Promise<GeoResponse>

}