package com.example.authentication.core.data.networking

import com.example.authentication.core.domain.Result
import com.example.authentication.core.domain.DataError
import kotlinx.serialization.SerializationException
import java.nio.channels.UnresolvedAddressException
import kotlin.coroutines.cancellation.CancellationException

inline fun <T> safeCall(execute: () -> T): Result<T, DataError.Network> {
    return try {
        Result.Success(execute())
    } catch (e: UnresolvedAddressException) {
        // لا يوجد انترنت أو العنوان غير موجود
        Result.Error(DataError.Network.NO_INTERNET)
    } catch (e: SerializationException) {
        // خطأ في قراءة وتحويل البيانات القادمة من السيرفر (JSON Parsing)
        Result.Error(DataError.Network.SERIALIZATION)
    } catch (e: Exception) {
        // إذا كان خطأ إلغاء الكوروتين (Cancellation)، يجب إعادته حتى لا نتسبب بمشاكل في تدفق البيانات
        if (e is CancellationException) throw e
        Result.Error(DataError.Network.UNKNOWN)
    }
}