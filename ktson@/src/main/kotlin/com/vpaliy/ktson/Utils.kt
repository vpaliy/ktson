package com.vpaliy.ktson

import kotlin.reflect.KClass

internal fun <T : Any> KClass<T>.createInstance(): T {
    val noArgConstructor = constructors.find {
        it.parameters.isEmpty()
    }
    noArgConstructor ?: throw IllegalArgumentException(
            "Class must have a no-argument constructor")
    return noArgConstructor.call()
}
