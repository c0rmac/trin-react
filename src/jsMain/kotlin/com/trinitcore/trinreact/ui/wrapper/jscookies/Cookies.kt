package com.trinitcore.trinreact.ui.wrapper.jscookies

@JsModule("js-cookie") external val cookies: Cookies

external class Cookies {

    fun set(key: String, value: Any?, options: CookieOptions)

    fun get(key: String, options: CookieOptions): dynamic

    fun remove(key: String, options: CookieOptions)

}