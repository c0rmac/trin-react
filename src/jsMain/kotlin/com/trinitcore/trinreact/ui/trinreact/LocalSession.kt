package com.trinitcore.trinreact.ui.trinreact

import com.trinitcore.trinreact.ui.app.material.App
import kotlinx.serialization.KSerializer

object LocalSession {

    // TUS : SESSION
    // TUS : For the global session
    fun <T>setGlobalSessionAttr(key: Enum<*>, value: T?, serializer: KSerializer<T>? = null) {
        setSessionAttrForComp<T>(App.componentIdentifier, key, value, serializer)
    }

    fun <T>getGlobalSessionAttr(key: Enum<*>, serializer: KSerializer<*>? = null): T? {
        return getSessionAttrForComp(App.componentIdentifier, key, serializer)
    }
    // DEIREADH : For the global session

    // TUS : TEMP ISSUE RESOLUTION - There appeared to be an issue when persisting cookies. The solution for now is to store cookie data to the memory
    private val inMemorySession = hashMapOf<String, Any?>()

    private val usesInMemorySession: Boolean by lazy {
        /*
        val platform = window.navigator.platform.toLowerCase()
        platform.contains("iphone") || platform.contains("ipod") || platform.contains("ipad") ||
                platform.contains("mac")

         */
        true
    }
    // DEIREADH : TEMP ISSUE RESOLUTION

    // TUS : For a component's session
    fun <T>setSessionAttrForComp(componentIdentifier: String, key: Enum<*>, value: T?, serializer: KSerializer<T>? = null) {
        if (usesInMemorySession) {
            inMemorySession["$componentIdentifier${key.name}"] = value
        } else {
            throw IllegalStateException("Cookies not being used")
            /*
            var valueType = "primitive"
            val persistValue = if (checkIsPrimitive(value)) value.toString()
            else {
                var serializedValue: String? = null

                // Handle none primitive types
                when {
                    value is Enum<*> -> valueType = Enum::class.simpleName!!
                    value is Array<*> -> valueType = Array<Any?>::class.simpleName!!
                    value is MutableList<*> -> valueType = MutableList::class.simpleName!!
                    value is List<*> -> valueType = List::class.simpleName!!
                    value != null -> valueType = "generic" /* throw UnsupportedTypeForLocalSessionException(value) */
                }

                if (serializedValue == null && value != null)
                    serializedValue = serializer?.let { defaultJson.stringify<T>(it as SerializationStrategy<T>, value) }
                            ?: JSON.stringify(value)

                serializedValue
            }

            cookies.set(key.name, persistValue, CookieOptions(path = "/$componentIdentifier"))
            cookies.set("${key.name}-type", valueType, CookieOptions(path = "/$componentIdentifier"))
             */
        }
    }

    fun <T>getSessionAttrForComp(componentIdentifier: String, key: Enum<*>, serializer: KSerializer<*>? = null): T? {
        if (usesInMemorySession) {
            return inMemorySession["$componentIdentifier${key.name}"] as? T?
        } else {
            throw IllegalStateException("Cookies not being used")
            /*
            val serializedValue = cookies.get(key.name, CookieOptions(path = "/$componentIdentifier"))
            val valueType = cookies.get("${key.name}-type", CookieOptions(path = "/$componentIdentifier"))

            try {
                if (serializedValue != null && valueType != "primitive") {
                    val deserializedValue = serializer?.let {
                        defaultJson.parse(it as DeserializationStrategy<T>, serializedValue)
                    } ?: JSON.parse<T>(serializedValue)
                    var returnValue = deserializedValue

                    // TUS : Post deserialization for when the serializer is not specified
                    if (serializer == null) {
                        when (valueType) {
                            Enum::class.simpleName -> {
                                // TUS : INJECTION - Inject comparison logic for equals() call for the deserialized enum
                                val equalsFunc: (Any?) -> Boolean = equalFunc@{ other: Any? ->
                                    if (other is Enum<*>) {
                                        // Issue - This is not a completed fix for the issue.
                                        return@equalFunc other.name == deserializedValue.asDynamic()["name\$"]
                                    }
                                    return@equalFunc false
                                }

                                deserializedValue.asDynamic()["__proto__"] = kotlinext.js.js {
                                    equals = equalsFunc
                                }
                                // DEIREADH : INJECTION - Inject comparison logic for equals() call for the deserialized enum
                            }
                            MutableList::class.simpleName -> {
                                // MutableList is stored as an array. Convert back to MutableList
                                returnValue = (deserializedValue as Array<*>).toMutableList() as T
                            }
                        }
                    }
                    // DEIREADH : Post deserialization for when the serializer is not specified

                    return returnValue
                } else {
                    return serializedValue
                }
            } catch (e: Throwable) {
                console.error(e)
            }
            return null
             */
        }
    }
    // DEIREADH : For a component's session
    // DEIREADH : SESSION

}