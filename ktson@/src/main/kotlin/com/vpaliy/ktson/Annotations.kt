package com.vpaliy.ktson

import kotlin.reflect.KClass

@Target(AnnotationTarget.PROPERTY,AnnotationTarget.FIELD)
annotation class Exclude

@Target(AnnotationTarget.PROPERTY,AnnotationTarget.FIELD)
annotation class Name(val name:String)

@Target(AnnotationTarget.CLASS)
annotation class SkipAs(val skipped:String)

@Target(AnnotationTarget.PROPERTY,AnnotationTarget.FIELD)
annotation class DeserializeAbstract(val target:KClass<out Any>)

@Target(AnnotationTarget.PROPERTY,AnnotationTarget.FIELD)
annotation class CustomSerializer(val target:KClass<out ObjectSerializer<*,*>>)