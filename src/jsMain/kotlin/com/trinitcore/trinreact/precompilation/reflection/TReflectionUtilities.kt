package com.trinitcore.trinreact.precompilation.reflection

import com.trinitcore.trinreact.precompilation.reflection.TClass
import kotlin.reflect.KClass

class TReflectionUtilities(
    private val kClassesToTClasses: Map<KClass<*>, TClass>,
    private val qualifiedPackagesToKClasses: Map<String, KClass<*>>
) {

    fun findClass(kClass: KClass<*>) = kClassesToTClasses[kClass]
    fun findKClass(qualifiedPackageName: String) = qualifiedPackagesToKClasses[qualifiedPackageName]

}