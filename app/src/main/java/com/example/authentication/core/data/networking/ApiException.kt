package com.example.authentication.core.data.networking

import io.ktor.client.statement.HttpResponse
import io.ktor.http.isSuccess

class ApiException(val statusCode: Int) : Exception("HTTP $statusCode")

fun HttpResponse.ensureSuccess(): HttpResponse {
    if (!status.isSuccess()) throw ApiException(status.value)
    return this
}