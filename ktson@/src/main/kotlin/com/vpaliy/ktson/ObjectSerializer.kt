package com.vpaliy.ktson

interface ObjectSerializer<Value,Json>{
    fun toJson(value:Value):Json
    fun fromJson(json:Json):Value
}