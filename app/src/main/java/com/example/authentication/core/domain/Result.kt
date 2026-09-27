package com.example.authentication.core.domain

sealed interface Result<out D, out E : RootError> {
    data class Success<out D>(val data: D) : Result<D, Nothing>
    data class Error<out E : RootError>(val error: E) : Result<Nothing, E>
}

// أداة مساعدة (Extension functions) لتسهيل التعامل مع النتيجة لاحقاً
inline fun <T, E : RootError, R> Result<T, E>.map(operation: (T) -> R): Result<R, E> {
    return when (this) {
        is Result.Success -> Result.Success(operation(data))
        is Result.Error -> Result.Error(error)
    }
}