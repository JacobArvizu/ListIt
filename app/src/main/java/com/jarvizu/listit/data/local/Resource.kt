package com.jarvizu.listit.data.local

data class Resource<out T>(
    val status: Status,
    val data: T?,
    val message:String?
){
    companion object{

        fun <T> success(data:T?): Resource<T>{
            return Resource(Status.SUCCESS, data, null)
        }

        fun <T> error(): Resource<T>{
            return Resource(Status.ERROR, null, null)
        }

        fun <T> loading(data:T?): Resource<T>{
            return Resource(Status.LOADING, data, null)
        }

    }
}