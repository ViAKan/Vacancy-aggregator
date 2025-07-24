package ru.practicum.android.diploma.util

import ru.practicum.android.diploma.util.ResponseCode.CODE_ID_OTHER_REGIONS
import ru.practicum.android.diploma.util.ResponseCode.CODE_NO_CONNECTION
import ru.practicum.android.diploma.util.ResponseCode.CODE_REQUEST_FAILED
import ru.practicum.android.diploma.util.ResponseCode.CODE_SEARCH_SUCCESS
import ru.practicum.android.diploma.util.ResponseCode.CODE_SERVER_ERROR
import ru.practicum.android.diploma.util.ResponseCode.CODE_UNKNOWN

enum class ResponseType(val code: Int) {
    NO_CONNECTION(CODE_NO_CONNECTION),
    SEARCH_SUCCESS(CODE_SEARCH_SUCCESS),
    SERVER_ERROR(CODE_SERVER_ERROR),
    REQUEST_FAILED(CODE_REQUEST_FAILED),
    ID_OTHER_REGIONS(CODE_ID_OTHER_REGIONS),
    UNKNOWN(CODE_UNKNOWN);
}

fun Int.toResponseType(): ResponseType =
    ResponseType.values().find { it.code == this } ?: ResponseType.UNKNOWN


object ResponseCode {
    const val CODE_NO_CONNECTION = -1
    const val CODE_SEARCH_SUCCESS = 2
    const val CODE_SERVER_ERROR = 5
    const val CODE_REQUEST_FAILED = 1
    const val CODE_ID_OTHER_REGIONS = 1001
    const val CODE_UNKNOWN = -999
}
