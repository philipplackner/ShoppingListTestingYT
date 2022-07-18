package com.androiddevs.shoppinglisttestingyt.base

/*data class Resource<T> {
    data class Success<T>(val data: T) : Resource<T>()
    data class Error(val exception: Throwable) : Resource<Nothing>()
    object Loading : Resource<Nothing>()
}*/

data class Resource<out T>(val status: Status, val data: T?, val message: String?) {
    companion object {
        fun <T> success(data: T?): Resource<T> {
            return Resource(Status.SUCCESS, data, null)
        }

        fun <T> error(message: String?, data: T? = null): Resource<T> {
            return Resource(Status.ERROR, data, message)
        }

        fun <T> loading(data: T? = null): Resource<T> {
            return Resource(Status.LOADING, data, null)
        }
    }
}

enum class Status {
    SUCCESS,
    ERROR,
    LOADING
}