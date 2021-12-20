package com.trinitcore.trinreact.ui

import com.trinitcore.trinreact.exception.InternalServerRESTException
import com.trinitcore.trinreact.exception.RESTException
import com.trinitcore.trinreact.precompilation.reflection.TReflectionUtilities
import com.trinitcore.trinreact.ui.trinreact.RESTAPIManager
import kotlinx.serialization.json.Json
import kotlin.reflect.KClass

abstract class Context(
    val appName: String, val apiUrlPrefix: String, val reflection: TReflectionUtilities, val unauthorisedRedirect: String = "",
    exceptions: Array<KClass<out RESTException>>
) {

    abstract val defaultJson: Json

    /* TUS : REST API */
    val restAPIExceptionRegistry = buildRESTAPIExceptionRegistry(*exceptions)

    // val restapiManager: RESTAPIManager = RESTAPIManager(this)

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