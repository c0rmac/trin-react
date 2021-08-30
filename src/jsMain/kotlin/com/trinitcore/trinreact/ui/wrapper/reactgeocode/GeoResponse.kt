package com.trinitcore.trinreact.ui.wrapper.reactgeocode

external class GeoResponse {

    val results: Array<GeoResult>

    @JsName("formatted_address")
    val formattedAddress: String

}