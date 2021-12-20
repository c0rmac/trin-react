package com.trinitcore.trinreact.ui.trinreact

import com.trinitcore.trinreact.exception.InternalClientRESTException
import com.trinitcore.trinreact.exception.RESTError
import com.trinitcore.trinreact.ui.Context
import kotlinext.js.js
import kotlinx.browser.window
import kotlinx.coroutines.*
import kotlinx.serialization.*
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.serializer
import org.w3c.fetch.RequestInit
import org.w3c.fetch.Response
import kotlin.reflect.KClass

/** prefixVocabularyForGETRequests */
private val prefixVocabForGETReq = hashSetOf("get", "find", "fetch")
/** prefixVocabularyForPUTRequests */
private val prefixVocabForPUTReq = hashSetOf("put", "generate", "set", "create")

enum class MediaType(val value: String) {
    APPLICATION_FORM_URLENCODED("application/x-www-form-urlencoded"), TEXT_PLANE("text/plain")
}

class RESTAPIManager(
    val context: Context,
    /** Sub prefix of the requests. Must start with '/your_url' */ private val subPrefix: String = "",
    val post: MediaType = MediaType.TEXT_PLANE
) {

    private fun getParamNames(serializedFuncBody: String): Array<String> {
        var funcIsFound = false
        var funcSepFound = false
        /** parameterOpeningApprenticeFound */
        var paramOpningApprFound = false
        /** parameterClosingApprenticeFound */
        var paramClosingApprFound = false

        /** isSkippingReceiverParameter */
        var isSkippingRecParam = false

        /** continueAgain */
        var contAgain = false

        var serializedFuncParams = ""

        for (char in serializedFuncBody) {
            if (contAgain) {
                contAgain = false
                continue
            }

            if (char == 'f' && !funcIsFound) {
                funcIsFound = true
                continue
            }

            if (char == ' ' && !funcSepFound && funcIsFound) {
                funcSepFound = true
                continue
            }

            if (!funcSepFound) {
                continue
            }

            if (char == '(' && !paramOpningApprFound && funcIsFound && funcSepFound) {
                paramOpningApprFound = true
                // isSkippingRecParam = true
                continue
            }

            if (char == ')') {
                paramClosingApprFound = true
                break
            }

            if (isSkippingRecParam) {
                if (char == ',') {
                    contAgain = true
                    continue
                }
            }

            serializedFuncParams += char
        }

        val deSerializedFuncParams = serializedFuncParams.split(", ")
        return deSerializedFuncParams.toTypedArray()
    }

    companion object {
        /** getThrowableForUnspecifiedParameter */
        private fun getThrwbleForUnspecParam(funcName: String, paramName: String, index: Int) =
            Throwable("The statically typed parameter p$index which acts as a placeholder for $paramName is not defined for the function $funcName. Please define it.")

        /** apiInstanceParameterNameExclusions */
        private val apiInstanceParamNameExls = hashSetOf("continuation", "callback\$default", "webSession", "request")

        private val apiInstances = hashMapOf<String, Any>()
    }

    fun <T : Any> getAPIInstance(kClass: KClass<T>): T {
        return apiInstances.getOrPut(context.reflection.findClass(kClass)!!.qualifiedName) {
            createAPIInstance(kClass)
        } as T
    }

    /**
     * Creates an instance of the REST interface which is mirror implemented in the server web controller.
     * All functions are dynamically initialised. As of 05/06/2020, all functions, upon invocation, with the prefix "get" or "fetch"
     * will perform a GET request to the server. Every other function upon invocation will perform a POST request to the server.
     */
    @OptIn(InternalSerializationApi::class)
    fun <T : Any> createAPIInstance(kClass: KClass<T>): T {
        val a = kClass.js.newInstance()
        a.apply {
            // TUS : Method 1
            val tClass = context.reflection.findClass(kClass)
            // DEIREADH : Method 1

            // TUS : Method 2
            val apiProto = this.asDynamic()["__proto__"]
            val funcNames = js("Object").keys(apiProto).unsafeCast<Array<String>>()

            val serializedFuncBodiesWrtNames = js("Object").values(apiProto).unsafeCast<Array<dynamic>>()
            // DEIREADH : Method 2

            for ((i, funcName) in funcNames.withIndex()) {
                val serializedFuncBody = serializedFuncBodiesWrtNames[i]
                val invokableFuncName = funcName.split("_").first()
                val jsParamNames = getParamNames(serializedFuncBody.toString())
                val tFunction = tClass?.functions?.find { it.name == invokableFuncName }
                val tParamNames = tFunction?.params?.map { it.name }?.toTypedArray() ?: jsParamNames

                val funcStaticallyTypedBody: suspend (p0: Any?, p1: Any?, p2: Any?, p3: Any?, p4: Any?, p5: Any?, p6: Any?, p7: Any?, p8: Any?, p9: Any?, p10: Any?) -> Any? =
                    funcStaticallyTypedBody@{ p0: Any?, p1: Any?, p2: Any?, p3: Any?, p4: Any?, p5: Any?, p6: Any?, p7: Any?, p8: Any?, p9: Any?, p10: Any? ->
                        fun getStaticTypedParamValue(index: Int): Any? {
                            return when (index) {
                                0 -> p0
                                1 -> p1
                                2 -> p2
                                3 -> p3
                                4 -> p4
                                5 -> p5
                                6 -> p6
                                7 -> p7
                                8 -> p8
                                9 -> p9
                                10 -> p10
                                else -> throw getThrwbleForUnspecParam(funcName, tParamNames[index], index)
                            }
                        }

                        val params = mutableMapOf<String, Any?>()
                        for ((j, paramName) in tParamNames.withIndex()) {
                            if (!apiInstanceParamNameExls.contains(paramName))
                                params[paramName] = getStaticTypedParamValue(j)
                        }

                        val returnTypeQualifiedName = tFunction?.returnType?.qualifiedName
                        var firstTypeParameterQualifiedName: String? = null
                        if (returnTypeQualifiedName == "List") firstTypeParameterQualifiedName =
                            tFunction.returnType.typeParameters.first()

                        val innerFuncReturnQualName =
                            (firstTypeParameterQualifiedName ?: returnTypeQualifiedName) ?: throw IllegalStateException(
                                "TFunction not found for ${funcName}"
                            )
                        console.log("innerFuncReturnQualName", innerFuncReturnQualName)
                        console.log("firstTypeParameterQualifiedName", firstTypeParameterQualifiedName)
                        console.log("returnTypeQualifiedName", returnTypeQualifiedName)
                        val innerFuncReturnSerializer = context.reflection.findKClass(
                            innerFuncReturnQualName
                        )?.serializerOrNull()
                        if (innerFuncReturnSerializer == null)
                            console.warn("The serializer for the ${kClass::simpleName}.$invokableFuncName return type was not specified. Using an unsafe cast instead.")

                        val returnSerializer =
                            if (firstTypeParameterQualifiedName != null && innerFuncReturnSerializer != null)
                                ListSerializer(innerFuncReturnSerializer)
                            else if (innerFuncReturnSerializer != null) innerFuncReturnSerializer
                        else {
                            when (returnTypeQualifiedName) {
                                "String" -> String.serializer()
                                "Int" -> Int.serializer()
                                "Long" -> Long.serializer()
                                else -> null
                            }
                        }

                        for (prefix in prefixVocabForGETReq) {
                            if (invokableFuncName.startsWith(prefix)) {
                                return@funcStaticallyTypedBody sendGetRequest(
                                    invokableFuncName,
                                    returnSerializer,
                                    params.toMap()
                                )
                            }
                        }

                        for (prefix in prefixVocabForPUTReq) {
                            if (invokableFuncName.startsWith(prefix)) {
                                return@funcStaticallyTypedBody sendPutRequest(
                                    invokableFuncName,
                                    returnSerializer,
                                    params.toMap()
                                )
                            }
                        }


                        return@funcStaticallyTypedBody sendPostRequest(
                            invokableFuncName,
                            returnSerializer,
                            params.toMap()
                        )
                    }

                fun funcStaticallyTypedBody0(): dynamic {
                    val funcStaticallyTypedBody0: suspend () -> Any? = {
                        funcStaticallyTypedBody(null, null, null, null, null, null, null, null, null, null, null)
                    }
                    return funcStaticallyTypedBody0.asDynamic()
                }

                fun funcStaticallyTypedBody1(): dynamic {
                    val funcStaticallyTypedBody1: suspend (p0: Any?) -> Any? = { p0: Any? ->
                        funcStaticallyTypedBody(p0, null, null, null, null, null, null, null, null, null, null)
                    }
                    return funcStaticallyTypedBody1.asDynamic()
                }

                fun funcStaticallyTypedBody2(): dynamic {
                    val funcStaticallyTypedBody2: suspend (p0: Any?, p1: Any?) -> Any? = { p0: Any?, p1: Any? ->
                        funcStaticallyTypedBody(p0, p1, null, null, null, null, null, null, null, null, null)
                    }
                    return funcStaticallyTypedBody2.asDynamic()
                }

                fun funcStaticallyTypedBody3(): dynamic {
                    val funcStaticallyTypedBody3: suspend (p0: Any?, p1: Any?, p2: Any?) -> Any? =
                        { p0: Any?, p1: Any?, p2: Any? ->
                            funcStaticallyTypedBody(p0, p1, p2, null, null, null, null, null, null, null, null)
                        }
                    return funcStaticallyTypedBody3.asDynamic()
                }

                fun funcStaticallyTypedBody4(): dynamic {
                    val funcStaticallyTypedBody4: suspend (p0: Any?, p1: Any?, p2: Any?, p3: Any?) -> Any? =
                        { p0: Any?, p1: Any?, p2: Any?, p3: Any? ->
                            funcStaticallyTypedBody(p0, p1, p2, p3, null, null, null, null, null, null, null)
                        }
                    return funcStaticallyTypedBody4.asDynamic()
                }

                fun funcStaticallyTypedBody5(): dynamic {
                    val funcStaticallyTypedBody5: suspend (p0: Any?, p1: Any?, p2: Any?, p3: Any?, p4: Any?) -> Any? =
                        { p0: Any?, p1: Any?, p2: Any?, p3: Any?, p4: Any? ->
                            funcStaticallyTypedBody(p0, p1, p2, p3, p4, null, null, null, null, null, null)
                        }
                    return funcStaticallyTypedBody5.asDynamic()
                }

                fun funcStaticallyTypedBody6(): dynamic {
                    val funcStaticallyTypedBody6: suspend (p0: Any?, p1: Any?, p2: Any?, p3: Any?, p4: Any?, p5: Any?) -> Any? =
                        { p0: Any?, p1: Any?, p2: Any?, p3: Any?, p4: Any?, p5: Any? ->
                            funcStaticallyTypedBody(p0, p1, p2, p3, p4, p5, null, null, null, null, null)
                        }
                    return funcStaticallyTypedBody6.asDynamic()
                }

                fun funcStaticallyTypedBody7(): dynamic {
                    val funcStaticallyTypedBody7: suspend (p0: Any?, p1: Any?, p2: Any?, p3: Any?, p4: Any?, p5: Any?, p6: Any?) -> Any? =
                        { p0: Any?, p1: Any?, p2: Any?, p3: Any?, p4: Any?, p5: Any?, p6: Any? ->
                            funcStaticallyTypedBody(p0, p1, p2, p3, p4, p5, p6, null, null, null, null)
                        }
                    return funcStaticallyTypedBody7.asDynamic()
                }

                fun funcStaticallyTypedBody8(): dynamic {
                    val funcStaticallyTypedBody8: suspend (p0: Any?, p1: Any?, p2: Any?, p3: Any?, p4: Any?, p5: Any?, p6: Any?, p7: Any?) -> Any? =
                        { p0: Any?, p1: Any?, p2: Any?, p3: Any?, p4: Any?, p5: Any?, p6: Any?, p7: Any? ->
                            funcStaticallyTypedBody(p0, p1, p2, p3, p4, p5, p6, p7, null, null, null)
                        }
                    return funcStaticallyTypedBody8.asDynamic()
                }

                fun funcStaticallyTypedBody9(): dynamic {
                    val funcStaticallyTypedBody9: suspend (p0: Any?, p1: Any?, p2: Any?, p3: Any?, p4: Any?, p5: Any?, p6: Any?, p7: Any?, p8: Any?) -> Any? =
                        { p0: Any?, p1: Any?, p2: Any?, p3: Any?, p4: Any?, p5: Any?, p6: Any?, p7: Any?, p8: Any? ->
                            funcStaticallyTypedBody(p0, p1, p2, p3, p4, p5, p6, p7, p8, null, null)
                        }
                    return funcStaticallyTypedBody9.asDynamic()
                }

                fun funcStaticallyTypedBody10(): dynamic {
                    val funcStaticallyTypedBody10: suspend (p0: Any?, p1: Any?, p2: Any?, p3: Any?, p4: Any?, p5: Any?, p6: Any?, p7: Any?, p8: Any?, p9: Any?) -> Any? =
                        { p0: Any?, p1: Any?, p2: Any?, p3: Any?, p4: Any?, p5: Any?, p6: Any?, p7: Any?, p8: Any?, p9: Any? ->
                            funcStaticallyTypedBody(p0, p1, p2, p3, p4, p5, p6, p7, p8, p9, null)
                        }
                    return funcStaticallyTypedBody10.asDynamic()
                }

                fun funcStaticallyTypedBody11(): dynamic {
                    val funcStaticallyTypedBody11: suspend (p0: Any?, p1: Any?, p2: Any?, p3: Any?, p4: Any?, p5: Any?, p6: Any?, p7: Any?, p8: Any?, p9: Any?, p10: Any?) -> Any? =
                        { p0: Any?, p1: Any?, p2: Any?, p3: Any?, p4: Any?, p5: Any?, p6: Any?, p7: Any?, p8: Any?, p9: Any?, p10: Any? ->
                            funcStaticallyTypedBody(p0, p1, p2, p3, p4, p5, p6, p7, p8, p9, p10)
                        }
                    return funcStaticallyTypedBody11.asDynamic()
                }

                /* Wrapper to handle additional coroutine callback parameter generated by the suspend function. */
                this.asDynamic()[funcName] =
                    { p0: Any?, p1: Any?, p2: Any?, p3: Any?, p4: Any?, p5: Any?, p6: Any?, p7: Any?, p8: Any?, p9: Any?, p10: Any?, p11: Any? ->
                        /* Where the first case calls a function with 0 parameters and
                        ** the last function calls a function with paramNames.size - 2 parameters.
                        * (The two parameters are kotlin generated coroutine callbacks)
                        */
                        when (tParamNames.size + 1) {
                            1 -> funcStaticallyTypedBody0()(p0)
                            2 -> funcStaticallyTypedBody1()(p0, p1)
                            3 -> funcStaticallyTypedBody2()(p0, p1, p2)
                            4 -> funcStaticallyTypedBody3()(p0, p1, p2, p3)
                            5 -> funcStaticallyTypedBody4()(p0, p1, p2, p3, p4)
                            6 -> funcStaticallyTypedBody5()(p0, p1, p2, p3, p4, p5)
                            7 -> funcStaticallyTypedBody6()(p0, p1, p2, p3, p4, p5, p6)
                            8 -> funcStaticallyTypedBody7()(p0, p1, p2, p3, p4, p5, p6, p7)
                            9 -> funcStaticallyTypedBody8()(p0, p1, p2, p3, p4, p5, p6, p7, p8)
                            10 -> funcStaticallyTypedBody9()(p0, p1, p2, p3, p4, p5, p6, p7, p8, p9)
                            11 -> funcStaticallyTypedBody10()(p0, p1, p2, p3, p4, p5, p6, p7, p8, p9, p10)
                            12 -> funcStaticallyTypedBody11()(p0, p1, p2, p3, p4, p5, p6, p7, p8, p9, p10, p11)
                            else -> throw getThrwbleForUnspecParam(
                                funcName,
                                tParamNames[tParamNames.size - 2],
                                tParamNames.size - 1
                            )
                        }
                    }
            }
        }

        return a as T
    }

    @OptIn(InternalSerializationApi::class)
    suspend fun <T> sendRequest(
        method: String,
        webListnerFuncName: String,
        serializer: KSerializer<T>?,
        params: Map<String, Any?>
    ): T? {
        var jsonStringRequestBody: String? = null
        var queryString = ""

        if (
            params.size == 1
            && params.values.firstOrNull()?.let { checkIsPrimitive(it) || it is Enum<*> } != true
            && method != "GET"
        ) {
            console.log(params)
            // Method 1 - Use the only object parameter as the body for the request. This is subject to GET request constraints.
            params.values.firstOrNull()?.let { value ->
                console.log(value)
                val firstParamSerializer = value::class.serializerOrNull() as? KSerializer<Any>?
                val firstClass = value::class
                val firstTClass = context.reflection.findClass(firstClass)!!
                val firstName = firstTClass.qualifiedName
                val secondPackage = firstName.split(".").toMutableList().apply { removeLast() }.joinToString(".")
                val secondClass = context.reflection.findKClass(secondPackage)
                val secondParamSerializer = secondClass?.serializerOrNull() as? KSerializer<Any>?

                val paramSerializer = if (secondParamSerializer is SealedClassSerializer) secondParamSerializer else firstParamSerializer

                jsonStringRequestBody = paramSerializer?.let { context.defaultJson.encodeToString(it, value) } ?: run {
                    console.warn("Unable to serialize using standard method")
                    JSON.stringify(value)
                }
                // jsonStringRequestBody = serializer?.let { Json.encodeToString(value) } ?: JSON.stringify(value)
            }
        } else {
            // Method 2 - Serialize the attributes of the objects and the primitives from the function argument into a query string
            queryString = params.mapNotNull {
                when {
                    it.value == null -> {
                        null
                    }
                    checkIsPrimitive(it.value) -> {
                        it.key + "=" + it.value
                    }
                    it.value is Enum<*> -> {
                        it.key + "=" + (it.value as Enum<*>).name
                    }
                    else -> {
                        when (val value = it.value) {
                            is Array<*> -> {
                                it.key + "=" + value.joinToString(",")
                            }
                            is Enum<*> -> it.key + "=" + value.name
                            else -> {
                                val a = js("Object").keys(it.value).unsafeCast<Array<String>>()
                                val b = js("Object").values(it.value).unsafeCast<Array<Any?>>()

                                val c = mutableListOf<String>()
                                // a.forEachIndexed { index, t -> c.add("$c=${js("encodeURIComponent")(b[index])}") }
                                a.forEachIndexed { index, t ->
                                    if (b[index] != null) c.add(
                                        "$t=${
                                            js("encodeURIComponent")(
                                                b[index]
                                            )
                                        }"
                                    )
                                }
                                c.joinToString("&")
                            }
                        }
                    }
                }
            }.joinToString("&")
        }

        console.log("queryString", queryString)

        val usePostUrlEncoded = (jsonStringRequestBody.isNullOrEmpty() && method == "POST" && post == MediaType.APPLICATION_FORM_URLENCODED)
        val fetchUrl = "${context.apiUrlPrefix}${this.subPrefix}/$webListnerFuncName" + if (!usePostUrlEncoded) "?$queryString" else ""

        val response = window.fetch(
            fetchUrl,
            RequestInit(
                method = method,
                headers = js {
                    Accept = "application/json";
                    this["Accept-Language"] = window.navigator.language

                    this["Content-Type"] = if (usePostUrlEncoded) {
                        post.value
                    } else {
                        "application/json"
                    }
                },
                body = if (usePostUrlEncoded) queryString else jsonStringRequestBody
            )
        )
            .await()

        if (response.status == 200.toShort()) {
            return serialize(response, serializer)
        } else {
            val restError = try {
                serialize(response, RESTError.serializer())
            } catch (e: Exception) {
                throw InternalClientRESTException("A serialization error occurred", language, e)
            }

            restError?.let {
                val exceptionClass = context.restAPIExceptionRegistry[restError.exceptionIdentifier]
                exceptionClass?.let {
                    val exception = exceptionClass.js.newInstance()
                    exception.defaultText = restError.message
                    throw exception
                } ?: throw InternalClientRESTException(
                    "No class was defined for the exception with identifier ${restError.exceptionIdentifier}",
                    language
                )
            } ?: throw InternalClientRESTException(
                "The errored response failed to parse",
                language
            )
        }
    }

    suspend fun <T> serialize(
        response: Response,
        serializer: KSerializer<T>?
    ): T? {
        return try {
            if (serializer != null) {
                val textResponse = response.text().await()

                when (serializer) {
                    String.serializer() -> textResponse as T?
                    Int.serializer() -> textResponse.toInt() as T?
                    Double.serializer() -> textResponse.toDouble() as T?
                    else -> context.defaultJson.decodeFromString(serializer, textResponse)
                }
            } else {
                response.json().await().unsafeCast<T>()?.let {
                    if (it is Array<*>) it.toList() as T?
                    else it
                }
            }
        } catch (e: Throwable) {
            console.error(e)
            null
        }
    }

    suspend fun <T> sendPutRequest(
        webListnerFuncName: String,
        serializer: KSerializer<T>?,
        params: Map<String, Any?>
    ): T? {
        return sendRequest("PUT", webListnerFuncName, serializer, params)
    }

    suspend fun <T> sendPostRequest(
        webListnerFuncName: String,
        serializer: KSerializer<T>?,
        params: Map<String, Any?>
    ): T? {
        return sendRequest("POST", webListnerFuncName, serializer, params)
    }

    suspend fun <T> sendGetRequest(
        webListnerFuncName: String,
        serializer: KSerializer<T>?,
        params: Map<String, Any?>
    ): T? {
        return sendRequest("GET", webListnerFuncName, serializer, params)
    }

}