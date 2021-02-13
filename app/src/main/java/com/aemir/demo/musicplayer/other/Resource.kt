package com.aemir.demo.musicplayer.other

// out is for super class
// ex: if T is Number then Integer, Double, etc can be used for T
data class Resource<out T>(val status: Status, val data: T?, val message: String?) {

    companion object {
        fun <T> success(data: T?) = Resource(Status.SUCCESS, data, null)
        fun <T> error(message: String, data: T?) = Resource(Status.ERROR, data, message)
        fun <T> loading(data: T?) = Resource(Status.LOADING, data, null)
    }

}

enum class Status {
    SUCCESS,
    ERROR,
    LOADING
}