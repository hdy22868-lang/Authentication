package com.example.authentication.core.component.localization

import com.example.authentication.R
import com.example.authentication.core.domain.DataError

fun DataError.toUiText(): UiText {
    return when (this) {
        DataError.Network.NO_INTERNET -> UiText.StringResource(R.string.error_no_internet)
        DataError.Network.REQUEST_TIMEOUT -> UiText.StringResource(R.string.error_request_timeout)
        DataError.Network.UNAUTHORIZED -> UiText.StringResource(R.string.error_unauthorized)
        DataError.Network.SERVER_ERROR -> UiText.StringResource(R.string.error_server)
        DataError.Network.CONFLICT -> UiText.StringResource(R.string.error_conflict)
        DataError.Network.TOO_MANY_REQUESTS -> UiText.StringResource(R.string.error_too_many_requests)
        else -> UiText.StringResource(R.string.error_unknown)
    }
}