package com.example.authentication.core.domain

sealed interface DataError : RootError {

    // أخطاء شبكة الإنترنت (مثل عدم وجود إنترنت، انتهاء المهلة، أخطاء السيرفر)
    enum class Network : DataError {
        NOT_FOUND,
        FORBIDDEN,
        BAD_REQUEST,
        REQUEST_TIMEOUT,
        UNAUTHORIZED,
        CONFLICT,
        TOO_MANY_REQUESTS,
        NO_INTERNET,
        PAYLOAD_TOO_LARGE,
        SERVER_ERROR,
        SERIALIZATION,
        UNKNOWN
    }

    // أخطاء التخزين المحلي (مثل فشل القراءة من DataStore)
    enum class Local : DataError {
        DISK_FULL,
        NOT_FOUND,
        UNKNOWN
    }
}