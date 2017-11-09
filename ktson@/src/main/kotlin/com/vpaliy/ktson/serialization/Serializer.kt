package com.vpaliy.ktson.serialization

import com.vpaliy.ktson.*
import kotlin.reflect.KProperty
import kotlin.reflect.KProperty1
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.memberProperties

class Serializer(val obj:Any){
    private val target=obj.javaClass.kotlin

    fun serialize()= buildString { serialize() }

    private fun StringBuilder.serialize():String{
        //transform all member properties which are not annotated with Exclude
        return target.memberProperties
                .filter { it.findAnnotation<Exclude>() == null }
                .joinTo(this, prefix = "{", postfix = "}"){
                    transform(it)
                    ""
                }.toString()
    }

    private fun StringBuilder.transform(property: KProperty1<Any, *>){
        val nameAnnotation=property.findAnnotation<Name>()
        //if the property has a custom name, use the annotation to fetch
        //Otherwise, use the property name
        val propertyName=nameAnnotation?.name?:property.name
        serializeString(propertyName)
        //name:value
        append(":")
        //get the value
        val value=property.get(obj)
        //if we have a custom serializer, use that serializer to convert the value to json
        //Otherwise, use the value and convert it to string
        val jsonValue=property.getSerializer()?.toJson(value)?:value
        serializePropertyValue(jsonValue)

    }

    private fun KProperty<*>.getSerializer(): ObjectSerializer<Any?>? {
        val customSerializerAnn = findAnnotation<CustomSerializer>() ?: return null
        val serializerClass = customSerializerAnn.target
        val valueSerializer = serializerClass.objectInstance
                ?:serializerClass.createInstance()
        @Suppress("UNCHECKED_CAST")
        return valueSerializer as ObjectSerializer<Any?>
    }

    private fun StringBuilder.serializeString(s: String) {
        append('\"')
        s.forEach { append(it.escape()) }
        append('\"')
    }

    private fun StringBuilder.serializePropertyValue(value: Any?) {
        when (value) {
            null -> append("null")
            is String -> serializeString(value)
            is Number, is Boolean -> append(value.toString())
            else -> Serializer(value).serialize()
        }
    }

    private fun Char.escape(): Any =
            when (this) {
                '\\' -> "\\\\"
                '\"' -> "\\\""
                '\b' -> "\\b"
                '\u000C' -> "\\f"
                '\n' -> "\\n"
                '\r' -> "\\r"
                '\t' -> "\\t"
                else -> this
            }
}