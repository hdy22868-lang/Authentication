package com.example.authentication.core.data.networking

import com.example.authentication.core.domain.Result
import com.example.authentication.core.domain.DataError
import kotlinx.serialization.SerializationException
import java.nio.channels.UnresolvedAddressException
import kotlin.coroutines.cancellation.CancellationException

inline fun <T> safeCall(execute: () -> T): Result<T, DataError.Network> {
    return try {
        Result.Success(execute())
    } catch (e: CancellationException) {
        throw e
    } catch (e: ApiException) {
        Result.Error(
            when (e.statusCode) {
                401 -> DataError.Network.UNAUTHORIZED
                408 -> DataError.Network.REQUEST_TIMEOUT
                409 -> DataError.Network.CONFLICT
                413 -> DataError.Network.PAYLOAD_TOO_LARGE
                429 -> DataError.Network.TOO_MANY_REQUESTS
                400, 422 -> DataError.Network.BAD_REQUEST
                403 -> DataError.Network.FORBIDDEN
                404 -> DataError.Network.NOT_FOUND
                in 500..599 -> DataError.Network.SERVER_ERROR
                else -> DataError.Network.UNKNOWN
            }
        )
    } catch (e: SerializationException) {
        Result.Error(DataError.Network.SERIALIZATION)
    } catch (e: java.net.SocketTimeoutException) {
        Result.Error(DataError.Network.REQUEST_TIMEOUT)
    } catch (e: io.ktor.client.plugins.HttpRequestTimeoutException) {
        Result.Error(DataError.Network.REQUEST_TIMEOUT)
    } catch (e: java.io.IOException) {
        Result.Error(DataError.Network.NO_INTERNET)
    } catch (e: UnresolvedAddressException) {
        Result.Error(DataError.Network.NO_INTERNET)
    } catch (e: Exception) {
        Result.Error(DataError.Network.UNKNOWN)
    }
}