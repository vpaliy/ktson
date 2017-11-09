package com.vpaliy.ktson

interface ObjectSerializer<Value>{
    fun toJson(value:Value):Any
    fun fromJson(json:Any):Value
}