package com.trinitcore.trinreact.precompilation.reflection

import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

enum class TClassType {STANDARD, INTERFACE, ABSTRACT}

@Serializable
class TClass(val qualifiedPackage: String, val name: String, val functions: Array<TFunction>, val classes: Array<TClass>, val type: TClassType) {
    val qualifiedName
    get() = "$qualifiedPackage.$name"
}

@Serializable
class TFunction(val qualifiedPackage: String, val name: String, val params: Array<TParam>, val returnType: TReturnType)

@Serializable
class TParam(val qualifiedPackage: String, val name: String, val type: String)

@Serializable
class TReflectionContext(val classes: Array<TClass>,
                         val functions: Array<TFunction>)

@Serializable
class TReturnType(val qualifiedName: String, val typeParameters: Array<String>)