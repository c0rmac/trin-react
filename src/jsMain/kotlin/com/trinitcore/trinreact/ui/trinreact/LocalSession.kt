package com.trinitcore.trinreact.ui.trinreact

import com.trinitcore.trinreact.ui.Context
import com.trinitcore.trinreact.ui.app.material.App
import kotlinx.browser.document
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlin.js.Date

external fun decodeURIComponent(encodedURI: String): String
external fun encodeURIComponent(decodedURI: String): String

object LocalSession {

    const val COOKIE_EXPIRATION = 30

    // TUS : SESSION
    // TUS : For the global session
    inline fun <reified T>setGlobalSessionAttr(context: Context, key: Enum<*>, value: T?) {
        setSessionAttrForComp<T>(context, App.componentIdentifier, key, value)
    }

    inline fun <reified T>getGlobalSessionAttr(context: Context, key: Enum<*>): T? {
        return getSessionAttrForComp(context, App.componentIdentifier, key)
    }
    // DEIREADH : For the global session

    // TUS : TEMP ISSUE RESOLUTION - There appeared to be an issue when persisting cookies. The solution for now is to store cookie data to the memory
    val inMemorySession = hashMapOf<String, Any?>()

    val usesInMemorySession: Boolean by lazy {
        /*
        val platform = window.navigator.platform.toLowerCase()
        platform.contains("iphone") || platform.contains("ipod") || platform.contains("ipad") ||
                platform.contains("mac")

         */
        false
    }
    // DEIREADH : TEMP ISSUE RESOLUTION

    fun setCookie(cname: String, cvalue: String, exdays: Int, path: String) {
        val d = Date(Date().getTime() + (exdays*24*60*60*1000))
        val expires = "expires="+ d.toUTCString()
        val cookieString = "$path-$cname=$cvalue;$expires;path=/"
        val enc = encodeURIComponent(cookieString)
        /* Issue: Can multiple cookies be stored?? */
        document.cookie = enc
    }

    fun getCookie(cname: String, path: String): String {
        val name = "$path-$cname="
        val ca = decodeURIComponent(document.cookie).split(';')
        for (i in ca.indices) {
            var c = ca[i]
            while (c[0] == ' ') {
                c = c.substring(1)
            }
            if (c.indexOf(name) == 0) {
                return c.substring(name.length, c.length)
            }
        }
        return ""
    }

    // TUS : For a component's session
    inline fun <reified T>setSessionAttrForComp(context: Context, componentIdentifier: String, key: Enum<*>, value: T?) {
        if (usesInMemorySession) {
            inMemorySession["$componentIdentifier${key.name}"] = value
        } else {
            value?.let {
                setCookie(key.name, context.defaultJson.encodeToString(value), COOKIE_EXPIRATION, componentIdentifier)
            } ?: kotlin.run { setCookie(key.name, "", COOKIE_EXPIRATION, componentIdentifier) }
        }
    }

    inline fun <reified T>getSessionAttrForComp(context: Context, componentIdentifier: String, key: Enum<*>): T? {
        return if (usesInMemorySession) {
            inMemorySession["$componentIdentifier${key.name}"] as? T?
        } else {
            val serializedValue = getCookie(key.name, componentIdentifier)
            console.log("serializedValue", serializedValue)
            if (serializedValue.isNotBlank())
                context.defaultJson.decodeFromString<T>(serializedValue)
            else null
        }
    }
    // DEIREADH : For a component's session
    // DEIREADH : SESSION

}