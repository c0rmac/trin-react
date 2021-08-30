package com.trinitcore.trinreact.ui.app.material

import com.trinitcore.trinreact.exception.InternalServerRESTException
import com.trinitcore.trinreact.exception.RESTException
import com.trinitcore.trinreact.precompilation.reflection.TReflectionUtilities
import kotlinx.serialization.json.Json
import kotlin.reflect.KClass

abstract class Context(
    val appName: String, val apiUrlPrefix: String, val reflection: TReflectionUtilities,
    vararg exceptions: KClass<out RESTException>
) {

    abstract val defaultJson: Json

    /* TUS : REST API */
    val restAPIExceptionRegistry = buildRESTAPIExceptionRegistry(*exceptions)

    private fun buildRESTAPIExceptionRegistry(vararg exceptions: KClass<out RESTException>)
            = exceptions.map { Pair(it.simpleName!!, it) }.toMutableList()
        .apply {
            // Include default exceptions which may be specified by the server
            this.add(Pair(RESTException::class.simpleName!!, RESTException::class))
            this.add(Pair(InternalServerRESTException::class.simpleName!!, InternalServerRESTException::class))
        }
        .toMap()
    /* DEIREADH : REST API */
}