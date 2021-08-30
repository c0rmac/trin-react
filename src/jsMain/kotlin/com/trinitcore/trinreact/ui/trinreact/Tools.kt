package com.trinitcore.trinreact.ui.trinreact

fun packageForStyle(vararg packages: String) = packages.joinToString("-") { it.replace(".", "-") }
fun pathForResource(resource: String, vararg packages: String) = "/app_static/" + packages.joinToString("/") { it.replace(".", "/") } + "/$resource"

fun String.sub(s: String): String = "$this.$s"

fun <T : Any> JsClass<T>.newInstance(): T {
    inline fun callCtor(ctor: dynamic) = js("new ctor()")
    return callCtor(asDynamic()) as T
}

fun Map<String, String>.mergeWith(another: Map<String, String>): Map<String, String> {
    val unionList: MutableMap<String, String> = toMutableMap()
    for ((key, value) in another) {
        unionList[key] = listOfNotNull(unionList[key], value).toSet().joinToString(", ")
    }
    return unionList
}

fun checkIsPrimitive(value: Any?): Boolean {
    return value is Boolean ||
            value is Char ||
            value is Byte ||
            value is Short ||
            value is Int ||
            value is Long ||
            value is Float ||
            value is Double ||
            value is Unit ||
            value is String
}

fun Double.toFixed(digits: Int) = this.asDynamic().toFixed(digits) as String