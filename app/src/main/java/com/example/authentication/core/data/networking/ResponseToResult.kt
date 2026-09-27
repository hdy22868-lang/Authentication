package com.example.authentication.core.data.networking

import com.example.authentication.core.domain.DataError
import io.ktor.client.statement.HttpResponse
import com.example.authentication.core.domain.Result
import io.ktor.client.call.body

suspend inline fun <reified T> responseToResult(
    response: HttpResponse
): Result<T, DataError.Network> {
    return when (response.status.value) {
        in 200..299 -> {
            try {
                Result.Success(response.body<T>())
            } catch (e: Exception) {
                Result.Error(DataError.Network.SERIALIZATION)
            }
        }
        401 -> Result.Error(DataError.Network.UNAUTHORIZED) // غير مصرح (التوكن انتهى أو غير صحيح)
        408 -> Result.Error(DataError.Network.REQUEST_TIMEOUT) // انتهت مهلة الاتصال
        409 -> Result.Error(DataError.Network.CONFLICT) // تعارض بيانات
        413 -> Result.Error(DataError.Network.PAYLOAD_TOO_LARGE) // الحجم كبير جداً
        429 -> Result.Error(DataError.Network.TOO_MANY_REQUESTS) // طلبات كثيرة في وقت قصير
        in 500..599 -> Result.Error(DataError.Network.SERVER_ERROR) // خطأ من السيرفر
        else -> Result.Error(DataError.Network.UNKNOWN)
    }
}